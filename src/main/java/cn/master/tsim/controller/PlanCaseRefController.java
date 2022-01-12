package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.service.PlanCaseRefService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-26
 */
@RestController
@RequestMapping("/planCaseRef")
public class PlanCaseRefController {
    private final PlanCaseRefService refService;

    public PlanCaseRefController(PlanCaseRefService refService) {
        this.refService = refService;
    }

    @GetMapping(value = "/loadRefRecords/{planId}")
    @ResponseBody
    public Map<String, Object> loadRefRecords(HttpServletRequest request, @PathVariable String planId) {
        Map<String, Object> map = new HashMap<>(2);
        try {
            final IPage<PlanCaseRef> iPage = refService.loadRefRecords(request, planId);
            map.put("total", iPage.getTotal());
            map.put("rows", iPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @PostMapping(value = "/addBugByFailCase")
    @ResponseBody
    public ResponseResult saveRefInfo(HttpServletRequest request) {
        try {
            final PlanCaseRef itemRef = refService.addBugByFailCase(request);
            return ResponseUtils.success("数据更新成功", itemRef);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据更新失败", e.getMessage());
        }
    }

    @PostMapping(value = "/batchPass")
    @ResponseBody
    public ResponseResult batchPass(HttpServletRequest request) {
        return refService.batchPass(request);
    }
}

