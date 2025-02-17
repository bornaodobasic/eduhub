package fer.progi.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "eduhub-materials";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String path, MultipartFile file) throws IOException {
        String key = path + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path tempFile = Files.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                tempFile
        );

        Files.delete(tempFile);

        return "File uploaded successfully. Key: " + key;
    }

    public void deleteFile(String key) {
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key).build());
    }

    public List<String> listFiles(String prefix) {
        return s3Client.listObjectsV2(builder -> builder.bucket(bucketName).prefix(prefix).build())
                .contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
    
    public byte[] getFile(String key) {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName) 
                        .key(key)           
                        .build(),
                ResponseTransformer.toBytes()
        ).asByteArray();
    }
    
    public String findFileBySuffix(String suffix) {
       
        List<S3Object> allObjects = s3Client.listObjectsV2(builder -> builder.bucket(bucketName).build())
                .contents();

        return allObjects.stream()
                .map(S3Object::key)
                .filter(key -> key.endsWith(suffix))
                .findFirst()
                .orElse(null); 
    }
    
    public String extractPrefix(String key) {
       
        int slashIndex = key.indexOf('/');
        if (slashIndex != -1) {
            return key.substring(0, slashIndex);
        }
        return null; 
    }
    
    public void uploadFileFromPath(String path, Path filePath) throws IOException {
        String key = path;

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                filePath
        );
    }





}
