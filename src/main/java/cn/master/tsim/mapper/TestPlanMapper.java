package cn.master.tsim.mapper;

import cn.master.tsim.entity.TestPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 测试计划表 Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Mapper
public interface TestPlanMapper extends BaseMapper<TestPlan> {
    /**
     * description: 根据测试计划和模块查询对应测试用例的通过率 <br>
     *
     * @param planId
     * @param moduleId
     * @return java.lang.String
     * @author 11's papa
     */
    String getPassRateByPlanAndModule(@Param("planId") String planId, @Param("moduleId") String moduleId);
}
