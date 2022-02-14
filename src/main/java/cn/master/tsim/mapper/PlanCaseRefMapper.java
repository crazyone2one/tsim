package cn.master.tsim.mapper;

import cn.master.tsim.entity.PlanCaseRef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-26
 */
@Mapper
public interface PlanCaseRefMapper extends BaseMapper<PlanCaseRef> {

    @Select("select * from t_plan_case_ref where plan_id=#{planId} and case_id=#{caseId}")
    PlanCaseRef queryPlanCaseRef(@Param("planId") String planId, @Param("caseId") String caseId);
}
