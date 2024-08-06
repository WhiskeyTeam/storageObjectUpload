package dev.doka.storage.api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TestController {

    private final FileUploadAPI api = new FileUploadAPI();
    private static final String UPLOAD_DIR = "/images"; // 업로드할 디렉토리

    @PostMapping("/upload")
    public Map<String, String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        // 파일을 로컬에 저장
        String originalFilename = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + originalFilename);
        Files.createDirectories(path.getParent()); // 디렉토리 생성
        try {
            // 파일을 로컬에 저장할 디렉토리와 파일 경로 설정
            path = Paths.get(UPLOAD_DIR + '/' + file.getOriginalFilename());

            // 디렉토리 생성
            Files.createDirectories(path.getParent());

            // 파일 내용 쓰기
            Files.write(path, file.getBytes());

            api.putObject(FileUploadAPI.bucketName, originalFilename, path.toString());

        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 성공 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }
}
