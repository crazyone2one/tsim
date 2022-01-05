package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 测试计划表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
public interface TestPlanService extends IService<TestPlan> {

    IPage<TestPlan> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize);

    /**
     * 保存测试计划数据
     *
     * @param request HttpServletRequest
     * @param storyId    需求id
     * @param planName 计划名称
     * @param planDesc 计划描述
     * @return cn.master.tsim.entity.TestPlan
     */
    ResponseResult savePlan(HttpServletRequest request, String storyId, String planName, String planDesc);

    TestPlan uniquePlan(String storyId, String planName, String planDesc);

    /**
     * 更新测试计划状态
     *
     * @param planId 测试计划数据id
     * @return cn.master.tsim.entity.TestPlan
     */
    TestPlan updatePlan(String planId);

}
