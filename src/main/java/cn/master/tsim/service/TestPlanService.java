package cn.master.tsim.service;

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

    IPage<TestPlan> pageList(TestPlan plan, Integer pageCurrent, Integer pageSize);

    TestPlan savePlan(HttpServletRequest request, TestPlan plan);

    TestPlan updatePlan(String argument);

}
