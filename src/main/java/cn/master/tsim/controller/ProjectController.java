package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(value = "/projectList")
    public String projectList(HttpServletRequest request, Model model, @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                              @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize, Project project) {
        final IPage<Project> iPage = projectService.projectListPages(project, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/project/projectList?pageCurrent=");
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        return "project/project_list";
    }

    @PostMapping(value = "/addProject")
    @ResponseBody
    public ResponseResult addProject(HttpServletRequest request, Project project, Model model) {
        try {
            if (StringUtils.isBlank(project.getProjectName()) && StringUtils.isBlank(project.getWorkDate())) {
                return ResponseUtils.error(400, "数据添加失败", "字段不能为空");
            }
            final Project project1 = projectService.addProject(project, request);
            model.addAttribute("resultMsg", ResponseUtils.success("数据[" + project1.getProjectName() + "]添加成功"));
            return ResponseUtils.success("数据[" + project1.getProjectName() + "]添加成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            model.addAttribute("resultMsg", ResponseUtils.error(400, "数据添加失败", e.getMessage()));
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
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
            final Map<String, String[]> parameterMap = request.getParameterMap();
            final String workDate = parameterMap.get("workDate")[0];
            final Project checkProject = projectService.checkProject(parameterMap.get("projectName")[0], workDate);
            if (Objects.nonNull(checkProject)) {
                for (TestTaskInfo projectTask : checkProject.getProjectTasks()) {
                    if (Objects.equals(projectTask.getIssueDate(), workDate)) {
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
}

