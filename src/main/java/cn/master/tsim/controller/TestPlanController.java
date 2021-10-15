package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestPlanService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 测试计划表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Controller
@RequestMapping("/plan")
public class TestPlanController {

    private final TestPlanService planService;
    private final ProjectService projectService;
    private final TestStoryService storyService;

    @Autowired
    public TestPlanController(TestPlanService planService, ProjectService projectService, TestStoryService storyService) {
        this.planService = planService;
        this.projectService = projectService;
        this.storyService = storyService;
    }

    @GetMapping("/list")
    public String allPlans(TestPlan plan, Model model,
                           @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                           @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        final IPage<TestPlan> iPage = planService.pageList(plan, pageCurrent, pageSize);
        iPage.getRecords().forEach(t -> {
            final Project project = projectService.getProjectById(t.getProjectId());
            t.setProject(project);
            t.setStory(storyService.searchStoryById(t.getStoryId()));
        });
        model.addAttribute("iPage", iPage);
        model.addAttribute("projects", projectService.projectMap());
        model.addAttribute("storyList", storyService.listStory());
        model.addAttribute("redirecting", "/plan/list?pageCurrent=");
        return "plan/plan_list";
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult savePlan(HttpServletRequest request,TestPlan plan) {
        try {
            final TestPlan testPlan = planService.savePlan(request,plan);
            return ResponseUtils.success("数据添加成功", testPlan);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updatePlanStatus")
    @ResponseBody
    public ResponseResult updateStoryStatus(@RequestBody String source) {
        try {
            final TestPlan testPlan = planService.updatePlan(source.split("=")[1]);
            return ResponseUtils.success("数据修改成功", testPlan);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据修改失败", e.getMessage());
        }
    }

    /**
     * 根据项目查询相关需求
     *
     * @param projectId 项目名称
     * @return cn.master.tsim.common.ResponseResult
     */
    @RequestMapping(value = "/getStoryMapByProject")
    @ResponseBody
    public ResponseResult getStoryMapByProject(@RequestParam String projectId) {
        try {
            final List<TestStory> stories = storyService.listStoryByProjectId(projectId);
            return ResponseUtils.success(stories);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }
}

