package cn.master.tsim.controller;


import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <p>
 * 问题单(bug) 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-28
 */
@Controller
@RequestMapping("/bug")
public class TestBugController {

    private final TestBugService bugService;

    @Autowired
    public TestBugController(TestBugService bugService) {
        this.bugService = bugService;
    }

    /**
     * 列表
     *
     * @param model Model
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String allBug(Model model) {
        model.addAttribute("users", Constants.userMaps);
        return "bug/bug_list";
    }

    @RequestMapping("/reloadTable")
    @ResponseBody
    public Map<String, Object> reloadTable(HttpServletRequest request, @RequestParam(value = "pageNum") Integer offset,
                                           @RequestParam(value = "pageSize") Integer limit) {
        final IPage<TestBug> iPage = bugService.pageListBug(request, offset, limit);
        Map<String, Object> map = new HashMap<>(2);
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<TestBug>());
        return map;
    }

    /**
     * 保存bug
     *
     * @param bug TestBug
     * @return cn.master.tsim.common.ResponseResult
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseResult saveTestCase(HttpServletRequest request, @ModelAttribute @Validated TestBug bug) {
        String successMsg = StringUtils.isNotBlank(bug.getId()) ? "数据更新成功" : "数据添加成功";
        String errorMsg = StringUtils.isNotBlank(bug.getId()) ? "数据更新失败" : "数据添加失败";
        try {
            final TestBug testBug = bugService.saveOrUpdateBug(request, bug);
            return ResponseUtils.success(successMsg, testBug);
        } catch (Exception e) {
            return ResponseUtils.error(400, errorMsg, e.getMessage());
        }
    }

    /**
     * bug详情
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.common.ResponseResult
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public ResponseResult bugDetail(HttpServletRequest request, @PathVariable String id) {
        return bugService.getBugById(id);
    }

    @RequestMapping("/batchDelete")
    @ResponseBody
    public ResponseResult delete(HttpServletRequest request) {
        return bugService.batchDelete(request);
    }
}

