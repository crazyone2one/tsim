package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.service.PlanCaseRefService;
import cn.master.tsim.service.TestPlanService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测试计划表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Slf4j
@Controller
@RequestMapping("/plan")
public class TestPlanController {

    private final TestPlanService planService;
    private final TestStoryService storyService;
    private final PlanCaseRefService planCaseRefService;

    @Autowired
    public TestPlanController(TestPlanService planService,
                              TestStoryService storyService, PlanCaseRefService planCaseRefService) {
        this.planService = planService;
        this.storyService = storyService;
        this.planCaseRefService = planCaseRefService;
    }

    @GetMapping("/list")
    public String allPlans(HttpServletRequest request) {
        return "plan/plan_list";
    }

    @RequestMapping("/loadPlan")
    @ResponseBody
    public Map<String, Object> loadPlan(HttpServletRequest request, @RequestParam(value = "pageNum") Integer offset,
                                        @RequestParam(value = "pageSize") Integer limit) {
        Map<String, Object> map = new HashMap<>(2);
        final IPage<TestPlan> iPage = planService.pageList(request, offset, limit);
        map.put("total", iPage.getTotal());
        map.put("rows", iPage.getRecords());
        return map;
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult savePlan(HttpServletRequest request,
                                   @RequestParam("storyId") String storyId,
                                   @RequestParam("planName") String planName,
                                   @RequestParam("planDesc") String planDesc
    ) {
        try {
            return planService.savePlan(request, storyId, planName, planDesc);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updatePlanStatus")
    @ResponseBody
    public ResponseResult updateStoryStatus(HttpServletRequest request) {
        try {
            final TestPlan testPlan = planService.updatePlan(request.getParameter("id"));
            return ResponseUtils.success("测试计划状态修改成功", testPlan);
        } catch (Exception e) {
            return ResponseUtils.error(400, "测试计划状态修改失败", e.getMessage());
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

    /**
     * 测试计划关联测试用例
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.common.ResponseResult
     */
    @PostMapping(value = "/addRefCase")
    @ResponseBody
    public ResponseResult addRefCase(HttpServletRequest request) {
        try {
            String planId = request.getParameter("planId");
            String caseId = request.getParameter("caseId");
            final List<String> list = JacksonUtils.jsonToObject(caseId, new TypeReference<List<String>>() {
            });
            planCaseRefService.addItemRef(planId, list);
            return ResponseUtils.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtils.error(ResponseCode.BODY_NOT_MATCH.getCode(), "数据添加失败", e.getMessage());
        }
    }
}

