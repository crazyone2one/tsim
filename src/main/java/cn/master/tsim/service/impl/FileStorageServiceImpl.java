package cn.master.tsim.service.impl;

import cn.master.tsim.common.UploadFileResponse;
import cn.master.tsim.config.FileProperties;
import cn.master.tsim.entity.DocInfo;
import cn.master.tsim.exception.FileStorageException;
import cn.master.tsim.exception.MentionedFileNotFoundException;
import cn.master.tsim.service.DocInfoService;
import cn.master.tsim.service.FileStorageService;
import cn.master.tsim.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author by 11's papa on 2022年02月07日
 * @version 1.0.0
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path fileStorageLocation;
    private final DocInfoService docInfoService;

    @Autowired
    public FileStorageServiceImpl(FileProperties fileProperties, DocInfoService docInfoService) {
        this.docInfoService = docInfoService;
        // 1:日期作为目录隔离文件 // 日期目录：20220207
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePath = dateFormat.format(new Date());
        // 2:最终文件的上传目录
        String realPath = "/uploadFiles/" + datePath;
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir() + realPath).normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException("文件夹创建失败", e);
        }
    }

    @Override
    public UploadFileResponse storeFile(HttpServletRequest request, MultipartFile file) {
        try {
            // 1:真实的文件名称
            String fileName = file.getOriginalFilename();
            // 2:截取后的文件名称
            assert fileName != null;
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            // 3:生成唯一的文件名称，随机生成如：xxx.jpg
            String newFileName = UuidUtils.generate() + suffix;
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String location = this.fileStorageLocation + "/" + newFileName;
            return new UploadFileResponse(fileName, suffix, file.getSize(), newFileName, location,
                    Objects.nonNull(request.getAttribute("docFlag")) ? request.getAttribute("docFlag").toString() : "undefine");
        } catch (IOException e) {
            throw new FileStorageException("文件上传失败", e);
        }
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Resource resource = new UrlResource(targetLocation.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MentionedFileNotFoundException("file not found");
            }
        } catch (MalformedURLException e) {
            throw new MentionedFileNotFoundException("file not found1", e);
        }
    }

    @Override
    public boolean deleteFile(String docId) {
        DocInfo docInfo = docInfoService.queryDocById(docId);
        docInfo.setDelFlag(1);
        docInfo.setUpdateDate(new Date());
        docInfoService.saveOrUpdate(docInfo);
        Path path = Paths.get(this.fileStorageLocation.resolve(docInfo.getUuidName()).toUri());
        boolean b = false;
        try {
            b = Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
