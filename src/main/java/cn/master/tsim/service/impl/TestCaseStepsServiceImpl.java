package cn.master.tsim.service.impl;

import cn.master.tsim.entity.TestCaseSteps;
import cn.master.tsim.mapper.TestCaseStepsMapper;
import cn.master.tsim.service.TestCaseStepsService;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.StreamUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
    public void saveStep(JSONArray stepsJson, String testCaseId) {
        StreamUtils.forEach(stepsJson, (index, obj) -> {
            final Map<String, Map<String, String>> mapMap = JacksonUtils.convertValue(obj, new TypeReference<Map<String, Map<String, String>>>() {
            });
            TestCaseSteps build = TestCaseSteps.builder().caseId(testCaseId)
                    .caseOrder(index)
                    .caseStep(mapMap.get(String.valueOf(index)).get("t_s"))
                    .caseStepResult(mapMap.get(String.valueOf(index)).get("t_r"))
                    .active(0).createDate(new Date())
                    .build();
            baseMapper.insert(build);
        });
    }

}
