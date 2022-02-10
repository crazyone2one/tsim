package cn.master.tsim.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("save-ref")
    @ResponseBody
    public String saveRef(HttpServletRequest request) {
        return "";
    }
}

