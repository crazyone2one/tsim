package cn.master.tsim.controller;


import cn.master.tsim.entity.TestCaseSteps;
import cn.master.tsim.service.TestCaseStepsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Controller
@RequestMapping("/testcaseSteps")
public class TestCaseStepsController {

    private final TestCaseStepsService service;

    public TestCaseStepsController(TestCaseStepsService service) {
        this.service = service;
    }

    @RequestMapping("/step4execute")
    @ResponseBody
    public Map<String, Object> reloadTable(HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<TestCaseSteps> caseSteps = service.listSteps(request.getParameter("caseId"));
        map.put("total", caseSteps.size());
        map.put("rows", caseSteps);
        return map;
    }
}

