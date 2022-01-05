package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.listener.TestCaseListener;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import cn.master.tsim.util.StreamUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.*;

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

    @GetMapping("/list")
    public String allTests(Model model) {
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        return "test-case/case_list";
    }

    @RequestMapping("/reloadTable")
    @ResponseBody
    public Map<String, Object> reloadTable(HttpServletRequest request) {
        final IPage<TestCase> iPage = caseService.pageList(request, 0, 0);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<TestCase>());
        return map;
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
    public ResponseResult loadCaseInfo(HttpServletRequest request,HttpServletResponse response) {
        try {
            final List<Project> projects = projectService.findByPartialProjectName("");
            for (Project project : projects) {
                final List<Module> modules = moduleService.listModule(project.getId());
                for (Module module : modules) {
                    final List<TestCase> cases = caseService.listTestCase(request, null, project.getId(), module.getId());
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
            final String planId = String.valueOf(params.get("planId"));
            params.put("pageSize", 10);
            final IPage<TestCase> casePage = caseService.loadCaseByPlan(request, params);
            request.getSession().setAttribute("planId", planId);
            return ResponseUtils.success("数据查询成功", casePage);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据查询错误", e.getMessage());
        }
    }
    @RequestMapping(value = "loadCaseByPlan/{planId}")
    @ResponseBody
    public Map<String, Object> loadCase(HttpServletRequest request, @PathVariable String planId) {
        Map<String, Object> map = new HashMap<>(2);
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            params.put("planId", planId);
            final IPage<TestCase> casePage = caseService.loadCaseByPlan(request, params);
            map.put("total", casePage.getTotal());
            map.put("rows", casePage.getRecords());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    @PostMapping(value = "/upload")
    public String uploadCase(HttpServletRequest request, MultipartFile file, Model model) {
        ExcelReader reader = null;
        try {
            final String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            final String distFileName = request.getServletContext().getRealPath("") + "upload" + File.separator + fileName;
            log.info(distFileName);
            reader = EasyExcel.read(file.getInputStream(), TestCase.class, new TestCaseListener(caseService)).build();
            reader.read(EasyExcel.readSheet(0).build());
            model.addAttribute("msg", "success");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(reader)) {
                reader.finish();
            }
        }
        return "redirect:/case/list";
    }

    @PostMapping(value = "/queryCase")
    @ResponseBody
    public ResponseResult queryCase(HttpServletRequest request) {
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            final TestCase testCase = caseService.queryCaseById(String.valueOf(params.get("id")));
            return ResponseUtils.success("数据查询成功", testCase);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseUtils.error(400, "数据查询失败", e.getMessage());
        }
    }

    /**
     * 测试用例模板导出
     *
     * @param response HttpServletResponse
     * @param fileName fileName
     * @return cn.master.tsim.common.ResponseResult
     */
    @RequestMapping("/download/{fileName:.+}")
    @ResponseBody
    public ResponseResult downloadTemplate(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        final String filePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("export")).getPath() + "/";
        File file = new File(filePath + fileName);
        try {
            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    //unknown mimetype so set the mimetype to application/octet-stream
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                /**
                 * Here we have mentioned it to show inline
                 */
                response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

                //Here we have mentioned it to show as attachment
                //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
                response.setContentLength((int) file.length());
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                return ResponseUtils.success("模板下载成功");
            } else {
                return ResponseUtils.error(ResponseCode.ERROR_404.getCode(), "模板不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtils.error("模板下载失败");
        }
    }
}

