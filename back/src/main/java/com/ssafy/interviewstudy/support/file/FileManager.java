package com.ssafy.interviewstudy.support.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
@Component
public class FileManager {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // 버킷명

    // upload 메서드 | 단일 파일 업로드
    public void upload(File file, String key) {
        uploadToS3(new PutObjectRequest(this.bucket, key, file));
    }

    // upload 메서드 | MultipartFile을 사용할 경우
    public void upload(InputStream is, String key, String contentType, long contentLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(contentLength);

        uploadToS3(new PutObjectRequest(this.bucket, key, is, objectMetadata));
    }

    // PutObjectRequest는 Aws s3 버킷에 업로드할 객체 메타 데이터와 파일 데이터로 이루어져 있다.
    private void uploadToS3(PutObjectRequest putObjectRequest) {
        try {
            this.s3Client.putObject(putObjectRequest);
            System.out.println(String.format("[%s] upload complete", putObjectRequest.getKey()));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 복사 메서드
    public void copy(String orgkey, String copyKey) {
        try {
            // copy 객체 생성
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(this.bucket, orgkey, this.bucket, copyKey);

            // copy
            this.s3Client.copyObject(copyObjectRequest);

            System.out.println(String.format("Finish copying [%s] to [%s]", orgkey, copyKey));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    // 삭제 메서드
    public void delete(String key) {
        try {
            // Delete 객체 생성
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucket, key);

            // Delete
            this.s3Client.deleteObject(deleteObjectRequest);

            System.out.println(String.format("[%s] delete key", key));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public byte[] download(String key) throws IOException{
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] file = IOUtils.toByteArray(inputStream);

        return file;
    }
}
