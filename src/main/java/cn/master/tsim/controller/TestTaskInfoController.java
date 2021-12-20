package cn.master.tsim.controller;


import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.service.TestTaskInfoService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 任务汇总表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
@Slf4j
@Controller
@RequestMapping("/task")
public class TestTaskInfoController {
    private final TestTaskInfoService taskInfoService;

    @Autowired
    public TestTaskInfoController(TestTaskInfoService taskInfoService) {
        this.taskInfoService = taskInfoService;
    }

    @GetMapping("/list")
    public String taskList(TestTaskInfo taskInfo, Model model, @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        final IPage<TestTaskInfo> iPage = taskInfoService.taskInfoPage(taskInfo, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/task/list?pageCurrent=");
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        model.addAttribute("users", Constants.userMaps);
        return "task/task_info";
    }

    /**
     * 更新项目状态
     *
     * @param request 请求参数
     * @return cn.master.tsim.common.ResponseResult
     */
    @PostMapping(value = "/updateProject")
    @ResponseBody
    public ResponseResult updateProject(HttpServletRequest request) {
        try {
            final TestTaskInfo taskInfo = taskInfoService.updateItemStatue(request);
            return ResponseUtils.success("项目状态修改成功", taskInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "项目状态修改失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/editTask")
    @ResponseBody
    public ResponseResult editTask(@RequestParam("id") String taskId,
                                   @RequestParam("finishStatus") String finishStatus,
                                   @RequestParam("deliveryStatus") String deliveryStatus,
                                   @RequestParam("remark") String remark) {
        return taskInfoService.updateTaskInfo(taskId, finishStatus, deliveryStatus, remark);
    }

    @RequestMapping(value = "/getTask/{id}")
    @ResponseBody
    public ResponseResult getTask(HttpServletRequest request, @PathVariable String id) {
        return taskInfoService.getTask(id);
    }

    @RequestMapping(value = "/checkReport/{id}")
    @ResponseBody
    public ResponseResult generateReport(HttpServletRequest request, @PathVariable("id") String id) {
        return taskInfoService.checkTaskReport(id);
    }

    @PostMapping(value = "/addTask")
    @ResponseBody
    public ResponseResult addTask(HttpServletRequest request, @RequestParam("pro") String proId,
                                  @RequestParam("desc") String description, @RequestParam("workDate") String workDate) {
        try {
            final TestTaskInfo taskInfo = taskInfoService.saveTaskInfo(request, proId, description, workDate);
            return ResponseUtils.success("数据添加成功", taskInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @RequestMapping("/reloadTable")
    public String reloadTable(Model model, TestTaskInfo taskInfo) {
        final IPage<TestTaskInfo> iPage = taskInfoService.taskInfoPage(taskInfo, 0, 0);
        model.addAttribute("iPage", iPage);
        return "task/task_info :: table_refresh";
    }
}

