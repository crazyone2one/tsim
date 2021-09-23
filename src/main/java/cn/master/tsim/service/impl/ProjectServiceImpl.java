package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Project;
import cn.master.tsim.mapper.ProjectMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.util.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    public List<Project> findByPartialProjectName(String searchString) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(searchString), "project_name", searchString);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Project getProjectById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Project getProjectByName(String projectName) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(projectName), "project_name", projectName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void addProject(Project project) {
        final Project project1 = getProjectByName(project.getProjectName());
        if (Objects.nonNull(project1)) {
            return;
        }
        project.setProjectCode(UuidUtils.generate());
        project.setCreateData(new Date());
        project.setDelFlag("0");
        baseMapper.insert(project);
    }

    @Override
    public Project addProject(String projectName) {
        final Project project = getProjectByName(projectName);
        if (Objects.nonNull(project)) {
            return project;
        }
        final Project build = Project.builder().projectName(projectName).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public void updateProjectStatus(String projectId) {
        final Project project = getProjectById(projectId);
        project.setDelFlag(Objects.equals(project.getDelFlag(), "0") ? "1" : "0");
        project.setUpdateDate(new Date());
        baseMapper.updateById(project);
    }

    @Override
    public IPage<Project> projectListPages(Project project, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        // 按照项目名称模糊查询
        wrapper.lambda().like(StringUtils.isNotBlank(project.getProjectName()), Project::getProjectName, project.getProjectName());
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize),
                wrapper);
    }
}
