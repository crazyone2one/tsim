package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.*;
import cn.master.tsim.mapper.ProjectMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.FreemarkerUtils;
import cn.master.tsim.util.ResponseUtils;
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
import javax.servlet.http.HttpServletResponse;
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
    private TestTaskInfoService taskInfoService;
    @Autowired
    SystemService systemService;

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
        // FIXME: 2021/10/22 0022 TooManyResultsException
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(projectName), "project_name", projectName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Project checkProject(String projectName, String workDate) {
        return baseMapper.queryProjectByName(projectName, workDate);
    }

    @Override
    public Project addProject(HttpServletRequest request, Map<String, String> proInfoMap) {
        final String name = proInfoMap.get("name");
        final String date = proInfoMap.get("date");
        /*1. 根据名称是否可查询到相关的项目*/
        final Project project = checkProject(name, date);
        if (Objects.nonNull(project)) {
            if (project.getProjectTasks().stream().anyMatch(t -> t.getIssueDate().equals(date))) {
                return project;
            }
            final TestTaskInfo taskInfo = taskInfoService.addItem(project, request, date);
            final List<TestTaskInfo> projectTasks = project.getProjectTasks();
            projectTasks.add(taskInfo);
            project.setProjectTasks(projectTasks);
            return project;
        }
        /*未查询到对应的项目数据,新创建*/
        final Project build = Project.builder().projectName(name)
                .projectCode(UuidUtils.generate())
                .createData(new Date())
                .delFlag(0)
                .build();
        baseMapper.insert(build);
        taskInfoService.addItem(build, request, date);
        systemService.refreshProjectName();
        return build;
    }

    @Override
    public Project addProject(Project project, HttpServletRequest request) {
        final Project projectByName = checkProject(project.getProjectName(), project.getWorkDate());
        if (Objects.nonNull(projectByName)) {
            for (TestTaskInfo projectTask : projectByName.getProjectTasks()) {
                if (Objects.equals(projectTask.getIssueDate(), project.getWorkDate())) {
                    return projectByName;
                }
            }
            final TestTaskInfo taskInfo = taskInfoService.addItem(projectByName, request, project.getWorkDate());
            final List<TestTaskInfo> projectTasks = projectByName.getProjectTasks();
            projectTasks.add(taskInfo);
            projectByName.setProjectTasks(projectTasks);
            return projectByName;
        }
//        未查询到相关的项目数据，新添加
        final Project build = Project.builder().projectName(project.getProjectName())
                .projectCode(UuidUtils.generate())
                .createData(new Date())
                .delFlag(0)
                .build();
        baseMapper.insert(build);
        taskInfoService.addItem(build, request, project.getWorkDate());
        systemService.refreshProjectName();
        return build;
    }

    @Override
    public void updateProjectStatus(String projectId) {
        final Project project = getProjectById(projectId);
        project.setDelFlag(Objects.equals(project.getDelFlag(), 0) ? 1 : 0);
        project.setUpdateDate(new Date());
        baseMapper.updateById(project);
    }

    @Override
    public IPage<Project> projectListPages(Project project, Integer pageCurrent, Integer pageSize) {
        String proName = StringUtils.isNotBlank(project.getProjectName()) ? project.getProjectName() : "";
        String workDate = StringUtils.isNotBlank(project.getWorkDate()) ? project.getWorkDate() : "";
        final IPage<Project> iPage = baseMapper.queryList(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                proName, workDate);
        iPage.getRecords().forEach(temp -> {
            //            统计关联的模块数量
            final List<Module> modules = moduleService.listModule(temp.getId());
            temp.setRefModuleCount(CollectionUtils.isNotEmpty(modules) ? modules.size() : 0);
            //            统计关联的测试用例数量
            final List<TestCase> cases = caseService.listTestCase(null, temp.getId(), "");
            temp.setRefCaseCount(CollectionUtils.isNotEmpty(cases) ? cases.size() : 0);
            //            关联bug
            final List<TestBug> testBugs = bugService.listBugByProjectId(temp.getId());
            temp.setRefBugCount(CollectionUtils.isNotEmpty(testBugs) ? testBugs.size() : 0);
        });
        return iPage;
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
    public ResponseResult generateReport(HttpServletRequest request, HttpServletResponse response, String id, String workDate) {
        ResponseResult result = ResponseUtils.success("报告生成成功");
        try {
            final String projectName = getProjectById(id).getProjectName();
            request.setAttribute("projectName", projectName);
            List<Map<String, Object>> list = new LinkedList<>();
            for (int i = 1; i < 5; i++) {
                Map<String, Object> testContent = new LinkedHashMap<>();
                testContent.put("index", i);
                testContent.put("modal", "xxx");
                testContent.put("result", "通过");
                testContent.put("bugCount", i);
                testContent.put("tester", "xxxx");
                testContent.put("date", "2021/11");
                list.add(testContent);
            }
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("projectName", projectName);
            data.put("username", "xxx");
            data.put("date", DateUtils.parse2String(new Date(),"yyyy-MM-dd"));
            data.put("pass_status", "通过");
            data.put("jf_status", "可以");
            data.put("testContent", list);
            FreemarkerUtils.generateWord(request, response, data, "project_report_template");
        } catch (Exception e) {
            result = ResponseUtils.error(400, "报告生成失败", e.getMessage());
        }
        return result;
    }

}
