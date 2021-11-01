package cn.master.tsim.controller;


import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.util.ResponseUtils;
import cn.master.tsim.util.StreamUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
     * @param bug   TestBug
     * @param model Model
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String allBug(TestBug bug, Model model, @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                         @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        final IPage<TestBug> iPage = bugService.pageListBug(bug, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("users", Constants.userMaps);
        model.addAttribute("redirecting", "/bug/list?pageCurrent=");
        return "bug/bug_list";
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
        try {
            final TestBug testBug = bugService.addBug(request, bug);
            return ResponseUtils.success("数据添加成功", testBug);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    /**
     * bug详情
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.common.ResponseResult
     */
    @PostMapping("/bugDetail")
    @ResponseBody
    public ResponseResult bugDetail(HttpServletRequest request) {
        try {
            final Map<String, Object> params = StreamUtils.getParamsFromRequest(request);
            final TestBug bug = bugService.getBugById(String.valueOf(params.get("bugId")));
            return ResponseUtils.success("数据添加成功", bug);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }
}

