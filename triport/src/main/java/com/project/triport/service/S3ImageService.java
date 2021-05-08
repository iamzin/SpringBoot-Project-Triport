package com.project.triport.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.requestDto.ImageRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.ImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor //원래는 NoArgsContructor
public class S3ImageService {

    private AmazonS3 s3Client;
    private final BoardImageInfoRepository boardImageInfoRepository;

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

    // 이미지 파일 추가 (수정을 따로 생각할 필요가 없음)
    public ResponseDto upload(ImageRequestDto requestDto) throws IOException {
        // 고유한 key 값을 갖기 위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");

        //fileName 변수는 S3 객체를 식별하는 key 값이고 이를 DB에 저장하는 것
        String fileName = deleteSpaceFromFileName(requestDto.getImageFile().getOriginalFilename()) + "-" + date.format(new Date());

        System.out.println(requestDto.getImageFile().getSize()); // 바이트 단위

        if (!limitImgSize(requestDto.getImageFile())) {
            throw new IOException("파일 용량 초과!!!");
        }

        // 파일 업로드 (파일 수정도 똑같이 putObject() 활용)
        s3Client.putObject(new PutObjectRequest(bucket, fileName, requestDto.getImageFile().getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // ImageInfo 테이블에 이미지 정보 저장
        BoardImageInfo boardImageInfo = new BoardImageInfo(requestDto.getTempId(), fileName);
        boardImageInfoRepository.save(boardImageInfo);

        ImageResponseDto imageResponseDto = new ImageResponseDto(fileName);

        return new ResponseDto(true, imageResponseDto, "이미지 업로드가 완료되었습니다.");
    }

    // 이미지 삭제 (예시)
    public String deleteImg(String currentFilePath) throws IOException {
        if ("".equals(currentFilePath) == false && currentFilePath != null) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, currentFilePath);

            if (isExistObject == true) {
                s3Client.deleteObject(bucket, currentFilePath);
                return "이미지 파일 삭제 완료";
            } else {
                return "해당 이미지 파일이 존재하지 않습니다.";
            }

        } else {
            return "해당 이미지 파일이 존재하지 않습니다.";
        }
    }

    public Boolean limitImgSize(MultipartFile file) {
        return file.getSize() <= 100000; // 100000 바이트 보다 작으면 true 반환
    }

    public String deleteSpaceFromFileName(String fileName) {
        return fileName.replace(" ", "+");
    }

}
