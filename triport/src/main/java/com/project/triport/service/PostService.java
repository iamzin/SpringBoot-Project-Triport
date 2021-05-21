package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import com.project.triport.entity.PostHashtag;
import com.project.triport.entity.PostLike;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.PostHashtagRepository;
import com.project.triport.repository.PostLikeRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.requestDto.VideoNameDto;
import com.project.triport.requestDto.VideoUrlDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.util.APIUtil;
import com.project.triport.util.VideoProbeResult;
import com.project.triport.util.S3Util;
import com.project.triport.util.VideoUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PostLikeRepository postLikeRepository;
    private final S3Util s3Util;
    private final APIUtil apiUtil;
    private final VideoUtil videoUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ResponseDto readPostsAll(int page, String filter, String keyword) {
        // paging, sort 정리(page uri, filter(sortBy) uri, size 고정값(6), sort 고정값(DESC))
        Sort sort = Sort.by(Sort.Direction.DESC, filter);
        Pageable pageable = PageRequest.of(page - 1, 6, sort);
        // 전체 post 리스트 조회
        Page<Post> postPage;
        if ("".equals(keyword)) {
            postPage = postRepository.findAllBy(pageable);
        } else {
            List<PostHashtag> postHashtagList = postHashtagRepository.findByHashtagContaining(keyword);
            Set<Post> postSet = new HashSet<>();
            for(PostHashtag postHashtag : postHashtagList){
                postSet.add(postHashtag.getPost());
            }
            List<Post> postList = new ArrayList<>(postSet);
            final int start = (int)pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), postList.size());
            postPage= new PageImpl<>(postList.subList(start, end), pageable, postList.size());
        }
        // 반환 page가 last page인지 확인
        Boolean isLast = postPage.isLast();
        // post와 member 정보를 통해 DetailResponseDto에 필요한 정보를 기입(생성자 사용)
        Member member = getAuthMember();
        // 필요한 post 정보를 담은 DetailResponseDto 를 담기 위한 리스트 생성
        List<DetailResponseDto> responseDtoList = makeResponseDtoList(postPage, false, member);

        return new ResponseDto(true, responseDtoList, "전체 post 조회 완료", isLast);
    }

    public ResponseDto readPost(Long postId) {
        try {
            // DetailPost 불러온다.
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("post가 존재하지 않습니다.")
            );
            Member member = getAuthMember();
            // detailResponseDto 생성자에 위 세가지 항목을 넣어 results 양식에 맞는 객체를 작성한다.
            DetailResponseDto detailResponseDto = makeDetailResponseDto(post, false, member);
            return new ResponseDto(true, detailResponseDto, "post detail 불러오기 성공");
        } catch (IllegalArgumentException e) {
            return new ResponseDto(false, e.getMessage());
        }
    }

    public ResponseDto readPostsMember() {
        Member member = getAuthMember();
        List<Post> postList = postRepository.findByMember(member);
        List<DetailResponseDto> listResponseDtoList = makeResponseDtoList(postList,false, member);

        return new ResponseDto(true, listResponseDtoList, "member post 조회 완료");
    }

    public ResponseDto readPostsMemberLike() {
        Member member = getAuthMember();
        List<PostLike> postLikeList = postLikeRepository.findAllByMember(member);
        List<Post> postList = new ArrayList<>();
        for(PostLike postLike : postLikeList) { postList.add(postLike.getPost()); }
        List<DetailResponseDto> listResponseDtoList =  makeResponseDtoList(postList,true, member);
        return new ResponseDto(true, listResponseDtoList, "좋아요 post 조회 완료");
    }

    public ResponseDto createPost(PostRequestDto requestDto) throws IOException {
        MultipartFile videoFile = requestDto.getFile();
        String filepath = videoUtil.storeVideo(videoFile);
        String videoType = new VideoNameDto(filepath).getType();
        try {
            VideoProbeResult probeResult = videoUtil.probe(filepath);
            if (probeResult.getDuration() > 10.99) {
                return new ResponseDto(false, "10초 이내의 영상만 업로드 가능합니다.");
            }
            String videoUrl = s3Util.upload(filepath);

            Member member = getAuthMember();
            Post post = new Post(videoType, videoUrl, probeResult.getPosPlay(), member);
            postRepository.save(post);

            List<PostHashtag> hashtagList = convertHashtag(post, requestDto.getHashtag());
            saveHashtagList(hashtagList);
            post.addHashtagAll(hashtagList);

            apiUtil.encodingFile(post);
            return new ResponseDto(true, "포스팅 완료!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseDto(false, "포스팅에 실패하였습니다. 운영자에게 문의해주세요.");
        } finally{
            videoUtil.deleteTmp(filepath);
        }
    }

    @Transactional
    public ResponseDto updatePost(Long postId, PostRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 post가 존재하지 않습니다.")
        );
        postHashtagRepository.deleteAllByPost(post);

        List<PostHashtag> hashtagList = convertHashtag(post, requestDto.getHashtag());
        saveHashtagList(hashtagList);
        post.update(hashtagList);

        return new ResponseDto(true, "포스트 수정 완료!");
    }

    public ResponseDto deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 post 입니다.")
        );
        String videoUrl = post.getVideoUrl();
        s3Util.deleteFolder(new VideoNameDto(videoUrl).getFilename());
        postRepository.deleteById(postId);
        return new ResponseDto(true, "포스트를 삭제 하였습니다.");
    }

    @Transactional public void updateUrl(VideoUrlDto requestDto)
    {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("해당 post가 존재하지 않습니다.")
        );
        post.updateUrl(requestDto);
    }

    public List<DetailResponseDto> makeResponseDtoList(Iterable<Post> postIterable, Boolean isLike, Member member) {
        List<DetailResponseDto> listResponseDtoList = new ArrayList<>();
        for (Post post : postIterable) {
            DetailResponseDto detailResponseDto = makeDetailResponseDto(post, isLike, member);
            listResponseDtoList.add(detailResponseDto);
        }
        return listResponseDtoList;
    }

    public DetailResponseDto makeDetailResponseDto(Post post, Boolean isLike, Member member){
        boolean isMembers = false;
        if(member != null) {
            if (!isLike) { // 좋아요 명단을 제외한 나머지 List
                isLike = postLikeRepository.existsByPostAndMember(post, member);
            }
            isMembers = post.getMember().getId().equals(member.getId());
        }
        return new DetailResponseDto(post, isLike, isMembers);
    }

    public Member getAuthMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getMember();
    }

    public List<PostHashtag> convertHashtag(Post post, List<String> hashtagStringList){
        List<PostHashtag> hashtagList = new ArrayList<>();
        for(String hashtag : hashtagStringList){
            hashtagList.add(new PostHashtag(post, hashtag));
        }
        return hashtagList;
    }

    public void saveHashtagList(List<PostHashtag> hashtagList){
        for(PostHashtag hashtag : hashtagList){
            postHashtagRepository.save(hashtag);
        }
    }
}
