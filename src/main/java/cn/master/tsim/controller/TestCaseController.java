package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Controller
@RequestMapping("/case")
public class TestCaseController {
    private final ProjectService projectService;
    private final ModuleService moduleService;
    private final TestCaseService caseService;

    @Autowired
    public TestCaseController(ProjectService projectService, ModuleService moduleService, TestCaseService caseService) {
        this.projectService = projectService;
        this.moduleService = moduleService;
        this.caseService = caseService;
    }

    @GetMapping("/case_list")
    public String allTests(Model model) {
        final List<TestCase> testCases = caseService.listTestCase("", "");
        model.addAttribute("tests", testCases);
        model.addAttribute("proMap", projectService.projectMap());
        model.addAttribute("moduleMap", moduleService.moduleMap());
        return "test-case/case_list";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseResult saveTestCase(@ModelAttribute @Validated TestCase testCase) {
        try {
            caseService.saveCase(testCase);
            return ResponseUtils.success("数据添加成功");
        } catch (Exception e) {
            return ResponseUtils.error(400,"数据添加失败", e.getMessage());
        }
    }
}

