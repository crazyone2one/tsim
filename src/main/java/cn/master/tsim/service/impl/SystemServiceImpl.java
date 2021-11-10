package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.entity.Project;
import cn.master.tsim.mapper.TesterMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.SystemService;
import cn.master.tsim.util.ParameterNotNull;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Created by 11's papa on 2021/10/12
 * @version 1.0.0
 */
@Service
public class SystemServiceImpl implements SystemService {
    private final TesterMapper testerMapper;
    private final ProjectService projectService;

    @Autowired
    public SystemServiceImpl(TesterMapper testerMapper, ProjectService projectService) {
        this.testerMapper = testerMapper;
        this.projectService = projectService;
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
}
