package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
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
import java.util.List;

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
        final List<Project> records = iPage.getRecords();
        model.addAttribute("records", records);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/project/projectList?pageCurrent=");
        model.addAttribute("ref", projectService.refMap());
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        return "project/project_list";
    }

    @PostMapping(value = "/addProject")
    @ResponseBody
    public ResponseResult addProject(Project project, Model model) {
        try {
            projectService.addProject(project);
            model.addAttribute("resultMsg", ResponseUtils.success("数据添加成功"));
            return ResponseUtils.success("数据添加成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            model.addAttribute("resultMsg", ResponseUtils.error(400, "数据添加失败", e.getMessage()));
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updateProject")
    @ResponseBody
    public ResponseResult updateProject(@RequestBody String source) {
        try {
            projectService.updateProjectStatus(source.split("=")[1]);
            return ResponseUtils.success("数据修改成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据修改失败", e.getMessage());
        }
    }
}

