package fer.progi.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "eduhub-materials";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

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

}
