package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.mapper.PlanCaseRefMapper;
import cn.master.tsim.mapper.TestTaskInfoMapper;
import cn.master.tsim.service.PlanCaseRefService;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.service.TestPlanService;
import cn.master.tsim.util.ResponseUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-26
 */
@Service
public class PlanCaseRefServiceImpl extends ServiceImpl<PlanCaseRefMapper, PlanCaseRef> implements PlanCaseRefService {

    private final TestCaseService caseService;
    private final TestBugService bugService;
    private final TestTaskInfoMapper taskInfoMapper;
    @Autowired
    private TestPlanService planService;

    @Autowired
    public PlanCaseRefServiceImpl(TestCaseService caseService, TestBugService bugService, TestTaskInfoMapper testTaskInfoMapper) {
        this.caseService = caseService;
        this.bugService = bugService;
        this.taskInfoMapper = testTaskInfoMapper;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void addItemRef(String planId, List<String> caseRef) {
        int index = 0;
        for (String caseId : caseRef) {
            final PlanCaseRef build = PlanCaseRef.builder().planId(planId).caseId(caseId).build();
            index += baseMapper.insert(build);
        }
        //            保存测试计划关联的测试用例时，更新task表中新增测试用例数量
        final TestTaskInfo taskInfo = taskInfoMapper.queryTaskInfoByPlan(planId);
        taskInfo.setCreateCaseCount(taskInfo.getCreateCaseCount() + index);
        taskInfo.setUpdateDate(new Date());
        taskInfoMapper.updateById(taskInfo);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public PlanCaseRef addBugByFailCase(HttpServletRequest request) {
        String moduleId = request.getParameter("moduleId");
//       添加关联的bug
        final TestBug build = TestBug.builder().projectId(request.getParameter("projectId"))
                .moduleId(request.getParameter("moduleId"))
                .title(request.getParameter("title"))
                .severity(Integer.parseInt(request.getParameter("severity")))
                .func(request.getParameter("func"))
                .bugDescription(request.getParameter("bugDescription"))
                .reproduceSteps(request.getParameter("reproduceSteps"))
                .expectResult(request.getParameter("expectResult"))
                .actualResult(request.getParameter("actualResult"))
                .bugStatus(Integer.parseInt(request.getParameter("bugStatus")))
                .note(request.getParameter("note"))
                .tester(request.getParameter("tester"))
                .bugRecurrenceProbability(Integer.parseInt(request.getParameter("bugRecurrenceProbability")))
                .build();
        final TestBug testBug = bugService.saveOrUpdateBug(request, build);
//        更新关系数据
        PlanCaseRef ref = baseMapper.selectById(request.getParameter("ref-id"));
        ref.setRunStatus(1);
        ref.setRunResult(1);
        ref.setBugId(testBug.getId());
        baseMapper.updateById(ref);
        return ref;
    }

    @Override
    public IPage<PlanCaseRef> loadRefRecords(HttpServletRequest request, String planId) {
        final Integer pageCurrent = Integer.parseInt(request.getParameter("pageNum"));
        final Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        QueryWrapper<PlanCaseRef> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(planId), PlanCaseRef::getPlanId, planId);
        final Page<PlanCaseRef> planCaseRefPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize),
                wrapper);
        planCaseRefPage.getRecords().forEach(r -> {
            r.setPlan(planService.queryPlanById(r.getPlanId()));
            r.setTestCase(caseService.getById(r.getCaseId()));
        });
        return planCaseRefPage;
    }

    @Override
    public List<PlanCaseRef> loadRefItemByPlanId(String planId) {
        QueryWrapper<PlanCaseRef> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(planId), PlanCaseRef::getPlanId, planId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public ResponseResult batchPass(HttpServletRequest request) {
        try {
            List<String> refIds = JSONArray.parseArray(request.getParameter("refIds"),String.class);
            refIds.forEach(t -> {
                PlanCaseRef planCaseRef = getById(t);
                planCaseRef.setRunStatus(1);
                planCaseRef.setRunResult(0);
                updateById(planCaseRef);
            });
            return ResponseUtils.success("数据更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error(400, "数据更新失败", e.getMessage());
        }
    }
}
