package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.TestTaskInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 任务汇总表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
public interface TestTaskInfoService extends IService<TestTaskInfo> {

    IPage<TestTaskInfo> taskInfoPage(HttpServletRequest request, Integer pageCurrent, Integer pageSize);

    /**
     * 保存 任务汇总信息
     *
     * @param request
     * @param projectId
     * @param plan
     * @return cn.master.tsim.entity.TestTaskInfo
     */
    TestTaskInfo addItem(HttpServletRequest request, String projectId, TestPlan plan);

    TestTaskInfo saveTaskInfo(HttpServletRequest request, String projectId, String description, String workDate);

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

    TestTaskInfo queryItem(String projectId, String storyId, String planId);

    /**
     * 更新任务数据
     *
     * @param request@return cn.master.tsim.common.ResponseResult
     */
    ResponseResult updateTaskInfo(HttpServletRequest request);

    /**
     * description: 导出数据 <br>
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param taskInfo TestTaskInfo
     * @throws IOException io
     * @author 11's papa
     */
    void exportTaskInfo(HttpServletRequest request, HttpServletResponse response, TestTaskInfo taskInfo) throws IOException;

    /**
     * description: 任务数据导出前检查 <br>
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */
    ResponseResult checkTaskInfoData(HttpServletRequest request);
}
