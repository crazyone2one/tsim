package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 需求表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Controller
@RequestMapping("/story")
public class TestStoryController {

    private final TestStoryService service;
    private final ProjectService projectService;

    @Autowired
    public TestStoryController(TestStoryService service, ProjectService projectService) {
        this.service = service;
        this.projectService = projectService;
    }

    /**
     * 列表
     *
     * @param story
     * @param model
     * @param pageCurrent
     * @param pageSize
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String allStory(TestStory story, Model model,
                           @RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                           @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        final IPage<TestStory> iPage = service.pageList(story, pageCurrent, pageSize);
        final List<TestStory> pageRecords = iPage.getRecords();
        pageRecords.forEach(temp->{
            temp.setProjectId(projectService.getProjectById(temp.getProjectId()).getProjectName());
        });
        model.addAttribute("records", pageRecords);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/story/list?pageCurrent=");
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        model.addAttribute("projects", projectService.findByPartialProjectName(""));
        return "story/story_list";
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult saveStory(HttpServletRequest request,TestStory story) {
        try {
            final TestStory testStory = service.saveStory(request, story);
            return ResponseUtils.success("数据添加成功", testStory);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updateStoryStatus")
    @ResponseBody
    public ResponseResult updateStoryStatus(@RequestBody String source) {
        try {
            final TestStory story = service.updateStory(source.split("=")[1]);
            return ResponseUtils.success("数据修改成功", story);
        }catch (Exception e) {
            return ResponseUtils.error(400, "数据修改失败", e.getMessage());
        }
    }
}

