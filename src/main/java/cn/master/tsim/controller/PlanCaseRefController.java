package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.service.PlanCaseRefService;
import cn.master.tsim.util.ResponseUtils;
import cn.master.tsim.util.StreamUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(value = "/loadRefRecords")
    @ResponseBody
    public ResponseResult loadRefRecords(HttpServletRequest request) {
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            final IPage<PlanCaseRef> iPage = refService.loadRefRecords(request, params);
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                return ResponseUtils.success("数据查询成功", iPage);
            } else {
                return ResponseUtils.error("数据查询失败");
            }
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @PostMapping(value = "/saveRefInfo")
    @ResponseBody
    public ResponseResult saveRefInfo(HttpServletRequest request) {
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            final PlanCaseRef itemRef = refService.uploadItemRef(request, params);
            return ResponseUtils.success("数据添加成功", itemRef);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }
}

