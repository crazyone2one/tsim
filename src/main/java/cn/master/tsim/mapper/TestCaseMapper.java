package cn.master.tsim.mapper;

import cn.master.tsim.entity.TestCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Mapper
public interface TestCaseMapper extends BaseMapper<TestCase> {

    @Select("select * from t_case where id=#{caseId} ")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "caseSteps", many = @Many(select = "cn.master.tsim.mapper.TestCaseStepsMapper.listAllByCaseId"))
    })
    TestCase queryCaseInfo(String caseId);
}
