package cn.master.tsim.controller;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.common.UploadFileResponse;
import cn.master.tsim.service.FileStorageService;
import cn.master.tsim.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author by 11's papa on 2022年02月07日
 * @version 1.0.0
 */
@Slf4j
@RestController
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("uploadSingleFile")
    public ResponseResult uploadSingleFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        UploadFileResponse uploadFileResponse = fileStorageService.storeFile(request, file);
        if (Objects.nonNull(uploadFileResponse)) {
            return ResponseUtils.success(uploadFileResponse);
        } else {
            throw new RuntimeException("文件上传失败");
        }
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    //@ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .contentLength(resource.getFile().length())
                .body(resource);
    }

    @PostMapping("deleteFile/{docId}")
    public boolean deleteFile(@PathVariable String docId) {
       return fileStorageService.deleteFile(docId);
    }
}
