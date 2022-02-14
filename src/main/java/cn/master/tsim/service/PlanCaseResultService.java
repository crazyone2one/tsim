package cn.master.tsim.service;

import cn.master.tsim.entity.PlanCaseResult;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-11
 */
public interface PlanCaseResultService extends IService<PlanCaseResult> {
    /**
     * 添加数据
     *
     * @param result 测试结果数据
     */
    void addRefItem(PlanCaseResult result);

    /**
     * 更新数据
     *
     * @param request 包含测试结果数据的request
     */
    void updateRefItem(HttpServletRequest request);

    /**
     * description: 获取测试结果 <br>
     *
     * @param planId 计划id
     * @param caseId 测试用例id
     * @return java.lang.String
     * @author 11's papa
     */
    String getExecuteResult(String planId, String caseId);

    /**
     * 获取通过率
     *
     * @param planId 计划id
     * @return java.lang.String
     */
    String getPassRate(String planId);

    /**
     * 获取已执行的测试用例数量
     *
     * @param planId 计划id
     * @return java.lang.String
     */
    String getRunCaseCount(String planId);
}
