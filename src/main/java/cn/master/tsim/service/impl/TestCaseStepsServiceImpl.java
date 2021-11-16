package cn.master.tsim.service.impl;

import cn.master.tsim.entity.TestCase;
import cn.master.tsim.entity.TestCaseSteps;
import cn.master.tsim.mapper.TestCaseStepsMapper;
import cn.master.tsim.service.TestCaseStepsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Service
public class TestCaseStepsServiceImpl extends ServiceImpl<TestCaseStepsMapper, TestCaseSteps> implements TestCaseStepsService {

    @Override
    public List<TestCaseSteps> listSteps(String caseId) {
        QueryWrapper<TestCaseSteps> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(caseId), TestCaseSteps::getCaseId, caseId)
                .orderByAsc(TestCaseSteps::getCaseOrder);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void saveStep(HttpServletRequest request, TestCase testCase) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        final String[] steps = parameterMap.get("caseSteps[]");
        final String[] results = parameterMap.get("caseExpectedResults[]");
        TestCaseSteps build;
        for (int i = 0; i < steps.length; i++) {
            build = TestCaseSteps.builder().caseId(testCase.getId())
                    .caseOrder(i)
                    .caseStep(steps[i]).caseStepResult(results[i]).active(0).createDate(new Date())
                    .build();
            baseMapper.insert(build);
        }
    }

    @Override
    public void saveStep(String caseId, String[] steps, String[] results) {
        TestCaseSteps build;
        for (int i = 0; i < steps.length; i++) {
            build = TestCaseSteps.builder().caseId(caseId)
                    .caseOrder(i)
                    .caseStep(steps[i]).caseStepResult(results[i]).active(0).createDate(new Date())
                    .build();
            baseMapper.insert(build);
        }
    }
}
