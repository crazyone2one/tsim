package cn.master.tsim.service.impl;

import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.mapper.PlanCaseRefMapper;
import cn.master.tsim.mapper.TestPlanMapper;
import cn.master.tsim.service.PlanCaseRefService;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
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

    private final TestPlanMapper planMapper;
    private final TestCaseService caseService;
    private final TestBugService bugService;

    @Autowired
    public PlanCaseRefServiceImpl(TestPlanMapper planMapper, TestCaseService caseService, TestBugService bugService) {
        this.planMapper = planMapper;
        this.caseService = caseService;
        this.bugService = bugService;
    }

    @Override
    public void addItemRef(String planId, List<String> caseRef) {
        for (String caseId : caseRef) {
            final PlanCaseRef build = PlanCaseRef.builder().planId(planId).caseId(caseId).runStatus(0).runResult(1).build();
            baseMapper.insert(build);
        }
    }

    @Override
    public PlanCaseRef uploadItemRef(HttpServletRequest request, Map<String, Object> params) {
        final Map<String, String> bugInfo = JacksonUtils.convertValue(params.get("bug_info"), new TypeReference<Map<String, String>>() {
        });
//       添加关联的bug
        final TestBug build = TestBug.builder().projectId(bugInfo.get("projectId"))
                .moduleId(bugInfo.get("moduleId")).title(bugInfo.get("title")).severity(bugInfo.get("severity"))
                .func(bugInfo.get("func")).bugStatus(bugInfo.get("bugStatus")).note(bugInfo.get("note")).tester(bugInfo.get("tester"))
                .build();
        final TestBug testBug = bugService.addBug(request, build);
        final Map<String, Object> planCase = JacksonUtils.convertValue(params.get("plan_case"), new TypeReference<Map<String, Object>>() {
        });
//        更新关系数据
        PlanCaseRef ref = baseMapper.selectById(String.valueOf(planCase.get("id")));
        ref.setRunStatus(1);
        ref.setRunResult(1);
        ref.setBugId(testBug.getId());
        baseMapper.updateById(ref);
        return ref;
    }

    @Override
    public IPage<PlanCaseRef> loadRefRecords(HttpServletRequest request, Map<String, Object> params) {
        final Integer pageCurrent = Integer.parseInt(String.valueOf(params.get("pn")));
        final Integer pageSize = Integer.parseInt(String.valueOf(params.get("pageSize")));
        final String planId = String.valueOf(params.get("planId"));
        QueryWrapper<PlanCaseRef> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(planId), PlanCaseRef::getPlanId, planId);
        final Page<PlanCaseRef> planCaseRefPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize),
                wrapper);
        planCaseRefPage.getRecords().forEach(r -> {
            r.setPlan(planMapper.selectById(r.getPlanId()));
            r.setTestCase(caseService.getById(r.getCaseId()));
        });
        return planCaseRefPage;
    }
}
