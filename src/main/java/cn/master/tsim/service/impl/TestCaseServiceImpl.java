package cn.master.tsim.service.impl;

import cn.master.tsim.entity.*;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.service.*;
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
    private PlanCaseRefService refService;
    @Autowired
    private TestPlanService planService;
    @Autowired
    TestTaskInfoService taskInfoService;
    @Autowired
    ProjectCaseRefService projectCaseRefService;


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
        final String storyId = request.getParameter("storyId");
        final int priority = Integer.parseInt(request.getParameter("priority"));
        final Project project = projectService.getProjectById(projectId);
        final Module module = moduleService.addModule(request,projectId,moduleId);
        final String stepStore = request.getParameter("stepStore");
        TestCase build = TestCase.builder().active(0).projectId(project.getId()).moduleId(module.getId())
                .name(request.getParameter("name"))
                .description(request.getParameter("description"))
                .precondition(request.getParameter("precondition"))
                .testMode(0)
                .priority(priority)
                .stepStore(stepStore)
                .resultStore(request.getParameter("resultStore"))
                .createDate(new Date()).build();
        baseMapper.insert(build);
        stepsService.saveStep(stepStore, build);
//       关联需求时 t_project_case_ref表增加一条记录
        if (StringUtils.isNotBlank(storyId)) {
            final int item = projectCaseRefService.addRefItem(projectId, storyId, build.getId(), "");
            final TestTaskInfo taskInfo = taskInfoService.queryItem(projectId, storyId);
            taskInfo.setCreateCaseCount(taskInfo.getCreateCaseCount() + item);
            taskInfoService.updateTask(request, taskInfo);
        }
        return build;
    }

    @Override
    public void importCase(HttpServletRequest request, List<TestCase> cases) {
        // TODO: 2021/11/16 0016 mapper里面新增一个方法batchInsert,所有数据一次性插入
        for (TestCase c : cases) {
            if (c.isRefFlag()) {
                continue;
            }
            final Project project = projectService.getProjectByName(c.getProjectId());
            final Module module = moduleService.addModule(request, c.getProjectId(), c.getModuleId());
            final TestCase build = TestCase.builder().active(0).projectId(project.getId()).moduleId(module.getId())
                    .name(c.getName()).description(c.getDescription()).precondition(c.getPrecondition())
                    .testMode(0).priority(c.getPriority()).stepStore(c.getStepStore())
                    .resultStore(c.getResultStore()).createDate(new Date()).build();
            baseMapper.insert(build);
            final String[] steps = c.getStepStore().split("\\n");
            final String[] results = c.getResultStore().split("\\n");
            stepsService.saveStep(build.getId(), steps, results);
        }
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
//          未查询到项目数时不使用项目查询条件
            if (CollectionUtils.isNotEmpty(projects)) {
                projects.forEach(temp -> tempProjectId.add(temp.getId()));
                wrapper.lambda().in(TestCase::getProjectId, tempProjectId);
            }
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
    public IPage<TestCase> loadCaseByPlan(Map<String, Object> params) {
        final int pn = Integer.parseInt(String.valueOf(params.get("pn")));
        final String planId = String.valueOf(params.get("planId"));
        /*1. 查询项目相关的测试用例*/
        final IPage<PlanCaseRef> ref = refService.loadRefRecords(null, params);
        List<String> caseIds = new LinkedList<>();
        ref.getRecords().forEach(r -> caseIds.add(r.getCaseId()));
        /*2. 查询所有的测试用例*/
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestCase::getProjectId, projectService.getProjectById(planService.getById(planId).getProjectId()).getId());
        final Page<TestCase> page = baseMapper.selectPage(
                new Page<>(Objects.equals(pn, 0) ? 1 : pn, Objects.equals(10, 0) ? 15 : 10),
                wrapper);
//        不加载已关联的测试用例数据
        final List<TestCase> records = page.getRecords();
        final Iterator<TestCase> iterator = records.iterator();
        while (iterator.hasNext()) {
            final TestCase next = iterator.next();
            caseIds.forEach(c->{
                if (c.equals(next.getId())) {
                    iterator.remove();
                }
            });
        }
        return page;
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
