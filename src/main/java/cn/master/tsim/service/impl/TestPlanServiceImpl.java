package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.mapper.TestPlanMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
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
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 测试计划表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Service
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan> implements TestPlanService {

    @Autowired
    private TestStoryService storyService;
    private final PlanStoryRefService planStoryRefService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    TestTaskInfoService taskInfoService;
    @Autowired
    PlanCaseRefService planCaseRefService;
    @Autowired
    PlanCaseResultService planCaseResultService;
    @Autowired
    TestCaseMapper testCaseMapper;
    @Autowired
    TestCaseService testCaseService;

    @Autowired
    public TestPlanServiceImpl(PlanStoryRefService planStoryRefService) {
        this.planStoryRefService = planStoryRefService;
    }

    @Override
    public IPage<TestPlan> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        String sortName = request.getParameter("sortName");
        String sortOrder = request.getParameter("sortOrder");
        String currentUser = request.getParameter("currentUser");
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<>();
//        按照项目查询
        final String projectName = request.getParameter("projectName");
        if (StringUtils.isNotBlank(projectName)) {
            List<String> tempProjectId = new LinkedList<>();
            projectService.findByPartialProjectName(projectName).forEach(t -> tempProjectId.add(t.getId()));
            wrapper.lambda().in(TestPlan::getProjectId, tempProjectId);
        }
        //        根据测试计划名称查询
        final String planName = request.getParameter("planName");
        wrapper.lambda().like(StringUtils.isNotBlank(planName), TestPlan::getName, planName);
        //        根据测试计划描述内容查询
        final String planDesc = request.getParameter("planDesc");
        wrapper.lambda().like(StringUtils.isNotBlank(planDesc), TestPlan::getDescription, planDesc);
        // 完成状态
        String workStatus = request.getParameter("workStatus");
        if (Objects.nonNull(workStatus) && StringUtils.isNotBlank(workStatus)) {
            wrapper.lambda().eq(TestPlan::getWorkStatus, Integer.valueOf(workStatus));
        }
        wrapper.lambda().eq(TestPlan::getDelFlag, 0);
        // 开始时间、结束时间排序
        if (StringUtils.isNotBlank(sortName)) {
            if (Objects.equals("startDate", sortName)) {
                if (Objects.equals("asc", sortOrder)) {
                    wrapper.lambda().orderByAsc(TestPlan::getStartDate);
                } else {
                    wrapper.lambda().orderByDesc(TestPlan::getStartDate);
                }
            }
            if (Objects.equals("finishDate", sortName)) {
                if (Objects.equals("asc", sortOrder)) {
                    wrapper.lambda().orderByAsc(TestPlan::getStartDate);
                } else {
                    wrapper.lambda().orderByDesc(TestPlan::getStartDate);
                }
            }
        }
        // 当前登录人
        if (StringUtils.isNotBlank(currentUser)) {
            Tester user = (Tester) request.getSession().getAttribute("account");
            wrapper.lambda().eq(TestPlan::getDutyMan, user.getId());
        }
        final Page<TestPlan> iPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        iPage.getRecords().forEach(t -> {
            t.setProject(projectService.getProjectById(t.getProjectId()));
            if (StringUtils.isNotBlank(t.getStoryId())) {
                t.setStory(storyService.searchStoryById(t.getStoryId()));
            }
            t.setPassRate(planCaseResultService.getPassRate(t.getId()));
            List<PlanCaseRef> planCaseRefs = planCaseRefService.loadRefItemByPlanId(t.getId());
            if (CollectionUtils.isNotEmpty(planCaseRefs)) {
                long count = planCaseRefs.stream().filter(p -> !"0".equals(p.getRunStatus())).count();
                if (count > 0) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    float v = Float.parseFloat(decimalFormat.format(count * 1.0 / planCaseRefs.size())) * 100;
                    t.setFinishProcess(String.valueOf(v));
                    t.setRunCaseCount(count + "/" + planCaseRefs.size());
                }
                for (PlanCaseRef ref : planCaseRefs) {
                    if (Objects.isNull(ref.getRunStatus())) {
                        t.setFinished(false);
                        break;
                    }
                }
            }
        });
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public ResponseResult savePlan(HttpServletRequest request) {
        String id = request.getParameter("id");
        String tempProjectId = request.getParameter("projectId");
        String tempStoryId = request.getParameter("storyId");
        String tempPlanName = request.getParameter("name");
        String description = request.getParameter("description").trim();
        String startDate = request.getParameter("startDate");
        String finishDate = request.getParameter("finishDate");
        String dutyMan = request.getParameter("dutyMan");
        if (StringUtils.isNotBlank(id)) {
            TestPlan byId = getById(id);
            byId.setStoryId(tempStoryId);
            byId.setName(tempPlanName);
            byId.setDescription(description);
            byId.setStartDate(DateUtils.parseStrToDate(DateUtils.DATEFORMAT_DAY, startDate));
            byId.setFinishDate(DateUtils.parseStrToDate(DateUtils.DATEFORMAT_DAY, finishDate));
            byId.setDutyMan(dutyMan);
            byId.setUpdateDate(new Date());
            updateById(byId);
            return ResponseUtils.success(ResponseCode.SUCCESS.getCode(), "数据更新成功", byId);
        }
//        同一项目下不允许出现相同的测试计划
        final TestPlan uniquePlan = uniquePlan(tempStoryId, tempPlanName, tempProjectId);
        if (Objects.nonNull(uniquePlan)) {
            return ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(), "存在相同测试计划");
        }
        TestPlan build = TestPlan.builder().description(description).name(tempPlanName)
                .projectId(tempProjectId).storyId(tempStoryId).workDate(DateUtils.parse2String(new Date(), "yyyy-MM"))
                .startDate(DateUtils.parseStrToDate(DateUtils.DATEFORMAT_DAY, startDate))
                .finishDate(DateUtils.parseStrToDate(DateUtils.DATEFORMAT_DAY, finishDate))
                .dutyMan(dutyMan)
                .delFlag(0).createDate(new Date()).build();
        baseMapper.insert(build);
        planStoryRefService.addRefItem(build.getId(), tempStoryId);
        taskInfoService.addItem(request, tempProjectId, build);
        return ResponseUtils.success(ResponseCode.SUCCESS.getCode(), "数据添加成功", build);
    }

    @Override
    public TestPlan uniquePlan(String storyId, String planName, String projectId) {
        return baseMapper.selectOne(new QueryWrapper<TestPlan>().lambda()
                .eq(TestPlan::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(storyId), TestPlan::getStoryId, storyId)
                .eq(TestPlan::getName, planName));
//                .eq(StringUtils.isNotBlank(projectId),TestPlan::getDescription, projectId));
    }

    @Override
    public TestPlan queryPlanById(String id) {
        TestPlan byId = getById(id);
        if (StringUtils.isNotBlank(byId.getStoryId())) {
            byId.setStory(storyService.searchStoryById(byId.getStoryId()));
        }
        byId.setProject(projectService.getProjectById(byId.getProjectId()));
        return byId;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public TestPlan updatePlan(String planId) {
        final TestPlan testPlan = baseMapper.selectById(planId);
        testPlan.setDelFlag(Objects.equals(testPlan.getDelFlag(), 0) ? 1 : 0);
        testPlan.setUpdateDate(new Date());
        baseMapper.updateById(testPlan);
        return testPlan;
    }

    @Override
    public List<TestPlan> queryPlansByProjectId(String projectId) {
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestPlan::getProjectId, projectId).eq(TestPlan::getDelFlag, 0);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> loadReportInfo(HttpServletRequest request) {
        List<Map<String, Object>> result = new LinkedList<>();
        String planId = request.getParameter("planId");
        List<PlanCaseRef> planCaseRefs = planCaseRefService.loadRefItemByPlanId(planId);
        if (CollectionUtils.isEmpty(planCaseRefs)) {
            return result;
        }
        List<String> caseId = new LinkedList<>();
        planCaseRefs.forEach(t -> caseId.add(t.getCaseId()));
        List<Map<String, Object>> maps = testCaseMapper.selectMaps(new QueryWrapper<TestCase>().lambda().in(TestCase::getId, caseId).groupBy(TestCase::getModuleId));
        maps.forEach(t -> {
            Map<String, Object> map = new LinkedHashMap<>();
            List<TestCase> casesByModule = testCaseMapper.selectList(new QueryWrapper<TestCase>().lambda().in(TestCase::getId, caseId).eq(TestCase::getModuleId, t.get("module_id")));
            map.put("moduleName", testCaseService.getById((String) t.get("id")).getModule().getModuleName());
            map.put("caseCount", casesByModule.size());
            map.put("passRate", baseMapper.getPassRateByPlanAndModule(planId, (String) t.get("module_id")));
            result.add(map);
        });
        return result;
    }

    @Override
    public Map<String, Long> getStatisticsCount(HttpServletRequest request) {
        String planId = request.getParameter("planId");
        List<PlanCaseRef> results = planCaseRefService.loadRefItemByPlanId(planId);
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("unExecute", results.stream().filter(t -> Objects.equals(t.getRunStatus(), "0")).count());
        map.put("pass", results.stream().filter(t -> (Objects.equals(t.getRunStatus(), "1") && Objects.equals(t.getRunResult(), "1"))).count());
        map.put("fail", results.stream().filter(t -> (Objects.equals(t.getRunStatus(), "1") && Objects.equals(t.getRunResult(), "2"))).count());
        map.put("blocking", results.stream().filter(t -> (Objects.equals(t.getRunStatus(), "1") && Objects.equals(t.getRunResult(), "3"))).count());
        return map;
    }
}
