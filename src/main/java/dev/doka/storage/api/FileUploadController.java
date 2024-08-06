package dev.doka.storage.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final FileUploadAPI fileUploadAPI;

    @Autowired
    public FileUploadController(FileUploadAPI fileUploadAPI) {
        this.fileUploadAPI = fileUploadAPI;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file){

        String bucketName = "whiskey-file";
        // 원본 파일 이름을 객체 이름으로 사용
        String objectName = file.getOriginalFilename();

        try {
            // 임시 파일 생성
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            // 파일 업로드
            fileUploadAPI.putObject(bucketName, objectName, tempFile.getName());

            // 임시 파일 삭제
            tempFile.delete();

            return "File uploaded successfully.";

        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed. " + e.getMessage();
        }
    }
}
