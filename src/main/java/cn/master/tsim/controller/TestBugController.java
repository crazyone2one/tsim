package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final ProjectService projectService;
    private final ModuleService moduleService;

    @Autowired
    public TestBugController(TestBugService bugService, ProjectService projectService, ModuleService moduleService) {
        this.bugService = bugService;
        this.projectService = projectService;
        this.moduleService = moduleService;
    }

    /**
     * 列表
     *
     * @param bug
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String allBug(TestBug bug, Model model,@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                         @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        final List<TestBug> bugList = bugService.listAllBug(bug);
        model.addAttribute("lists", bugList);
        model.addAttribute("proMap", projectService.projectMap());
        model.addAttribute("moduleMap", moduleService.moduleMap());
        final IPage<TestBug> iPage = bugService.pageListBug(bug, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/bug/list?pageCurrent=");
        return "bug/bug_list";
    }

    /**
     * 保存bug
     *
     * @param bug
     * @return cn.master.tsim.common.ResponseResult
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseResult saveTestCase(@ModelAttribute @Validated TestBug bug) {
        try {
            final TestBug testBug = bugService.addBug(bug);
            return ResponseUtils.success("数据添加成功", testBug);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }
}

