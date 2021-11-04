package cn.master.tsim.mapper;

import cn.master.tsim.entity.TestCaseSteps;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface TestCaseStepsMapper extends BaseMapper<TestCaseSteps> {

    @Select("select * from t_case_steps where case_id=#{caseId} ")
    List<TestCaseSteps> listAllByCaseId(String caseId);
}
