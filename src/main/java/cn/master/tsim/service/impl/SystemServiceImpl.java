package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.config.FileProperties;
import cn.master.tsim.entity.Project;
import cn.master.tsim.mapper.TesterMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.SystemService;
import cn.master.tsim.util.ParameterNotNull;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
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
    private  ProjectService projectService;

    public SystemServiceImpl(FileProperties properties) {
        // 4:日期作为目录隔离文件 // 日期目录：20211125
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePath = dateFormat.format(new Date());
        // 5:最终文件的上传目录
        String realPath = "/files/"+ datePath;
        this.fileStorageLocation = Paths.get(properties.getUploadDir() + realPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("文件夹创建失败",e);
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
    public boolean validate(Object object) {
        boolean flag = true;
        try {
            final Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                final Object value = field.get(object);
                final ParameterNotNull annotation = field.getAnnotation(ParameterNotNull.class);
                // FIXME: 2021/11/1 0001 字段可为空的排除掉
                if (Objects.nonNull(annotation) && StringUtils.isBlank(String.valueOf(value))) {
                    flag = false;
                }
                if (Objects.nonNull(annotation) && Objects.isNull(value)) {
                    flag = false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return flag;
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
            return ResponseUtils.success("select a file");
        }
        try {
            // 1:真实的文件名称
            String fileName = file.getOriginalFilename();
            resultMap.put("fileName", fileName);
            // 2:截取后的文件名称
            assert fileName != null;
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            // 3:生成唯一的文件名称，随机生成如：xxx.jpg
            String newFileName = UUID.randomUUID() + suffix;
            resultMap.put("docName", fileName);
            resultMap.put("uuidName", newFileName);
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String location = this.fileStorageLocation + "/" + newFileName;
            System.out.println(location );
            resultMap.put("docPath", location);
            return ResponseUtils.success("文件上传成功",resultMap);
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
            File file = new File(downloadUri);
            // 重置response
            response.reset();
            // ContentType，即告诉客户端所发送的数据属于什么类型
//            response.setContentType("application/octet-stream; charset=UTF-8");
            // 获得文件的长度
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            // 发送给客户端的数据
            OutputStream outputStream = response.getOutputStream();
            byte[] buff = new byte[1024];
            BufferedInputStream bis;
            // 读取文件
            bis = new BufferedInputStream(new FileInputStream(downloadUri));
            int i = bis.read(buff);
            // 只要能读到，则一直读取
            while (i != -1) {
                // 将文件写出
                outputStream.write(buff, 0, buff.length);
                // 刷出
                outputStream.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
