package cn.master.tsim.service;

import cn.master.tsim.entity.Project;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 添加项目
     *
     * @param project project
     */
    void addProject(Project project);

    /**
     * 添加项目
     *
     * @param projectName 项目名称
     * @return int
     */
    Project addProject(String projectName);

    /**
     * 更新项目状态
     *
     * @param projectId projectId
     */
    void updateProjectStatus(String projectId);

    IPage<Project> projectListPages(Project project, Integer pageCurrent, Integer pageSize);
}
