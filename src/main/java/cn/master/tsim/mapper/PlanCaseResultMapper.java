package cn.master.tsim.mapper;

import cn.master.tsim.entity.PlanCaseResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-11
 */
@Mapper
public interface PlanCaseResultMapper extends BaseMapper<PlanCaseResult> {

    @Select("select * from t_plan_case_result where plan_id=#{planId} and case_id=#{caseId} and case_step_id=#{stepId}")
    PlanCaseResult queryPlanCaseResult(@Param("planId") String planId, @Param("caseId") String caseId, @Param("stepId") String stepId);

    @Select("select * from t_plan_case_result where plan_id=#{planId} and case_id=#{caseId}")
    List<PlanCaseResult> queryCaseResultList(@Param("planId") String planId, @Param("caseId") String caseId);

    @Select("select * from t_plan_case_result where plan_id=#{planId}")
    List<PlanCaseResult> queryCaseResultListByPlanId(@Param("planId") String planId);
}
