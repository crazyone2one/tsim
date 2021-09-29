package cn.master.tsim.service;

import cn.master.tsim.entity.TestCase;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface TestCaseService extends IService<TestCase> {

    TestCase saveCase(TestCase testCase);

    List<TestCase> listTestCase(TestCase testCase, String projectId, String moduleId);

    void updateCase(String caseId);

    IPage<TestCase> pageList(TestCase testCase, Integer pageCurrent, Integer pageSize);
}
