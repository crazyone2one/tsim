package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
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
import java.io.File;
import java.lang.reflect.Field;
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

    @Override
    public ResponseResult uploadFile(HttpServletRequest request, MultipartFile file) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (Objects.isNull(file)) {
            return ResponseUtils.error();
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
            resultMap.put("newFileName", newFileName);
            // 4:日期作为目录隔离文件 // 日期目录：20211125
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String datePath = dateFormat.format(new Date());
            // 5:最终文件的上传目录
            String realPath = "upload/files/"+ datePath;
            // 生成的最终目录; upload/files/20211125
            File targetFile = new File(realPath);
            // 6：如果dirFile不存在，则创建
            if (!targetFile.isDirectory()) {
                if (!targetFile.mkdirs()) {
                    return ResponseUtils.error("文件夹创建失败");
                }
            }
            // 7: 指定文件上传后完整的文件名
            // 文件在服务器的最终路径是：/home/jingll/IdeaProjects/tsim/upload/files/20211125/xxx.jpg
            File localFile = new File(targetFile.getCanonicalPath() + "/" + newFileName);
            // 8：文件上传
            file.transferTo(localFile);
            return ResponseUtils.success("文件上传成功",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error();
        }
    }
}
