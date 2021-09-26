package cn.master.tsim.service.impl;

import cn.master.tsim.entity.TestCaseSteps;
import cn.master.tsim.mapper.TestCaseStepsMapper;
import cn.master.tsim.service.TestCaseStepsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public TestCaseSteps saveStep(TestCaseSteps steps) {
        baseMapper.insert(steps);
        return steps;
    }
}
