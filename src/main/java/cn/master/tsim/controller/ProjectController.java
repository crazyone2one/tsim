package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
     * @param model Model
     * @return java.lang.String
     */
    @GetMapping(value = "/list")
    public String projectList(Model model) {
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        return "project/project_list";
    }

    @RequestMapping("/loadProject")
    @ResponseBody
    public Map<String, Object> loadProject(HttpServletRequest request,
                                           @RequestParam(value = "pageNum") Integer offset,
                                           @RequestParam(value = "pageSize") Integer limit) {
        final String projectName = request.getParameter("projectName");
        Map<String, Object> map = new HashMap<>(2);
        final IPage<Project> iPage = projectService.projectListPages(null, projectName, offset, limit);
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<Project>());
        return map;
    }

    /**
     * 添加项目数据
     *
     * @return java.lang.String
     */
    @PostMapping(value = "/addProject")
    @ResponseBody
    public ResponseResult addProject(@RequestParam("name") String proName) {
        return projectService.saveProject(proName);
    }

    /**
     * 验证项目是否已存在
     *
     * @return cn.master.tsim.common.ResponseResult
     */
    @GetMapping(value = "/checkProject/{name}")
    @ResponseBody
    public ResponseResult checkProject(@PathVariable String name) {
        try {
            if (StringUtils.isBlank(name)) {
                return ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(), ResponseCode.PARAMS_ERROR.getMessage());
            }
            final Project checkProject = projectService.getProjectByName(name);
            if (Objects.nonNull(checkProject)) {
                return ResponseUtils.error(400, "[" + name + "]已存在项目", checkProject);
            }
            return ResponseUtils.success(name);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
    }

    @GetMapping(value = "/checkUniqueProject")
    @ResponseBody
    public ResponseResult checkUnique(HttpServletRequest request) {
        try {
            Project project = projectService.queryProject(request);
            if (Objects.nonNull(project)) {
                return ResponseUtils.success();
            }
            return ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(),ResponseCode.PARAMS_ERROR.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
    }

    @PostMapping(value = "/del/{id}")
    @ResponseBody
    public ResponseResult deleteProject(@PathVariable String id) {
        try {
            return ResponseUtils.success("数据更新成功", projectService.deleteProject(id));
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据更新失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updateStatus/{id}")
    @ResponseBody
    public ResponseResult updateStatus(HttpServletRequest request,@PathVariable String id) {
        try {
            projectService.updateProjectStatus(id);
            return ResponseUtils.success("数据更新成功");
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据更新失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/generateReport/{id}/{storyId}/{workDate}")
    @ResponseBody
    public ResponseResult generateReport(HttpServletRequest request, HttpServletResponse response, Model model,
                                         @PathVariable("id") String id,
                                         @PathVariable("workDate") String workDate, @PathVariable String storyId) {
        ResponseResult result = ResponseUtils.success("报告生成成功");
        try {
            final ResponseResult responseResult = projectService.generateReport(request, response, id, workDate, storyId);
            model.addAttribute("success", true);
            response.sendRedirect("/project/list");
        } catch (Exception e) {
            result = ResponseUtils.error("报告生成失败");
            model.addAttribute("fail", true);
        }
        return result;
    }

    @RequestMapping(value = "/queryList")
    @ResponseBody
    public ResponseResult queryList(HttpServletRequest request) {
        ResponseResult result = ResponseUtils.success("数据查询成功");
        try {
            final List<Project> projects = projectService.findByPartialProjectName("");
            result.setData(projects);
        } catch (Exception e) {
            ResponseUtils.error("数据查询失败");
        }
        return result;
    }
}

