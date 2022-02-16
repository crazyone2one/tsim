package cn.master.tsim.mapper;

import cn.master.tsim.entity.TestCaseSteps;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface TestCaseStepsMapper extends BaseMapper<TestCaseSteps> {
    /**
     * description: 查询测试用例步骤数据 <br>
     *
     * @param caseId 测试用例id
     * @return java.util.List<cn.master.tsim.entity.TestCaseSteps>
     * @author 11's papa
     */
    @Select("select * from t_case_steps where case_id=#{caseId} and active='0';")
    List<TestCaseSteps> listAllByCaseId(String caseId);
}
