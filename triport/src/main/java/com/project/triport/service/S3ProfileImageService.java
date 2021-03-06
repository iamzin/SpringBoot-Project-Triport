package com.project.triport.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3ProfileImageService {

    private AmazonS3 s3Client;

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

    // 마이페이지 프로필 이미지 추가
    public String uploadProfileImage(MultipartFile profileImgFile) throws IOException {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "profileImage/" + date.format(new Date()) + "-" + deleteSpaceFromFileName(profileImgFile);

        if (!limitImgSize(profileImgFile)) {
            throw new IllegalArgumentException("파일 용량이 초과되었습니다. 10MB 이하로 업로드 해주세요.");
        }

        try {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, profileImgFile.getInputStream(), null)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IOException("파일 저장에 실패하였습니다.");
        }

        return s3Client.getUrl(bucket,fileName).toString().split("https://triportawsbucket.s3.ap-northeast-2.amazonaws.com/profileImage/")[1];
    }

    public Boolean limitImgSize(MultipartFile file) {
        return file.getSize() <= 10000000; // 10MB 보다 작으면 true 반환
    }

    public String deleteSpaceFromFileName(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "_");
    }

    // file을 S3에 upload 후 url 반환
    public String getFileUrl(MultipartFile file) throws IOException {
        String filePath = uploadProfileImage(file);
        return "https://" + S3ProfileImageService.CLOUD_FRONT_DOMAIN_NAME + "/profileImage/" + filePath;
    }
}
