package com.proyekakhir.manajemendokumen.services;

import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.StorageDto;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class StorageService {

    @Value("${storage.bucket}")
    private String storageBucket;

    private MinioClient minioClient;

    public StorageService(Environment env){
        minioClient = MinioClient.builder()
                .endpoint(env.getProperty("storage.url"))
                .credentials(env.getProperty("storage.user"),env.getProperty("storage.password"))
                .build();
    }

    public ByteArrayResource download(String filename){
        try {
            GetObjectArgs builder = GetObjectArgs.builder()
                    .bucket(this.storageBucket)
                    .object(filename)
                    .build();
            byte[] out = minioClient.getObject(builder).readAllBytes();
            ByteArrayResource resource = new ByteArrayResource(out);

            return resource;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            Logger.getLogger(getClass().getName()).info(e.getMessage());
        }
        return null;
    }

    public boolean remove(String minioPath){
        try {
            RemoveObjectArgs builder = RemoveObjectArgs.builder()
                    .bucket(this.storageBucket)
                    .object(minioPath)
                    .build();
            minioClient.removeObject(builder);

            return true;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            Logger.getLogger(getClass().getName()).info(e.getMessage());
        }
        return false;
    }

    public StorageDto uploadByIdentifier(byte[] file, String folder, String identifier) {
        StorageDto minioDto = new StorageDto();
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(this.storageBucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(this.storageBucket).build());
            }

            String filePath = folder + "/" + identifier;

            ByteArrayInputStream bais = new ByteArrayInputStream(file);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.storageBucket)
                            .object(filePath).stream(
                                    bais, bais.available(), -1)
                            .build());
            bais.close();
            minioDto.setFilePath(filePath);
            minioDto.setIdentifier(identifier);
            minioDto.setUrl(minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(this.storageBucket)
                            .object(filePath)
                            .build()));
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            Logger.getLogger(getClass().getName()).info(e.getMessage());
        }
        return minioDto;
    }

    public StorageDto upload(byte[] file, String folder, String ext) {
        UUID identifier = UUID.randomUUID();
        return uploadByIdentifier(file, folder, identifier+(!ObjectUtils.isEmpty(ext)?ext:""));
    }

    public String getDownloadUrl(String filePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return this.minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(this.storageBucket)
                        .object(filePath)
                        .expiry(3600)
                        .build());
    }


}
