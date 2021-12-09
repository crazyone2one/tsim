package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestStory;
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
     * @param request
     * @param projectId
     * @param story
     * @return cn.master.tsim.entity.TestTaskInfo
     */
    TestTaskInfo addItem(HttpServletRequest request, String projectId, TestStory story);

    TestTaskInfo saveTaskInfo(HttpServletRequest request, String projectId, String description, String workDate);

    /**
     * 获取单个任务汇总信息
     *
     * @param request  HttpServletRequest
     * @param projectId  Project
     * @param story
     * @return cn.master.tsim.entity.TestTaskInfo
     */

    TestTaskInfo getItemByProject(HttpServletRequest request, String projectId, TestStory story);

    /**
     * 更新任务完成状态
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.entity.TestTaskInfo
     */
    TestTaskInfo updateItemStatue(HttpServletRequest request);

    void updateTask(HttpServletRequest request, TestTaskInfo taskInfo);

    TestTaskInfo checkReportDoc(String projectId, String storyId, String workDate);

    /**
     * 查询任务数据
     *
     * @param id 任务id
     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */

    ResponseResult getTask(String id);
    ResponseResult checkTaskReport(String id);

    TestTaskInfo queryItem(String projectId, String storyId);
}
