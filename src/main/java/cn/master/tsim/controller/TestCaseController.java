package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Slf4j
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
    public String allTests(HttpServletRequest request, TestCase testCase, Model model) {
        final List<TestCase> testCases = caseService.listTestCase(testCase, "", "");
        model.addAttribute("tests", testCases);
        model.addAttribute("proMap", projectService.projectMap());
        model.addAttribute("moduleMap", moduleService.moduleMap());
        return "test-case/case_list_2";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseResult saveTestCase(@ModelAttribute @Validated TestCase testCase) {
        try {
            caseService.saveCase(testCase);
            return ResponseUtils.success("数据添加成功");
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updateCaseStatus")
    @ResponseBody
    public ResponseResult updateCaseStatus(@RequestBody String arguments) {
        try {
            caseService.updateCase(arguments.split("=")[1]);
            return ResponseUtils.success("数据修改成功");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据修改失败", e.getMessage());
        }
    }

    @RequestMapping(value = "loadCaseInfo")
    @ResponseBody
    public ResponseResult loadCaseInfo(HttpServletResponse response) {
        try {
            final List<Project> projects = projectService.findByPartialProjectName("");
            for (Project project : projects) {
                final List<Module> modules = moduleService.listModule(project.getId());
                for (Module module : modules) {
                    final List<TestCase> cases = caseService.listTestCase(null, project.getId(), module.getId());
                    module.setCaseList(cases);
                }
                project.setModules(modules);
            }
            return ResponseUtils.success("数据查询成功", projects);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
    }
}

