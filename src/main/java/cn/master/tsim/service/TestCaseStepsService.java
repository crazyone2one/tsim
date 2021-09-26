package cn.master.tsim.service;

import cn.master.tsim.entity.TestCaseSteps;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface TestCaseStepsService extends IService<TestCaseSteps> {

    List<TestCaseSteps> listSteps(String caseId);

    TestCaseSteps saveStep(TestCaseSteps steps);
}
