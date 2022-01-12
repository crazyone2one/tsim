package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
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
     * @return cn.master.tsim.entity.TestCase
     */
    ResponseResult saveCase(HttpServletRequest request);

    /**
     * 导入数据
     *
     * @param request HttpServletRequest
     * @param cases 待导入的测试用例数据
     */
    void importCase(HttpServletRequest request, List<TestCase> cases);

    List<TestCase> listTestCase(HttpServletRequest request, TestCase testCase, String projectId, String moduleId);

    /**
     * 测试用例设置为无效状态
     *
     * @param request HttpServletRequest
     */
    void disableCase(HttpServletRequest request);
    void deleteCase(HttpServletRequest request);

    /**
     * 测试用例模块查询
     *
     * @param request     查询参数
     * @param pageCurrent 分页
     * @param pageSize    分页
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.TestCase>
     */
    IPage<TestCase> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize);

    /**
     * 测试计划关联测试用例，查询相应的测试用例
     *
     * @param request HttpServletRequest
     * @param planId  参数
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.TestCase>
     */
    IPage<TestCase> loadCaseByPlan(HttpServletRequest request, String planId);

    Map<String, Integer> caseCountByStatus(String projectId, String moduleId);

    TestCase getById(String caseId);

    /**
     * 查询测试用例
     *
     * @param caseId 测试用例id
     * @return cn.master.tsim.entity.TestCase
     */
    TestCase queryCaseById(String caseId);
}
