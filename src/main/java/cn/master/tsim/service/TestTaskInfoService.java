package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestTaskInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 任务汇总表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
public interface TestTaskInfoService extends IService<TestTaskInfo> {

    IPage<TestTaskInfo> taskInfoPage(TestTaskInfo taskInfo, Integer pageCurrent, Integer pageSize);

    /**
     * 保存 任务汇总信息
     *
     * @param project
     * @param request
     * @param workDate
     * @return cn.master.tsim.entity.TestTaskInfo
     */
    TestTaskInfo addItem(Project project, HttpServletRequest request, String workDate);

    /**
     * 获取单个任务汇总信息
     *
     * @param request  HttpServletRequest
     * @param project  Project
     * @param workDate workDate
     * @return cn.master.tsim.entity.TestTaskInfo
     */

    TestTaskInfo getItemByProject(HttpServletRequest request, Project project, String workDate);

    /**
     * 更新任务完成状态
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.entity.TestTaskInfo
     */
    TestTaskInfo updateItemStatue(HttpServletRequest request);

    void updateTask(HttpServletRequest request, TestTaskInfo taskInfo);

    boolean checkReportDoc(String projectId, String workDate);

    /**
     * 查询任务数据
     *
     * @param id 任务id
     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */

    ResponseResult getTask(String id);
}
