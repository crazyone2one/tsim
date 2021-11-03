package cn.master.tsim.mapper;

import cn.master.tsim.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目信息
     *
     * @param projectName 项目名称
     * @param workDate    任务时间
     * @return cn.master.tsim.entity.Project
     */
    @Select("select * from t_project where project_name=#{projectName} and del_flag='0'")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "projectTasks", many = @Many(select = "cn.master.tsim.mapper.TestTaskInfoMapper.queryList"))
    })
    Project queryProjectByName(@Param("projectName") String projectName, @Param("workDate") String workDate);

    /**
     * 自定义查询
     *
     * @param page
     * @param proName
     * @param workDate
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.Project>
     */
    IPage<Project> queryList(Page<Project> page, @Param("proName") String proName, @Param("workDate") String workDate);
}
