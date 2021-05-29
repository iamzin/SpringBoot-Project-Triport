package com.project.triport.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.project.triport.requestDto.VideoNameDto;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Component
@NoArgsConstructor
public class S3Util {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomainName;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(String filepath) throws AmazonServiceException {
        VideoNameDto videoNameDto = new VideoNameDto(filepath);

        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        File file = new File(filepath);
        String randomString = UUID.randomUUID().toString();
        String filename = "video/" + randomString + "/" + randomString + "." + videoNameDto.getType();
        try {
            transferManager.upload(bucket, filename, file).waitForCompletion();
            logger.info("File {} S3 Upload Success!", randomString + "." + videoNameDto.getType());
        } catch (AmazonClientException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        String videoUrl = "https://" + cloudFrontDomainName + "/" + filename;
        return videoUrl;
    }

    public void deleteFolder(String directory) {
        try {
            // 삭제할 Data를 Keys에 저장
            ArrayList<KeyVersion> keys = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                keys.add(new KeyVersion("video/" + directory + '/' + directory + i + "." + "ts"));
            }
            keys.add(new KeyVersion("video/" + directory + '/' + directory + ".m3u8"));
            keys.add(new KeyVersion("video/" + directory + '/' + directory + ".mp4"));
            keys.add(new KeyVersion("video/" + directory + '/' + directory + ".MOV"));
            // 삭제 요청할 양식을 만듬
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                    .withKeys(keys)
                    .withQuiet(false);
            // 요청 양식을 이용해 삭제 진행
            DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            int successfulDeletes = delObjRes.getDeletedObjects().size();
            logger.info("directory = " + directory);
            logger.info(successfulDeletes + " objects successfully deleted.");
        } catch (AmazonServiceException e) {
            logger.error(e.getMessage());
        }
    }
}
