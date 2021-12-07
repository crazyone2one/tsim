package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
public interface ProjectService extends IService<Project> {
    /**
     * 模糊查询项目
     *
     * @param searchString 项目名称
     * @return java.util.List<cn.master.tsim.entity.Project>
     */
    List<Project> findByPartialProjectName(String searchString);

    /**
     * 查询项目
     *
     * @param id 项目id
     * @return cn.master.tsim.entity.Project
     */
    Project getProjectById(String id);

    /**
     * 精确查询项目
     *
     * @param projectName 项目名称
     * @return cn.master.tsim.entity.Project
     */
    Project getProjectByName(String projectName);

    /**
     * 根据项目名称和任务时间查询项目数据
     *
     * @param projectName 项目名称
     * @param workDate    任务时间
     * @return cn.master.tsim.entity.Project
     */
    Project checkProject(String projectName, String workDate);

    /**
     * 添加项目数据
     *
     * @param request HttpServletRequest
     * @param project Project
     * @return cn.master.tsim.entity.Project
     */
    Project addProject(HttpServletRequest request, Project project);

    Project saveProject(String projectName);

    /**
     * 更新项目状态
     *
     * @param projectId projectId
     */
    void updateProjectStatus(String projectId);

    /**
     * 分页查询
     *
     * @param project     项目信息
     * @param pageCurrent pageCurrent
     * @param pageSize    pageSize
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.Project>
     */
    IPage<Project> projectListPages(Project project, Integer pageCurrent, Integer pageSize);


    /**
     * 生成测试报告
     *
     * @param request
     * @param response
     * @param id
     * @param workDate
     * @param storyId
     * @return cn.master.tsim.common.ResponseResult
     */
    ResponseResult generateReport(HttpServletRequest request, HttpServletResponse response, String id, String workDate, String storyId);

}
