package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.listener.TestCaseListener;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.ResponseUtils;
import cn.master.tsim.util.StreamUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public String allTests(HttpServletRequest request, TestCase testCase, Model model,
                           @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                           @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        final IPage<TestCase> iPage = caseService.pageList(testCase, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/case/case_list?pageCurrent=");
        return "test-case/case_list_2";
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult saveTestCase(HttpServletRequest request) {
        try {
            final TestCase saveCase = caseService.saveCase(request, null);
            return ResponseUtils.success("数据添加成功", saveCase);
        } catch (Exception e) {
            log.error(e.getMessage());
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

    @RequestMapping(value = "loadCaseByProject")
    @ResponseBody
    public ResponseResult loadCaseByProject(HttpServletRequest request) {
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            final String projectId = String.valueOf(params.get("projectId"));
            final int pn = Integer.parseInt(String.valueOf(params.get("pn")));
            final IPage<TestCase> casePage = caseService.pageByProject(projectId, pn, 10);
            request.getSession().setAttribute("planId", String.valueOf(params.get("planId")));
            return ResponseUtils.success("数据查询成功", casePage);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
    }

    @PostMapping(value = "/upload")
    public String uploadCase(MultipartFile file, Model model) {
        ExcelReader reader = null;
        try {
            reader = EasyExcel.read(file.getInputStream(), TestCase.class, new TestCaseListener(caseService)).build();
            reader.read(EasyExcel.readSheet(0).build());
            model.addAttribute("msg", "success");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "failed");
        } finally {
            if (Objects.nonNull(reader)) {
                reader.finish();
            }
        }
        return "redirect:/case/case_list";
    }

    @PostMapping(value = "/queryCase")
    @ResponseBody
    public ResponseResult queryCase(HttpServletRequest request) {
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            final TestCase testCase = caseService.queryCaseById(String.valueOf(params.get("id")));
            return ResponseUtils.success("数据修改成功", testCase);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据修改失败", e.getMessage());
        }
    }
}

