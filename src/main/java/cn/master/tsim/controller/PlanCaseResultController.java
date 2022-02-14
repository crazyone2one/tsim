package cn.master.tsim.controller;


import cn.master.tsim.service.PlanCaseResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/plan-case-result")
public class PlanCaseResultController {

    private final PlanCaseResultService planCaseResultService;

    public PlanCaseResultController(PlanCaseResultService planCaseResultService) {
        this.planCaseResultService = planCaseResultService;
    }

    @PostMapping(value = "/addRef")
    @ResponseBody
    public void saveRefInfo(HttpServletRequest request) throws JsonProcessingException {
        planCaseResultService.updateRefItem(request);
    }
}

