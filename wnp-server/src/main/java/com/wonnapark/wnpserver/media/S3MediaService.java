package com.wonnapark.wnpserver.media;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3MediaService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3MediaService(
            AmazonS3 amazonS3,
            @Value("${cloud.aws.s3.bucket}") String bucketName
    ) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String upload(String key, File file) {
        amazonS3.putObject(bucketName, key, file);
        return amazonS3.getUrl(bucketName, key).toExternalForm();
    }

    public void delete(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    public String findObjectUrlByName(String key) {
        return amazonS3.getUrl(bucketName, key).toString();
    }

}
