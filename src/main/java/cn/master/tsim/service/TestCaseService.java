package cn.master.tsim.service;

import cn.master.tsim.entity.TestCase;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface TestCaseService extends IService<TestCase> {

    /**
     * 保存测试用例
     *
     * @param request  HttpServletRequest
     * @param testCase TestCase参数(暂用不到）
     * @return cn.master.tsim.entity.TestCase
     */
    TestCase saveCase(HttpServletRequest request, TestCase testCase);

    List<TestCase> listTestCase(TestCase testCase, String projectId, String moduleId);

    void updateCase(String caseId);

    IPage<TestCase> pageList(TestCase testCase, Integer pageCurrent, Integer pageSize);

    Map<String, Integer> caseCountByStatus(String projectId, String moduleId);

    Map<String, Map<String, Integer>> caseCountByStatus();

    TestCase getById(String caseId);

    TestCase queryCaseById(String caseId);
}
