package cn.master.tsim.service.impl;

import cn.master.tsim.entity.PlanCaseResult;
import cn.master.tsim.mapper.PlanCaseResultMapper;
import cn.master.tsim.service.PlanCaseRefService;
import cn.master.tsim.service.PlanCaseResultService;
import cn.master.tsim.util.JacksonUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-11
 */
@Service
public class PlanCaseResultServiceImpl extends ServiceImpl<PlanCaseResultMapper, PlanCaseResult> implements PlanCaseResultService {
    @Autowired
    private PlanCaseRefService planCaseRefService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void addRefItem(PlanCaseResult result) {
        baseMapper.insert(result);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateRefItem(HttpServletRequest request) {
        String planId = request.getParameter("planId");
        String caseId = request.getParameter("caseId");
        String caseFragment = request.getParameter("caseFragment");
        String bugFragment = request.getParameter("bugFragment");
        List<Map<String, String>> extraParams = JacksonUtils.jsonToObject(caseFragment, new TypeReference<List<Map<String, String>>>() {
        });
        // 更新每条测试步骤的结果
        extraParams.forEach(t->{
            String caseStepId = t.get("caseStepId");
            PlanCaseResult result = baseMapper.queryPlanCaseResult(planId, caseId, caseStepId);
            result.setRunStatus(true);
            result.setRunResult(Optional.ofNullable(t.get("caseResult")).orElse(""));
            result.setExecuteResult(Optional.ofNullable(t.get("executeResult")).orElse(""));
            baseMapper.updateById(result);
        });
        // 更新测试用例数据的结果
        planCaseRefService.updateItemRef(planId, caseId, "");
        // TODO: 2022/2/14 0014 添加关联bug
    }

    @Override
    public String getExecuteResult(String planId, String caseId) {
        List<PlanCaseResult> results = baseMapper.queryCaseResultList(planId, caseId);
        long blockingCount = results.stream().filter(t -> Objects.equals(t.getExecuteResult(), "3")).count();
        if (blockingCount > 0) {
            return "3";
        }
        long passCount = results.stream().filter(t -> Objects.equals(t.getExecuteResult(), "1")).count();
        if (Objects.equals(passCount, (long) results.size())) {
            return "1";
        } else {
            return "2";
        }
    }

    @Override
    public String getPassRate(String planId) {
        List<PlanCaseResult> results = baseMapper.queryCaseResultListByPlanId(planId);
        long passCount = results.stream().filter(t -> Objects.equals(t.getExecuteResult(), "1")).count();
        if (passCount > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            float rate = Float.parseFloat(decimalFormat.format(passCount * 1.0 / results.size())) * 100;
            return String.valueOf(rate);
        }
        return "0.0";
    }

    @Override
    public String getRunCaseCount(String planId) {
        List<PlanCaseResult> results = baseMapper.queryCaseResultListByPlanId(planId);
        long count = results.stream().filter(t -> Objects.equals(t.getRunStatus(), true)).count();
        return count + "/" + results.size();
    }
}
