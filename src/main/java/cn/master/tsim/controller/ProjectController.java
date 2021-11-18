package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 加载列表数据
     *
     * @param request     HttpServletRequest
     * @param model       Model
     * @param pageCurrent pageCurrent
     * @param pageSize    pageSize
     * @param project     Project
     * @return java.lang.String
     */
    @GetMapping(value = "/list")
    public String projectList(HttpServletRequest request, Model model, @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Project project) {
        final IPage<Project> iPage = projectService.projectListPages(project, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/project/list?pageCurrent=");
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        return "project/project_list";
    }

    /**
     * 添加项目数据
     *
     * @param request HttpServletRequest
     * @return java.lang.String
     */
    @PostMapping(value = "/addProject")
    @ResponseBody
    public ResponseResult addProject(HttpServletRequest request) {
        try {
            final String name = request.getParameter("name");
            final String date = request.getParameter("date");
            Map<String, String> infoMap = new LinkedHashMap<>();
            infoMap.put("name", name);
            infoMap.put("date", date);
            final Project project1 = projectService.addProject(request, infoMap);
            return ResponseUtils.success(project1);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
    }

    /**
     * 验证项目是否已存在
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.common.ResponseResult
     */
    @GetMapping(value = "/checkProject")
    @ResponseBody
    public ResponseResult checkProject(HttpServletRequest request) {
        final ResponseResult success = ResponseUtils.success("数据查询成功");
        try {
            final String name = request.getParameter("name");
            final String date = request.getParameter("date");
            final Project checkProject = projectService.checkProject(name, date);
            if (Objects.nonNull(checkProject)) {
                for (TestTaskInfo projectTask : checkProject.getProjectTasks()) {
                    if (Objects.equals(projectTask.getIssueDate(), date)) {
                        return ResponseUtils.error(400, "该月份已存在项目", checkProject);
                    }
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
        return success;
    }

    @RequestMapping(value = "/generateReport/{id}/{workDate}")
    @ResponseBody
    public ResponseResult generateReport(HttpServletRequest request, HttpServletResponse response, Model model,
                                         @PathVariable("id") String id,
                                         @PathVariable("workDate") String workDate) {
        ResponseResult result = ResponseUtils.success("报告生成成功");
        try {
            final ResponseResult responseResult = projectService.generateReport(request, response, id, workDate);
            model.addAttribute("success", true);
        } catch (Exception e) {
            result = ResponseUtils.error("报告生成失败");
            model.addAttribute("fail", true);
        }
        return result;
    }
}

