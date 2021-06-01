package com.project.triport.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;
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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
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
    public static final String CLOUD_FRONT_DOMAIN_NAME = "d1nogx3a73keco.cloudfront.net";

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

    // 신규 게시글 작성 중 이미지 파일 추가
    public ResponseDto uploadImageToNewBoard(ImageRequestDto requestDto) throws IOException {
        // 고유한 key 값을 갖기 위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");


        //fileName 변수는 S3 객체를 식별하는 key 값이고 이를 DB에 저장하는 것
        String fileName = "image/" + date.format(new Date()) + "-" + deleteSpaceFromFileName(Objects.requireNonNull(requestDto.getImageFile().getOriginalFilename()));

        if (!limitImgSize(requestDto.getImageFile())) {
            throw new IllegalArgumentException("파일 용량 초과!!!");
        }

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        try {
            BufferedImage resizeImg = createResizeImg(requestDto.getImageFile(), 500, 700);
            uploadImgToS3(resizeImg, fileName);
            // 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, requestDto.getImageFile().getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new IOException("이미지 파일 저장 실패!!!");
        }


        String filePath = s3Client.getUrl(bucket,fileName).toString().split("https://triportawsbucket.s3.ap-northeast-2.amazonaws.com/image/")[1];

        // ImageInfo 테이블에 이미지 정보 저장
        BoardImageInfo boardImageInfo = new BoardImageInfo(member, filePath);
        boardImageInfoRepository.save(boardImageInfo);

        ImageResponseDto imageResponseDto = new ImageResponseDto(filePath);

        return new ResponseDto(true, imageResponseDto, "이미지 업로드가 완료되었습니다.", 200);
    }

    // 기존 게시물 수정 중 이미지 파일 추가
    public ResponseDto uploadImageToUpdatingBoard(Long boardId, MultipartFile imageFile) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Member member = getAuthMember();

        // 고유한 key 값을 갖기 위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");


        //fileName 변수는 S3 객체를 식별하는 key 값이고 이를 DB에 저장하는 것
        String fileName = "image/" + date.format(new Date()) + "-" + deleteSpaceFromFileName(Objects.requireNonNull(imageFile.getOriginalFilename()));

        if (!limitImgSize(imageFile)) {
            throw new IllegalArgumentException("파일 용량 초과!!!");
        }

        try {
            // 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, imageFile.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new IOException("이미지 파일 저장 실패!!");
        }


        String filePath = s3Client.getUrl(bucket,fileName).toString().split("https://triportawsbucket.s3.ap-northeast-2.amazonaws.com/image/")[1];

        // ImageInfo 테이블에 이미지 정보 저장
        BoardImageInfo boardImageInfo = new BoardImageInfo(member, filePath, board);
        boardImageInfoRepository.save(boardImageInfo);

        ImageResponseDto imageResponseDto = new ImageResponseDto(filePath);

        return new ResponseDto(true, imageResponseDto, "이미지 업로드가 완료되었습니다.",200);
    }

    // 이미지 삭제
    @Async
    public void deleteImg(String currentFilePath) throws IOException {
        if (!"".equals(currentFilePath) && currentFilePath != null) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, "image/"+currentFilePath);

            if (isExistObject) {
                s3Client.deleteObject(bucket, "image/"+currentFilePath);
            } else {
                throw new IOException("해당 이미지 파일이 존재하지 않습니다.");
            }

        } else {
            throw new IOException("해당 이미지 파일이 존재하지 않습니다.");
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
        return file.getSize() <= 10000000; // 10MB 보다 작으면 true 반환
    }

    public String deleteSpaceFromFileName(String fileName) {
        return fileName.replace(" ", "_");
    }

    public BufferedImage createResizeImg(MultipartFile file, int width, int height) throws IOException {
        InputStream in = file.getInputStream();

        BufferedImage originalImage = ImageIO.read(in);

        MultiStepRescaleOp rescale = new MultiStepRescaleOp(width, height);

        rescale.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);

        BufferedImage thumbImage = rescale.filter(originalImage, null);

        in.close();

        return thumbImage;
    }

    public void uploadImgToS3(BufferedImage image, String fileName) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);

        byte[] bytes = os.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        s3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

//    public ArrayList<Integer> parsingWidthAndHeight(String sizingLine) {
//        String[] widthAndHeight = sizingLine.split("\\*");
//        if(widthAndHeight.length == 2) {
//
//        }
//    }

}
