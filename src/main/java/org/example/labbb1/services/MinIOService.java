package org.example.labbb1.services;



import io.minio.*;
import io.minio.messages.Retention;
import io.minio.messages.RetentionMode;
import org.example.labbb1.exceptions.MinIOException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.ZonedDateTime;

@Service
public class MinIOService {
    private final MinioClient minioClient;
    private final String bucketName = "space";

    public MinIOService() {
         minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
    }

    public void uploadAndLockFile(InputStream inputStream, long fileSize, String fileName) throws MinIOException {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
                    .stream(inputStream, fileSize, -1)
                            .retention(new Retention(RetentionMode.COMPLIANCE, ZonedDateTime.now().plusDays(1)))
                    .build());
        } catch (Exception e){
            throw new MinIOException();
        }
    }

    public void unlockFile(String fileName) throws MinIOException{
        try {
            minioClient.setObjectRetention(
                    SetObjectRetentionArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .config(new Retention(RetentionMode.GOVERNANCE, ZonedDateTime.now().minusDays(1)))
                            .bypassGovernanceMode(true)
                            .build());
        } catch (Exception e) {
            throw new MinIOException();
        }
    }

    public InputStream downloadFile(String fileName) throws MinIOException {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e){
            throw new MinIOException();
        }
    }


    public void deleteFile(String fileName) throws MinIOException {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw new MinIOException();
        }
    }

}
