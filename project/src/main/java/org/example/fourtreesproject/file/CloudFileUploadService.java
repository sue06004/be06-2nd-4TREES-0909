package org.example.fourtreesproject.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudFileUploadService implements FileUploadService {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public String makeFolder() {
        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return folderPath;
    }

    @Override
    public List<String> upload(MultipartFile[] fileArray) {
        ObjectMetadata metadata = new ObjectMetadata();
        List<String> imgUrlList = new ArrayList<String>();
        MultipartFile multipartFile;

        for (int i = 0; i < fileArray.length; i++) {
            multipartFile = fileArray[i];
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            String uploadPath = makeFolder();
            try {
                String fileName = uploadPath + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
                // 2024/07/17/fhdksfhuefhehfkzsdhfszjdfhszhf_도지민통장사본
                amazonS3.putObject(bucketName, fileName, multipartFile.getInputStream(), metadata);
                imgUrlList.add(amazonS3.getUrl(bucketName, fileName).toString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgUrlList;
    }
}

