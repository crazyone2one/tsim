package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.config.FileProperties;
import cn.master.tsim.entity.Project;
import cn.master.tsim.mapper.TesterMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.SystemService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Created by 11's papa on 2021/10/12
 * @version 1.0.0
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private TesterMapper testerMapper;
    @Autowired
    @Lazy
    private ProjectService projectService;

    public SystemServiceImpl(FileProperties properties) {
        // 4:日期作为目录隔离文件 // 日期目录：20211125
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePath = dateFormat.format(new Date());
        // 5:最终文件的上传目录
        String realPath = "/files/" + datePath;
        this.fileStorageLocation = Paths.get(properties.getUploadDir() + realPath).normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("文件夹创建失败", e);
        }
    }


    @Override
    @PostConstruct
    public void initUserMap() {
        testerMapper.selectList(new QueryWrapper<>()).forEach(u -> {
            Constants.userMaps.put(u.getId(), u);
        });
    }

    @Override
    public void refreshUserMap() {
        Constants.userMaps.clear();
        initUserMap();
    }

    @Override
    @PostConstruct
    public void initProjectName() {
        final List<Project> projects = projectService.findByPartialProjectName("");
        if (CollectionUtils.isNotEmpty(projects)) {
            List<String> projectNames = new LinkedList<>();
            projects.forEach(p -> projectNames.add(p.getProjectName()));
            Constants.projectNames = projectNames;
        }
    }

    @Override
    public void refreshProjectName() {
        Constants.projectNames.clear();
        initProjectName();
    }

    private final Path fileStorageLocation;

    @Override
    public ResponseResult storeFile(HttpServletRequest request, MultipartFile file) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (Objects.isNull(file)) {
            return ResponseUtils.error("select a file");
        }
        try {
            // 1:真实的文件名称
            String fileName = file.getOriginalFilename();
            // 2:截取后的文件名称
            assert fileName != null;
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            // 3:生成唯一的文件名称，随机生成如：xxx.jpg
            String newFileName = UUID.randomUUID() + suffix;
            resultMap.put("docName", fileName);
            resultMap.put("uuidName", newFileName);
            resultMap.put("flag", Objects.nonNull(request.getAttribute("docFlag")) ? request.getAttribute("docFlag").toString() : "undefine");
            request.removeAttribute("docFlag");
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String location = this.fileStorageLocation + "/" + newFileName;
            resultMap.put("docPath", location);
            return ResponseUtils.success("文件上传成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error("文件保存失败");
        }
    }

    @Override
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String uuidName) {
        try {
            String downloadUri = this.fileStorageLocation + "/" + uuidName;
            // 读文件
            File file = new File(uuidName);
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            }
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buff = new byte[fis.available()];
            int read = fis.read(buff);
            fis.close();
            // 重置response
            response.reset();
            response.setCharacterEncoding("UTF-8");
            // 获得文件的长度
            response.setHeader("Content-Length", "" + file.length());
            // 解决下载文件时文件名乱码问题
            byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;fileName=" + new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1));
            // 发送给客户端的数据
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buff);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件下载失败", e);
        }
    }
}
