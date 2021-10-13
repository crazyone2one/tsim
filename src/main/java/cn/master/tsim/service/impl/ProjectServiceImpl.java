package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.mapper.ProjectMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.UuidUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private TestCaseService caseService;
    @Autowired
    private TestBugService bugService;
    @Autowired
    private ProjectBugRefService projectBugRefService;
    @Autowired
    private TestTaskInfoService taskInfoService;

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
    public Project addProject(String projectName, HttpServletRequest request) {
        /*1. 根据名称是否可查询到相关的项目*/
        final Project project = getProjectByName(projectName);
        if (Objects.nonNull(project)) {
            return project;
        }
        /*未查询到对应的项目数据,新创建*/
        final Project build = Project.builder().projectName(projectName)
                .projectCode(UuidUtils.generate())
                .workDate(DateUtils.parse2String(new Date(), "yyyy-MM"))
                .createData(new Date())
                .delFlag("0")
                .build();
        baseMapper.insert(build);
        final String workDate = StringUtils.isNotBlank(project.getWorkDate()) ? project.getWorkDate() : DateUtils.parse2String(new Date(), "yyyy-MM");
        taskInfoService.addItem(build, request, workDate);
        return build;
    }

    @Override
    public Project addProject(Project project, HttpServletRequest request) {
        final Project projectByName = getProjectByName(project.getProjectName());
        if (Objects.nonNull(projectByName)) {
            return projectByName;
        }
        final Project build = Project.builder().projectName(project.getProjectName())
                .projectCode(UuidUtils.generate())
                .createData(new Date())
                .delFlag("0")
                .build();
        baseMapper.insert(build);
        final String workDate = StringUtils.isNotBlank(project.getWorkDate()) ? project.getWorkDate() : DateUtils.parse2String(new Date(), "yyyy-MM");
        taskInfoService.addItem(build, request, workDate);
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
        if (StringUtils.isNotBlank(project.getProjectName())) {
            final Project projectByName = getProjectByName(project.getProjectName());
            wrapper.lambda().in(StringUtils.isNotBlank(project.getWorkDate()), Project::getId, projectBugRefService.refList(projectByName.getId(), null, project.getWorkDate()));
        } else {
            List<String> tempRefId = new LinkedList<>();
            projectBugRefService.refList(null, null, project.getWorkDate()).forEach(r -> tempRefId.add(r.getProjectId()));
            wrapper.lambda().in(StringUtils.isNotBlank(project.getWorkDate()), Project::getId, tempRefId);
        }
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }

    @Override
    public Map<String, Project> projectMap() {
        Map<String, Project> result = new LinkedHashMap<>();
        baseMapper.selectList(new QueryWrapper<>()).forEach(temp -> {
            result.put(temp.getId(), temp);
        });
        return result;
    }

    @Override
    public Map<String, Map<String, Integer>> refMap() {
        Map<String, Map<String, Integer>> mapMap = new LinkedHashMap<>();
        findByPartialProjectName("").forEach(temp -> {
            Map<String, Integer> tempMap = new LinkedHashMap<>();
//            统计关联的模块数量
            final List<Module> modules = moduleService.listModule(temp.getId());
            tempMap.put("module", CollectionUtils.isNotEmpty(modules) ? modules.size() : 0);
//            统计关联的测试用例数量
            final List<TestCase> cases = caseService.listTestCase(null, temp.getId(), "");
            tempMap.put("case", CollectionUtils.isNotEmpty(cases) ? cases.size() : 0);

//            关联bug
            final List<TestBug> testBugs = bugService.listBugByProjectId(temp.getId());
            tempMap.put("bug", CollectionUtils.isNotEmpty(testBugs) ? testBugs.size() : 0);
            mapMap.put(temp.getId(), tempMap);
        });
        return mapMap;
    }
}
