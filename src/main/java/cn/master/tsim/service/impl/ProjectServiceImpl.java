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
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    DocInfoService docInfoService;

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
    @Deprecated
    public Project addProject(HttpServletRequest request, Project project) {
        final Project projectByName = getProjectByName(project.getProjectName());
        if (Objects.nonNull(projectByName)) {
            return projectByName;
        }
//        未查询到相关的项目数据，新添加
        final Project build = Project.builder().projectName(project.getProjectName().trim())
                .projectCode(UuidUtils.generate())
                .createData(new Date())
                .delFlag(0)
                .build();
        baseMapper.insert(build);
        systemService.refreshProjectName();
        return build;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Project saveProject(String projectName) {
        final Project build = Project.builder().projectName(projectName)
                .projectCode(UuidUtils.generate())
                .createData(new Date())
                .delFlag(0)
                .build();
        baseMapper.insert(build);
        systemService.refreshProjectName();
        return build;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int deleteProject(String projectId) {
        final Project project = getProjectById(projectId);
        project.setDelFlag(1);
        project.setUpdateDate(new Date());
        return baseMapper.updateById(project);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void updateProjectStatus(String projectId) {
        final Project project = getProjectById(projectId);
        project.setDelFlag(Objects.equals(project.getDelFlag(), 0) ? 1 : 0);
        project.setUpdateDate(new Date());
        baseMapper.updateById(project);
    }

    @Override
    public IPage<Project> projectListPages(HttpServletRequest request, String projectName, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        //               根据项目名称模糊查询
        wrapper.lambda().like(StringUtils.isNotBlank(projectName), Project::getProjectName, projectName);
//        wrapper.lambda().eq(Project::getDelFlag, 0);
        wrapper.lambda().orderByAsc(Project::getDelFlag);
        final IPage<Project> iPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize), wrapper);
        iPage.getRecords().forEach(temp -> {
            //            统计关联的模块数量
            final List<Module> modules = moduleService.listModule(temp.getId());
            temp.setRefModuleCount(CollectionUtils.isNotEmpty(modules) ? modules.size() : 0);
            //            统计关联的测试用例数量
            final List<TestCase> cases = caseService.listTestCase(request, null, temp.getId(), "");
            temp.setRefCaseCount(CollectionUtils.isNotEmpty(cases) ? cases.size() : 0);
            //            关联bug
            final List<TestBug> testBugs = bugService.listBugByProjectId(temp.getId());
            temp.setRefBugCount(CollectionUtils.isNotEmpty(testBugs) ? testBugs.size() : 0);
        });
        return iPage;
    }

    @Override
    public ResponseResult generateReport(HttpServletRequest request, HttpServletResponse response, String id, String workDate, String storyId) {
        ResponseResult result = ResponseUtils.success("报告生成成功");
        try {
            request.setAttribute("projectName", id);
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
            data.put("projectName", id);
            data.put("username", "xxx");
            data.put("date", DateUtils.parse2String(new Date(), "yyyy-MM-dd"));
            data.put("pass_status", "通过");
            data.put("jf_status", "可以");
            data.put("testContent", list);
//            保存文件系统到数据库
            final String reportName = FreemarkerUtils.generateWord(request, response, data, "project_report_template");
            Map<String, String> map = new LinkedHashMap<>();
            map.put("docName", reportName);
            map.put("uuidName", reportName);
            map.put("flag", "report");
            map.put("docPath", "");
            DocInfo saveDocInfo = docInfoService.saveDocInfo(request, map);
//            更新task表中测试报告字段值
            final TestTaskInfo taskInfo1 = taskInfoService.checkReportDoc(id, storyId, workDate);
            taskInfo1.setReportDoc(saveDocInfo.getId());
            taskInfoService.updateTask(request, taskInfo1);
        } catch (Exception e) {
            result = ResponseUtils.error(400, "报告生成失败", e.getMessage());
        }
        return result;
    }

    @Override
    public Project queryProject(HttpServletRequest request) {
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(name), Project::getProjectName, name)
                .eq(StringUtils.isNotBlank(id), Project::getId, id);
        return baseMapper.selectOne(wrapper);
    }
}
