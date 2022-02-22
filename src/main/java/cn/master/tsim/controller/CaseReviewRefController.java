package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.CaseReviewRef;
import cn.master.tsim.service.CaseReviewRefService;
import cn.master.tsim.util.ResponseUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/case-review-ref")
public class CaseReviewRefController {
    private final CaseReviewRefService refService;

    public CaseReviewRefController(CaseReviewRefService refService) {
        this.refService = refService;
    }

    @RequestMapping("save-ref")
    @ResponseBody
    public ResponseResult saveRef(HttpServletRequest request) {
        refService.saveRef(request);
        return ResponseUtils.success("数据添加成功");
    }

    @RequestMapping("/getRefCases")
    @ResponseBody
    public ResponseResult getRefCases(HttpServletRequest request, Model model) {
        List<CaseReviewRef> reviewRefs = refService.listRef(request);
        model.addAttribute("refs", reviewRefs);
        return ResponseUtils.success(reviewRefs);
    }
}

