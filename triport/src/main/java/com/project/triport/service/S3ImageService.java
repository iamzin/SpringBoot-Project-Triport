package com.project.triport.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.repository.BoardRepository;
import com.project.triport.requestDto.ImageRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor //원래는 NoArgsContructor
public class S3ImageService {

    private AmazonS3 s3Client;
    private final BoardImageInfoRepository boardImageInfoRepository;
    private final BoardRepository boardRepository;

    // @Value 로 application.yml에 작성한  cloud.aws.credentials의 값들을 가져옴
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 클라우드 프론트 도메인 주소
    public static final String CLOUD_FRONT_DOMAIN_NAME = "d1lq37d4y8t66p.cloudfront.net";

    // DI가 이뤄진 후 초기화를 수행하는 매서드, AmazonS3ClientBuilder를 통해 S3 Client를 가져올때 '자격증명'이 필요함
    // '자격증명' = "AccessKey' + 'SecretKey'
    // 의존성 주입 시점에는 @Value 값이 설정되지 않으므로 @PostConstruct 사용
    @PostConstruct
    public void setS3Client() {
        // accessKey와 secretKey를 이용하여 자격증명 객체를 얻는다.
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    // 신규 게시글 작성 중 이미지 파일 추가 (수정을 따로 생각할 필요가 없음)
    public ResponseDto uploadImageToNewBoard(ImageRequestDto requestDto) throws IOException {
        // 고유한 key 값을 갖기 위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");

        if(!restrictImgExtension(Objects.requireNonNull(requestDto.getImageFile().getOriginalFilename()))) {
            throw new IOException("jpg, png 확장자 파일만 업로드가 가능합니다.");
        }

        //fileName 변수는 S3 객체를 식별하는 key 값이고 이를 DB에 저장하는 것
        String fileName = requestDto.getImageFile().getOriginalFilename() + "-" + date.format(new Date());


        if (!limitImgSize(requestDto.getImageFile())) {
            throw new IOException("파일 용량 초과!!!");
        }

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        System.out.println("member.getId() = " + member.getId()); // -> null 이 뜬다. 즉 member를 못가져온다.

        // 파일 업로드 (파일 수정도 똑같이 putObject() 활용)
        s3Client.putObject(new PutObjectRequest(bucket, fileName, requestDto.getImageFile().getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String filePath = s3Client.getUrl(bucket,fileName).toString().split("https://triport-image.s3.ap-northeast-2.amazonaws.com/")[1];

        // ImageInfo 테이블에 이미지 정보 저장
        BoardImageInfo boardImageInfo = new BoardImageInfo(member, filePath);
        boardImageInfoRepository.save(boardImageInfo);

        ImageResponseDto imageResponseDto = new ImageResponseDto(filePath);

        return new ResponseDto(true, imageResponseDto, "이미지 업로드가 완료되었습니다.");
    }

    // 기존 게시물 수정 중 이미지 파일 추가
    public ResponseDto uploadImageToUpdatingBoard(Long boardId, MultipartFile imageFile) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Member member = getAuthMember();

        // 고유한 key 값을 갖기 위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");

        if(!restrictImgExtension(Objects.requireNonNull(imageFile.getOriginalFilename()))) {
            throw new IOException("jpg, png 확장자 파일만 업로드가 가능합니다.");
        }

        //fileName 변수는 S3 객체를 식별하는 key 값이고 이를 DB에 저장하는 것
        String fileName = imageFile.getOriginalFilename() + "-" + date.format(new Date());

        if (!limitImgSize(imageFile)) {
            throw new IOException("파일 용량 초과!!!");
        }

        // 파일 업로드 (파일 수정도 똑같이 putObject() 활용)
        s3Client.putObject(new PutObjectRequest(bucket, fileName, imageFile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String filePath = s3Client.getUrl(bucket,fileName).toString().split("https://triport-image.s3.ap-northeast-2.amazonaws.com/")[1];

        // ImageInfo 테이블에 이미지 정보 저장
        BoardImageInfo boardImageInfo = new BoardImageInfo(member, filePath, board);
        boardImageInfoRepository.save(boardImageInfo);

        ImageResponseDto imageResponseDto = new ImageResponseDto(filePath);

        return new ResponseDto(true, imageResponseDto, "이미지 업로드가 완료되었습니다.");
    }

    // 이미지 삭제
    @Async
    public CompletableFuture<String> deleteImg(String currentFilePath) throws IOException {
        if (!"".equals(currentFilePath) && currentFilePath != null) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, currentFilePath);

            if (isExistObject) {
                s3Client.deleteObject(bucket, currentFilePath);
//                System.out.println("이미지 파일 삭제 완료");
                return CompletableFuture.completedFuture("이미지 파일 삭제 완료");
            } else {
//                System.out.println("해당 이미지 파일이 존재하지 않습니다.");
                return CompletableFuture.completedFuture("해당 이미지 파일이 존재하지 않습니다.");
            }

        } else {
//            System.out.println("이미지 파일 주소가 올바르지 않습니다.");
            return CompletableFuture.completedFuture("해당 이미지 파일이 존재하지 않습니다.");
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

    public Boolean limitImgSize(MultipartFile file) {
        return file.getSize() <= 100000; // 100000 바이트 보다 작으면 true 반환
    }

    public String deleteSpaceFromFileName(String fileName) {
        return fileName.replace(" ", "+");
    }

    public Boolean restrictImgExtension(String fileName) {
        String extensionPart = fileName.substring(fileName.length() - 4).toLowerCase(Locale.ROOT);
        System.out.println("extensionPart = " + extensionPart);
        return extensionPart.equals(".png") || extensionPart.equals(".jpg");
    }

}
