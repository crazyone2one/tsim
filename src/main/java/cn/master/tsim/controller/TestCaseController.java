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
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
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
    public Map<String, Object> reloadTable(HttpServletRequest request, @RequestParam(value = "pageNum") Integer offset,
                                           @RequestParam(value = "pageSize") Integer limit) {
        final IPage<TestCase> iPage = caseService.pageList(request, offset, limit);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<TestCase>());
        return map;
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult saveTestCase(HttpServletRequest request) {
        try {
            return caseService.saveCase(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @RequestMapping(value = "loadCaseInfo")
    @ResponseBody
    public ResponseResult loadCaseInfo(HttpServletRequest request, HttpServletResponse response) {
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
            final IPage<TestCase> casePage = caseService.loadCaseByPlan(request, planId);
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
            final IPage<TestCase> casePage = caseService.loadCaseByPlan(request, planId);
            map.put("total", casePage.getTotal());
            map.put("rows", casePage.getRecords());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseResult uploadCase(HttpServletRequest request, MultipartFile file) {
        try {
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), TestCase.class, new TestCaseListener(caseService));
            return ResponseUtils.success("数据导入成功", read.sheet().doReadSync());
        } catch (ExcelAnalysisException exception) {
            return ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(), "数据导入失败", exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.error("数据导入失败: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/queryCase/{id}")
    @ResponseBody
    public ResponseResult queryCase(HttpServletRequest request, @PathVariable String id) {
        try {
            final TestCase testCase = caseService.queryCaseById(id);
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
    @RequestMapping("/downloadTemplate/{fileName:.+}")
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
                /*
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

    @PostMapping("/disable")
    @ResponseBody
    public ResponseResult disableCase(HttpServletRequest request) {
        try {
            caseService.disableCase(request);
            return ResponseUtils.success("数据更新成功");
        } catch (Exception e) {
            return ResponseUtils.error("数据更新失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult deleteCase(HttpServletRequest request) {
        try {
            caseService.deleteCase(request);
            return ResponseUtils.success("数据删除成功");
        } catch (Exception e) {
            return ResponseUtils.error("数据删除失败");
        }
    }

    @RequestMapping("checkCaseData")
    @ResponseBody
    public ResponseResult checkCaseData(HttpServletRequest request) {
        return caseService.checkCaseData(request);
    }

    /**
     * description: 导出测试用例数据 <br>
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param testCase 查询条件
     * @author 11's papa
     */
    @RequestMapping("/exportCase")
    public void exportCase(HttpServletRequest request, HttpServletResponse response, TestCase testCase) {
        try {
            caseService.exportCaseInfo(request, response, testCase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

