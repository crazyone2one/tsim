package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.CaseReview;
import cn.master.tsim.service.CaseReviewService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
@Slf4j
@RestController
@RequestMapping("/case-review")
public class CaseReviewController {

    private final CaseReviewService reviewService;

    public CaseReviewController(CaseReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RequestMapping("/reloadTable")
    @ResponseBody
    public Map<String, Object> reloadTable(HttpServletRequest request, @RequestParam(value = "pageNum") Integer offset,
                                           @RequestParam(value = "pageSize") Integer limit) {
        Map<String, Object> map = new LinkedHashMap<>();
        IPage<CaseReview> iPage = reviewService.dataList(request, offset, limit);
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<CaseReview>());
        return map;
    }

    @PostMapping("save-review")
    @ResponseBody
    public ResponseResult saveReview(HttpServletRequest request) {
        ResponseResult result = ResponseUtils.success();
        try {
            CaseReview caseReview = reviewService.saveOrUpdate(request);
            result.setData(caseReview);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setCode(ResponseUtils.error().getCode());
            result.setMsg("数据添加失败");
        }
        return result;
    }
}

