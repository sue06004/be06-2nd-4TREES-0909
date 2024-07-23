package org.example.fourtreesproject.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    public List<String> upload(MultipartFile[] file);
}
