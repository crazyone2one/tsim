package cn.master.tsim.service;

import cn.master.tsim.entity.TestCase;
import cn.master.tsim.entity.TestCaseSteps;
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
     * @param stepStore json格式测试步骤及结果
     * @param testCase  测试用例数据
     */
    void saveStep(String stepStore, TestCase testCase);

    /**
     * 保存测试步骤数据
     *
     * @param caseId  测试用例数据id
     * @param steps   步骤集合
     * @param results 结果集合
     */
    void saveStep(String caseId, String[] steps, String[] results);
}
