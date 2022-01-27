package cn.master.tsim.controller;


import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.service.TestTaskInfoService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
    public String taskList(HttpServletRequest request, Model model) {
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
    public ResponseResult editTask(HttpServletRequest request) {
        return taskInfoService.updateTaskInfo(request);
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
    @ResponseBody
    public Map<String, Object> reloadTable(HttpServletRequest request,
                                           @RequestParam(value = "pageNum") Integer offset,
                                           @RequestParam(value = "pageSize") Integer limit) {
        String projectName = request.getParameter("projectName");
        final IPage<TestTaskInfo> iPage = taskInfoService.taskInfoPage(request, offset, limit);
        Map<String, Object> map = new HashMap<>(2);
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<TestTaskInfo>());
        return map;
    }
}

