package com.project.triport.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.project.triport.responseDto.ResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@NoArgsConstructor
public class S3Util {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.cloudfront.domain}")
    public String cloudFrontDomainName;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public ResponseDto upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucket,fileName, file.getInputStream(),null )
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String videoUrl = "https://" + cloudFrontDomainName + "/" + fileName;

        return new ResponseDto(true,videoUrl,"영상 저장 성공!");
    }

    public void uploadFolder(String filepath){
        TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        File file = new File(filepath);
        try {
//            MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucket,"test3", file, false);
            xfer_mgr.uploadDirectory(bucket,"test3", file, false);
//            // loop with Transfer.isDone()
//            XferMgrProgress.showTransferProgress(xfer);
//            // or block with Transfer.waitForCompletion()
//            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
//            xfer_mgr.shutdownNow();
    }

}
