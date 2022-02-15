package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
     * @return cn.master.tsim.entity.TestPlan
     */
    ResponseResult savePlan(HttpServletRequest request);

    TestPlan uniquePlan(String storyId, String planName, String planDesc);

    TestPlan queryPlanById(String id);

    /**
     * 更新测试计划状态
     *
     * @param planId 测试计划数据id
     * @return cn.master.tsim.entity.TestPlan
     */
    TestPlan updatePlan(String planId);

    /**
     * description: 根据项目查询测试计划数据 <br>
     *
     * @param projectId 项目id
     * @return java.util.List<cn.master.tsim.entity.TestPlan>
     * @author 11's papa
     */
    List<TestPlan> queryPlansByProjectId(String projectId);

    List<Map<String, Object>> loadReportInfo(HttpServletRequest request);

    Map<String, Long> getStatisticsCount(HttpServletRequest request);
}
