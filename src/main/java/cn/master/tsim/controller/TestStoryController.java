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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

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
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        final IPage<TestStory> iPage = service.pageList(story, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/story/list?pageCurrent=");
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        model.addAttribute("projects", projectService.findByPartialProjectName(""));
        return "story/story_list";
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult saveStory(HttpServletRequest request) {
        try {
            final TestStory testStory = service.saveStory(request);
            return ResponseUtils.success("数据添加成功", testStory);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getCause().getMessage());
        }
    }

    @GetMapping(value = "/uniqueStory")
    @ResponseBody
    public ResponseResult uniqueStory(HttpServletRequest request) {
        try {
            final TestStory story = service.getStory(request.getParameter("description"), request.getParameter("date"), "");
            if (Objects.nonNull(story)) {
                return ResponseUtils.error(401, "存在相同测试需求", story);
            }
            return ResponseUtils.success();
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据查询失败", e.getMessage());
        }
    }

    @PostMapping(value = "/updateStoryStatus")
    @ResponseBody
    public ResponseResult updateStoryStatus(HttpServletRequest request) {
        try {
            final TestStory story = service.updateStory(request.getParameter("id"));
            return ResponseUtils.success("需求状态修改成功", story);
        } catch (Exception e) {
            return ResponseUtils.error(400, "需求状态修改失败", e.getMessage());
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public ResponseResult upload(HttpServletRequest request, MultipartFile file) {
        request.setAttribute("docFlag","story");
        return service.upload(request, file);
    }

    /**
     * 下载文件
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param fileName 文件名称
     * @param uuidName uuid名称
     * @return cn.master.tsim.common.ResponseResult
     */
    @RequestMapping("/download/{fileName}/{uuidName}")
    @ResponseBody
    public ResponseResult download(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable String fileName, @PathVariable String uuidName) {
        return service.downloadFile(request, response, fileName, uuidName);
    }

    @RequestMapping(value = "/queryList")
    @ResponseBody
    public ResponseResult queryList(HttpServletRequest request) {
        ResponseResult result = ResponseUtils.success("数据查询成功");
        try {
            final List<TestStory> stories = service.listStory();
            result.setData(stories);
        } catch (Exception e) {
            ResponseUtils.error("数据查询失败");
        }
        return result;
    }
}

