package cn.master.tsim.service.impl;

import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.mapper.PlanCaseRefMapper;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.mapper.TestPlanMapper;
import cn.master.tsim.service.PlanCaseRefService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    private final TestCaseMapper caseMapper;

    @Autowired
    public PlanCaseRefServiceImpl(TestPlanMapper planMapper, TestCaseMapper caseMapper) {
        this.planMapper = planMapper;
        this.caseMapper = caseMapper;
    }

    @Override
    public void addItemRef(String planId, List<String> caseRef) {
        for (String caseId : caseRef) {
            final PlanCaseRef build = PlanCaseRef.builder().planId(planId).caseId(caseId).runStatus(0).runResult(1).build();
            baseMapper.insert(build);
        }
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
            r.setTestCase(caseMapper.selectById(r.getCaseId()));
        });
        return planCaseRefPage;
    }
}
