package cn.master.tsim.mapper;

import cn.master.tsim.entity.TestTaskInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 任务汇总表 Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
@Mapper
public interface TestTaskInfoMapper extends BaseMapper<TestTaskInfo> {

    /**
     * 查询项目相关任务信息
     *
     * @param projectId 项目id
     * @param workDate  任务时间
     * @return java.util.List<cn.master.tsim.entity.TestTaskInfo>
     */
    @Select("select * from t_task_info where project_id =#{projectId} ")
    List<TestTaskInfo> queryList(@Param("projectId") String projectId, @Param("workDate") String workDate);

    @Select("select * from t_task_info where project_id =#{projectId} and issue_date=#{workDate} ")
    TestTaskInfo queryInfoByIdAndDate(@Param("projectId") String projectId, @Param("workDate") String workDate);

    @Select("select project_id from  t_task_info where issue_date = #{workDate} ")
    List<String> queryTaskInfoId(@Param("workDate") String workDate);

    @Select("SELECT * from t_task_info t1 INNER JOIN t_plan t2 on t2.story_id=t1.story_id WHERE t2.id=#{planId}")
    TestTaskInfo queryTaskInfoByPlan(String planId);
}
