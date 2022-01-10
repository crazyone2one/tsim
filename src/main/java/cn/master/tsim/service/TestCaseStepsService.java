package cn.master.tsim.service;

import cn.master.tsim.entity.TestCaseSteps;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface TestCaseStepsService extends IService<TestCaseSteps> {

    List<TestCaseSteps> listSteps(String caseId);

    /**
     * 保存测试步骤数据
     *
     * @param stepsJson  json格式测试步骤及结果
     * @param testCaseId 测试用例数据
     */
    void saveStep(JSONArray stepsJson, String testCaseId);

    void removeStepByCaseId(String id);

}
