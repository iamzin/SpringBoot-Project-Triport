package com.project.triport.util;

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
import lombok.NoArgsConstructor;
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

    @Value("${cloud.aws.cloudfront.domainSon}")
    private String cloudFrontDomainName;
    @Value("${cloud.aws.credentials.accessKeySon}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKeySon}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucketSon}")
    private String bucket;
    @Value("${cloud.aws.region.staticSon}")
    private String region;


    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(String filepath) throws IOException, AmazonServiceException, InterruptedException {
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        File file = new File(filepath);
        String randomString = UUID.randomUUID().toString();
        String filename = "video/" + randomString + "/" + randomString + ".mp4";
        try {
            transferManager.upload(bucket, filename, file).waitForCompletion();
        } finally{
            transferManager.shutdownNow();
        }
        String videoUrl = "https://" + cloudFrontDomainName + "/" + filename;
        return videoUrl;
    }

//    public String uploadFolder(String filepath) throws AmazonServiceException, InterruptedException {
//        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
//        File file = new File(filepath);
//        try {
//            transferManager.uploadDirectory(bucket, file.getName(), file, false).waitForCompletion();
//            String videoUrl = "https://" + cloudFrontDomainName + "/" + file.getName() + "/" + file.getName() + ".m3u8";
//            return videoUrl;
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//            System.exit(1);
//            ;
//            throw e;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    public void deleteFolder(String directory) {
        try {
            // 삭제할 Data를 Keys에 저장
            ArrayList<KeyVersion> keys = new ArrayList<KeyVersion>();
            for (int i = 0; i < 5; i++) {
                keys.add(new KeyVersion(directory + '/' + directory + i + "." + "ts"));
            }
            keys.add(new KeyVersion(directory + '/' + directory + ".m3u8"));
            keys.add(new KeyVersion(directory + '/' + directory + ".mp4"));

            // 삭제 요청할 양식을 만듬
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                    .withKeys(keys)
                    .withQuiet(false);

            // 요청 양식을 이용해 삭제 진행
            DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            int successfulDeletes = delObjRes.getDeletedObjects().size();
            for (KeyVersion key : keys) {
                System.out.println(key.getKey());
            }
            System.out.println("directory = " + directory);
            System.out.println(successfulDeletes + " objects successfully deleted.");
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
    }
}
