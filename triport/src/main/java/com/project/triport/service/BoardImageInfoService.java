package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.responseDto.results.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class BoardImageInfoService {

    private final BoardImageInfoRepository boardImageInfoRepository;
    private final S3ImageService s3ImageService;

    // 게시글 신규 작성 때 사용하지 않는 이미지 파일 삭제
    // imageUrlList에 있는 이미지 파일은 제외하고 나머지 이미지 파일은 삭제
    @Transactional
    public void CompareAndDeleteImageForCreate(List<ImageResponseDto> imageUrlList) throws IOException {
        Member member = getAuthMember();
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByMemberAndBoardIsNull(member);

        // Repository에서 memberId와 ImageUrl로 찾은 칼럼들의 shouldBeDeleted 값을 false로 바꾼다.
        CompareAndChangeShouldBeDelete(imageUrlList, boardImageInfoList);

        // Repository에서 memberId가 현재 사용자의 Id이고 BoardId가 null인 칼럼들 가운데 shouldBeDeleted 값이 true인 칼럼을 삭제하고, S3에서도 삭제한다.
        List<BoardImageInfo> shouldBeDeletedBoardImageInfoList = boardImageInfoRepository.findDeletingImageInfoFromCreate(member);

        for (BoardImageInfo boardImageInfo : shouldBeDeletedBoardImageInfoList) {
            s3ImageService.deleteImg(boardImageInfo.getFilePath()); // S3에서 삭제
        }
        boardImageInfoRepository.bulkDeleteImageInfoFromCreate(member); // 테이블에서 삭제
    }


    // 게시글 수정 중일때 사용하지 않는 이미지 파일 삭제
    @Transactional
    public void CompareAndDeleteImageForUpdate(List<ImageResponseDto> imageUrlList, Board board) throws IOException {
        Member member = getAuthMember();
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByMemberAndBoard(member, board);

        // Repository에서 memberId와 ImageUrl로 찾은 칼럼들의 shouldBeDeleted 값을 false로 바꾼다.
        CompareAndChangeShouldBeDelete(imageUrlList, boardImageInfoList);

        // Repository에서 memberId가 현재 사용자의 Id이고 BoardId가 수정중인 게시글 Id인 칼럼들 가운데 shouldBeDeleted 값이 true인 칼럼을 삭제하고, S3에서도 삭제한다.
        List<BoardImageInfo> shouldBeDeletedBoardImageInfoList = boardImageInfoRepository.findDeletingImageInfoFromUpdate(member, board);

        for (BoardImageInfo boardImageInfo : shouldBeDeletedBoardImageInfoList) {
            s3ImageService.deleteImg(boardImageInfo.getFilePath()); // S3에서 삭제
        }
        boardImageInfoRepository.bulkDeleteImageInfoFromUpdate(member, board); // 테이블에서 삭제

    }

    // 게시글 삭제할 때 게시글의 이미지 파일 삭제
    public void deleteImageFromS3(Board board) throws IOException {
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByBoard(board);
        for (BoardImageInfo boardImageInfo : boardImageInfoList) {
            s3ImageService.deleteImg(boardImageInfo.getFilePath());
        }
    }

    public void CompareAndChangeShouldBeDelete(List<ImageResponseDto> imageUrlList, List<BoardImageInfo> boardImageInfoList) {
        for (BoardImageInfo boardImageInfo : boardImageInfoList) {
            for (ImageResponseDto imageResponseDto : imageUrlList) {
                String filename = imageResponseDto.getImageFilePath().split("https://" + S3ImageService.CLOUD_FRONT_DOMAIN_NAME + "/image/")[1];
                if (boardImageInfo.getFilePath().equals(filename)) {
                    boardImageInfo.updateShouldBeDelete(false); // shouldBeDeleted 값을 false로 바꿈
                    break;
                }
            }
        }
    }

    public Member getAuthMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getMember();
    }
}
