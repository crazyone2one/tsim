package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.ExcelUtils;
import cn.master.tsim.util.ResponseUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
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
    TestCaseStepsService caseStepsService;


    @Autowired
    public TestCaseServiceImpl(ProjectService projectService, ModuleService moduleService, TestCaseStepsService stepsService) {
        this.projectService = projectService;
        this.moduleService = moduleService;
        this.stepsService = stepsService;
    }

    @Override
    public ResponseResult saveCase(HttpServletRequest request) {
        final String projectId = request.getParameter("projectId");
        final String moduleId = request.getParameter("moduleId");
        final String planId = request.getParameter("tempPlanId");
        final String priority = request.getParameter("priority");
        final Module module = moduleService.addModule(request, projectId, moduleId);
        final String stepStore = request.getParameter("stepStore");
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        String precondition = request.getParameter("precondition");
        String runModeManual = request.getParameter("runModeManual");
        String runModeAuto = request.getParameter("runModeAuto");
        TestCase build = null;
        String message;
        Object object;
        if (StringUtils.isNotBlank(id)) {
            TestCase tempCase = getById(id);
            tempCase.setName(name);
            tempCase.setDescription(description);
            tempCase.setPrecondition(precondition);
            //tempCase.setTestMode(Integer.parseInt(request.getParameter("testMode")));
            tempCase.setRunModeManual(StringUtils.isNotBlank(runModeManual) ? runModeManual : "");
            tempCase.setRunModeAuto(StringUtils.isNotBlank(runModeAuto) ? runModeAuto : "");
            tempCase.setPriority(priority);
            tempCase.setStepStore(stepStore);
            tempCase.setUpdateDate(new Date());
            baseMapper.updateById(tempCase);
            stepsService.removeStepByCaseId(id);
            stepsService.saveStep(JSON.parseArray(stepStore), id);
            message = "数据更新成功";
            object = tempCase;
        } else {
            build = TestCase.builder().active(0).projectId(projectId).moduleId(module.getId())
                    .name(name)
                    .description(description)
                    .precondition(precondition)
                    .runModeManual(StringUtils.isNotBlank(runModeManual) ? runModeManual : "")
                    .runModeAuto(StringUtils.isNotBlank(runModeAuto) ? runModeAuto : "")
                    .priority(priority)
                    .stepStore(stepStore)
                    .resultStore(request.getParameter("resultStore"))
                    .delFlag(0)
                    .createDate(new Date()).build();
            baseMapper.insert(build);
            stepsService.saveStep(JSON.parseArray(stepStore), build.getId());
            message = "数据添加成功";
            object = build;
        }
        if (StringUtils.isNotBlank(planId)) {
            assert build != null;
            final int item = projectCaseRefService.addRefItem(projectId, "", planId, build.getId());
            final TestTaskInfo taskInfo = taskInfoService.queryItem(projectId, "", planId);
            taskInfo.setCreateCaseCount(taskInfo.getCreateCaseCount() + item);
            taskInfoService.updateTask(request, taskInfo);
        }
        return ResponseUtils.success(message, object);
    }

    @Override
    public void importCase(HttpServletRequest request, List<TestCase> cases) {
        String projectId = request.getParameter("projectId");
        String tempPlanId = request.getParameter("tempPlanId");
        // TODO: 2021/11/16 0016 mapper里面新增一个方法batchInsert,所有数据一次性插入
        for (TestCase c : cases) {
            if (c.isRefFlag()) {
                continue;
            }
            final String[] steps = c.getStepStore().split("\\n");
            final String[] results = c.getResultStore().split("\\n");
            JSONArray stepsJson = new JSONArray();
            for (int i = 0; i < steps.length; i++) {
                Map<String, Map<String, String>> stepsMap = new LinkedHashMap<>();
                Map<String, String> tempMap = new LinkedHashMap<>();
                tempMap.put("t_s", steps[i]);
                // 不太清楚这样处理是否正确
                try {
                    tempMap.put("t_r", Objects.nonNull(results[i]) ? results[i] : "");
                } catch (ArrayIndexOutOfBoundsException e) {
                    tempMap.put("t_r", "");
                }
                stepsMap.put("" + i, tempMap);
                stepsJson.add(stepsMap);
            }
            if (StringUtils.isBlank(projectId)) {
                Project projectByName = projectService.getProjectByName(c.getProjectId());
                projectId = projectByName.getId();
            }
            final Module module = moduleService.addModule(request, projectId, c.getModuleId());
            final TestCase build = TestCase.builder().active(0).projectId(projectId).moduleId(module.getId())
                    .name(c.getName()).description(c.getDescription()).precondition(c.getPrecondition())
                    .testMode(0).runModeManual("0").priority(c.getPriority()).stepStore(stepsJson.toJSONString())
                    .resultStore("").delFlag(0).createDate(new Date()).build();
            baseMapper.insert(build);
            stepsService.saveStep(stepsJson, build.getId());
            if (StringUtils.isNotBlank(tempPlanId)) {
                final int item = projectCaseRefService.addRefItem(projectId, "", tempPlanId, build.getId());
                final TestTaskInfo taskInfo = taskInfoService.queryItem(projectId, "", tempPlanId);
                taskInfo.setCreateCaseCount(taskInfo.getCreateCaseCount() + item);
                taskInfoService.updateTask(request, taskInfo);
            }
        }
    }

    @Override
    public List<TestCase> listTestCase(HttpServletRequest request, TestCase testCase, String projectId, String moduleId) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        if (Objects.nonNull(testCase)) {
            extractedSearchWrapper(request, wrapper, tempProjectId, tempModuleId);
        } else {
            wrapper.lambda().eq(StringUtils.isNotBlank(projectId), TestCase::getProjectId, projectId);
            wrapper.lambda().eq(StringUtils.isNotBlank(moduleId), TestCase::getModuleId, moduleId);
        }
        return baseMapper.selectList(wrapper);
    }

    private void extractedSearchWrapper(HttpServletRequest request, QueryWrapper<TestCase> wrapper, List<String> tempProjectId, List<String> tempModuleId) {
        //            项目名称模糊查询
        final String projectName = request.getParameter("projectName");
        if (StringUtils.isNotBlank(projectName)) {
            final List<Project> projects = projectService.findByPartialProjectName(projectName);
//          未查询到项目数时不使用项目查询条件
            if (CollectionUtils.isNotEmpty(projects)) {
                projects.forEach(temp -> tempProjectId.add(temp.getId()));
                wrapper.lambda().in(TestCase::getProjectId, tempProjectId);
            }
        }
//            模块模糊查询
        final String moduleName = request.getParameter("moduleName");
        if (StringUtils.isNotBlank(moduleName)) {
            moduleService.findByPartialModuleName(moduleName).forEach(temp -> tempModuleId.add(temp.getId()));
            wrapper.lambda().in(TestCase::getModuleId, tempModuleId);
        }
//            测试用例标题模糊查询
        final String caseName = request.getParameter("caseName");
        wrapper.lambda().like(StringUtils.isNotBlank(caseName), TestCase::getName, caseName);
//            优先级
        final String priority = request.getParameter("priority");
        if (StringUtils.isNotBlank(priority)) {
            wrapper.lambda().eq(TestCase::getPriority, Integer.parseInt(priority));
        }
        final String active = request.getParameter("active");
        if (StringUtils.isNotBlank(active)) {
            wrapper.lambda().eq(TestCase::getActive, Integer.parseInt(active));
        }
        wrapper.lambda().eq(TestCase::getDelFlag, 0).orderByAsc(TestCase::getActive);
    }

    @Override
    public void disableCase(HttpServletRequest request) {
        final String ids = request.getParameter("ids");
        final List<String> idList = JSONArray.parseArray(ids, String.class);
        idList.forEach(t -> {
            final TestCase testCase = baseMapper.selectById(t);
            testCase.setActive(1);
            testCase.setUpdateDate(new Date());
            baseMapper.updateById(testCase);
        });
    }

    @Override
    public void deleteCase(HttpServletRequest request) {
        final List<String> idList = JSONArray.parseArray(request.getParameter("ids"), String.class);
        idList.forEach(t -> {
            final TestCase testCase = baseMapper.selectById(t);
            testCase.setDelFlag(1);
            testCase.setUpdateDate(new Date());
            baseMapper.updateById(testCase);
            caseStepsService.removeStepByCaseId(testCase.getId());
        });
    }

    @Override
    public IPage<TestCase> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        if (Objects.nonNull(request)) {
            extractedSearchWrapper(request, wrapper, tempProjectId, tempModuleId);
        }
        final Page<TestCase> page = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize),
                wrapper);
        page.getRecords().forEach(t -> {
            t.setProject(projectService.getProjectById(t.getProjectId()));
            t.setModule(moduleService.getModuleById(t.getModuleId()));
        });
        return page;
    }

    @Override
    public IPage<TestCase> loadCaseByPlan(HttpServletRequest request, String planId) {
        String moduleName = request.getParameter("moduleName");
        String title = request.getParameter("title");
        final int pn = Integer.parseInt(request.getParameter("pageNum"));
        /*1. 查询项目相关的测试用例*/
        List<String> refIds = new LinkedList<>();
        refService.loadRefItemByPlanId(planId).forEach(r -> refIds.add(r.getCaseId()));
        /*2. 查询所有的测试用例*/
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        //        根据测试计划查询对应项目下的测试用例数据
        wrapper.lambda().eq(TestCase::getProjectId, projectService.getProjectById(planService.getById(planId).getProjectId()).getId());
        wrapper.lambda().inSql(StringUtils.isNotBlank(moduleName), TestCase::getModuleId, "select id from t_module where module_name like '%" + moduleName + "%'");
        wrapper.lambda().like(StringUtils.isNotBlank(title), TestCase::getName, title).eq(TestCase::getDelFlag, 0);
        List<TestCase> records = baseMapper.selectList(wrapper);
        List<String> unRefIds = new ArrayList<>();
        records.forEach(t -> unRefIds.add(t.getId()));
        /*3.移除已关联过的测试用例*/
        refIds.forEach(t -> {
            unRefIds.removeIf(u -> Objects.equals(u, t));
        });
        if (CollectionUtils.isEmpty(unRefIds)) {
            unRefIds.add(" ");
        }
        Page<TestCase> page = baseMapper.selectPage(
                new Page<>(Objects.equals(pn, 0) ? 1 : pn, Objects.equals(10, 0) ? 15 : 10),
                // FIXME: 2022/1/14 0014
                new QueryWrapper<TestCase>().in("id", unRefIds));
        page.getRecords().forEach(testCase -> {
            testCase.setModule(moduleService.getModuleById(testCase.getModuleId()));
        });
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
        testCase.setModule(moduleService.getModuleById(testCase.getModuleId()));
        return testCase;
    }

    @CachePut(cacheNames = "caseData")
    public List<TestCase> queryList(HttpServletRequest request) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        extractedSearchWrapper(request, wrapper, tempProjectId, tempModuleId);
        final List<String> idList = JSONArray.parseArray(request.getParameter("ids"), String.class);
        wrapper.lambda().in(CollectionUtils.isNotEmpty(idList), TestCase::getId, idList);
        return baseMapper.selectList(wrapper);
    }
    @Override
    public ResponseResult checkCaseData(HttpServletRequest request) {
        List<TestCase> testCases = queryList(request);
        if (CollectionUtils.isEmpty(testCases)) {
            return ResponseUtils.success(ResponseCode.PARAMS_ERROR.getCode(), "未查询到相关数据");
        }
        return ResponseUtils.success("数据查询成功", testCases);
    }

    @Override
    @Cacheable(cacheNames = "caseData")
    public void exportCaseInfo(HttpServletRequest request, HttpServletResponse response, TestCase testCase) throws IOException {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        //            项目名称模糊查询
        final String projectName = testCase.getProjectId();
        if (StringUtils.isNotBlank(projectName)) {
            final List<Project> projects = projectService.findByPartialProjectName(projectName);
//          未查询到项目数时不使用项目查询条件
            if (CollectionUtils.isNotEmpty(projects)) {
                projects.forEach(temp -> tempProjectId.add(temp.getId()));
                wrapper.lambda().in(TestCase::getProjectId, tempProjectId);
            }
        }
//            模块模糊查询
        final String moduleName = testCase.getModuleId();
        if (StringUtils.isNotBlank(moduleName)) {
            moduleService.findByPartialModuleName(moduleName).forEach(temp -> tempModuleId.add(temp.getId()));
            wrapper.lambda().in(TestCase::getModuleId, tempModuleId);
        }
//            测试用例标题模糊查询
        final String caseName = testCase.getName();
        wrapper.lambda().like(StringUtils.isNotBlank(caseName), TestCase::getName, caseName);
//            优先级
        wrapper.lambda().eq(StringUtils.isNotBlank(testCase.getPriority()),TestCase::getPriority, testCase.getPriority());
        wrapper.lambda().eq(Objects.nonNull(testCase.getActive()),TestCase::getActive, testCase.getActive());
        wrapper.lambda().eq(TestCase::getDelFlag, 0).orderByAsc(TestCase::getActive);
        String caseId = testCase.getId();
        if (StringUtils.isNotBlank(caseId)) {
            String[] ids = caseId.split(",");
            wrapper.lambda().in(TestCase::getId, Arrays.asList(ids));
        }
        List<TestCase> testCases = baseMapper.selectList(wrapper);
        testCases.forEach(t->{
            t.setProjectId(projectService.getProjectById(t.getProjectId()).getProjectName());
            t.setModuleId(moduleService.getModuleById(t.getModuleId()).getModuleName());
            TestCase tempCase = baseMapper.queryCaseInfo(t.getId());
            StringJoiner tempStep = new StringJoiner("\\n");
            StringJoiner tempResult = new StringJoiner("\\n");
            tempCase.getCaseSteps().forEach(testCaseSteps -> {
                tempStep.add(testCaseSteps.getCaseStep());
                tempResult.add(testCaseSteps.getCaseStepResult());
            });
            t.setStepStore(tempStep.toString());
            t.setResultStore(tempResult.toString());
            if (StringUtils.isNotBlank(t.getRunModeManual()) && StringUtils.isNotBlank(t.getRunModeAuto())) {
                t.setRunModeManual("手动/自动");
            } else if (StringUtils.isNotBlank(t.getRunModeManual())) {
                t.setRunModeManual("手动");
            } else if (StringUtils.isNotBlank(t.getRunModeAuto())) {
                t.setRunModeManual("自动");
            }
            t.setPriority(Constants.CASE_PRIORITY.get(t.getPriority()));
        });
        ExcelUtils.exportByExcelFile(response, testCases, "测试用例数据", TestCase.class);
    }


}
