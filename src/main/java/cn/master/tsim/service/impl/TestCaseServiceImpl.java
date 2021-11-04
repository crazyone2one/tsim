package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.service.TestCaseStepsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements TestCaseService {

    private final ProjectService projectService;
    private final ModuleService moduleService;
    private final TestCaseStepsService stepsService;


    @Autowired
    public TestCaseServiceImpl(ProjectService projectService, ModuleService moduleService, TestCaseStepsService stepsService) {
        this.projectService = projectService;
        this.moduleService = moduleService;
        this.stepsService = stepsService;
    }

    @Override
    public TestCase saveCase(HttpServletRequest request, TestCase testCase) {
        final String projectId = request.getParameter("projectId");
        final String moduleId = request.getParameter("moduleId");
        final int priority = Integer.parseInt(request.getParameter("priority"));
        final Project project = projectService.addProject(projectId, request);
        final Module module = moduleService.addModule(projectId, moduleId, request);
        TestCase build = TestCase.builder().active(0).projectId(project.getId()).moduleId(module.getId())
                .name(request.getParameter("name"))
                .description(request.getParameter("description"))
                .precondition(request.getParameter("precondition"))
                .testMode(0)
                .priority(priority)
                .stepStore(request.getParameter("stepStore"))
                .resultStore(request.getParameter("resultStore"))
                .createDate(new Date()).build();
        baseMapper.insert(build);
        stepsService.saveStep(request, build);
        return build;
    }

    @Override
    public List<TestCase> listTestCase(TestCase testCase, String projectId, String moduleId) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        if (Objects.nonNull(testCase)) {
            extractedSearchWrapper(testCase, wrapper, tempProjectId, tempModuleId);
        } else {
            wrapper.lambda().eq(StringUtils.isNotBlank(projectId), TestCase::getProjectId, projectId);
            wrapper.lambda().eq(StringUtils.isNotBlank(moduleId), TestCase::getModuleId, moduleId);
        }
        return baseMapper.selectList(wrapper);
    }

    private void extractedSearchWrapper(TestCase testCase, QueryWrapper<TestCase> wrapper, List<String> tempProjectId, List<String> tempModuleId) {
        //            项目名称模糊查询
        if (StringUtils.isNotBlank(testCase.getProjectId())) {
            final List<Project> projects = projectService.findByPartialProjectName(testCase.getProjectId());
            projects.forEach(temp -> tempProjectId.add(temp.getId()));
            wrapper.lambda().in(TestCase::getProjectId, tempProjectId);
        }
//            模块模糊查询
        if (StringUtils.isNotBlank(testCase.getModuleId())) {
            moduleService.findByPartialModuleName(testCase.getModuleId()).forEach(temp -> tempModuleId.add(temp.getId()));
            wrapper.lambda().in(TestCase::getModuleId, tempModuleId);
        }
//            测试用例标题模糊查询
        wrapper.lambda().like(StringUtils.isNotBlank(testCase.getName()), TestCase::getName, testCase.getName());
//            优先级
        if (Objects.nonNull(testCase.getPriority())) {
            wrapper.lambda().eq(TestCase::getPriority, testCase.getPriority());
        }
//            是否删除
        wrapper.lambda().eq(Objects.nonNull(testCase.getActive()), TestCase::getActive, testCase.getActive());
    }

    @Override
    public void updateCase(String caseId) {
        final TestCase aCase = getById(caseId);
        aCase.setActive(Objects.equals(aCase.getActive(), 0) ? 1 : 0);
        aCase.setUpdateDate(new Date());
        baseMapper.updateById(aCase);
    }

    @Override
    public IPage<TestCase> pageList(TestCase testCase, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        if (Objects.nonNull(testCase)) {
            extractedSearchWrapper(testCase, wrapper, tempProjectId, tempModuleId);
        }
        final Page<TestCase> page = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        page.getRecords().forEach(t -> {
            t.setProject(projectService.getProjectById(t.getProjectId()));
            t.setModule(moduleService.getModuleById(t.getModuleId()));
        });
        return page;
    }

    @Override
    public IPage<TestCase> pageByProject(String projectId, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestCase::getProjectId, projectService.getProjectById(projectId).getId());
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }

    @Override
    public Map<String, Integer> caseCountByStatus(String projectId, String moduleId) {
        Map<String, Integer> map = new LinkedHashMap<>();
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(projectId), TestCase::getProjectId, projectId);
        wrapper.lambda().eq(StringUtils.isNotBlank(moduleId), TestCase::getModuleId, moduleId);
        map.put("total", baseMapper.selectCount(wrapper));
        // TODO: 2021/9/30 0030 已执行状态的测试用例
        return map;
    }

    @Override
    public Map<String, Map<String, Integer>> caseCountByStatus() {
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        baseMapper.selectList(new QueryWrapper<TestCase>().lambda().select(TestCase::getProjectId)).forEach(t -> {
            result.put(t.getProjectId(), caseCountByStatus(t.getProjectId(), null));
        });
        return result;
    }

    @Override
    public TestCase getById(String caseId) {
        final TestCase testCase = baseMapper.selectById(caseId);
        testCase.setProject(projectService.getProjectById(testCase.getProjectId()));
        return testCase;
    }

    @Override
    public TestCase queryCaseById(String caseId) {
        final TestCase testCase = baseMapper.queryCaseInfo(caseId);
        testCase.setProjectId(projectService.getProjectById(testCase.getProjectId()).getProjectName());
        testCase.setModuleId(moduleService.getModuleById(testCase.getModuleId()).getModuleName());
        return testCase;
    }

}
