package cn.master.tsim.controller;


import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String allStory(Model model) {
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        return "story/story_list";
    }

    @RequestMapping("/reloadTable")
    @ResponseBody
    public Map<String, Object> reloadTable(HttpServletRequest request, @RequestParam(value = "pageNum") Integer offset,
                                           @RequestParam(value = "pageSize") Integer limit) {
        Map<String, Object> map = new HashMap<>(2);
        final IPage<TestStory> iPage = service.pageList(request, offset, limit);
        map.put("total", iPage.getTotal());
        map.put("rows", CollectionUtils.isNotEmpty(iPage.getRecords()) ? new LinkedList<>(iPage.getRecords()) : new LinkedList<TestStory>());
        return map;
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseResult saveStory(HttpServletRequest request, TestStory story) {
        return service.saveOrUpdateStory(request, story);
    }

    @GetMapping(value = "/uniqueStory")
    @ResponseBody
    public ResponseResult uniqueStory(HttpServletRequest request, TestStory testStory) {
        try {
            final List<TestStory> testStories = service.checkUniqueStory(testStory);
            if (CollectionUtils.isNotEmpty(service.checkUniqueStory(testStory))) {
                final TestStory story = testStories.get(0);
                final String projectName = projectService.getProjectById(story.getProjectId()).getProjectName();
                return ResponseUtils.error(401, projectName + "已关联" + story.getWorkDate() + "月份测试需求:" + story.getDescription(), story);
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
    public ResponseResult upload(HttpServletRequest request, MultipartFile file, Model m) {
        request.setAttribute("docFlag", "story");
        final ResponseResult upload = service.upload(request, file);
        m.addAttribute("docName", JacksonUtils.convertValue(upload.getData(), new TypeReference<Map<String, String>>() {
        }).get("docName"));
        return upload;
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

    @RequestMapping(value = "/queryList/{projectId}")
    @ResponseBody
    public ResponseResult queryListByProject(HttpServletRequest request, @PathVariable String projectId) {
        ResponseResult result = ResponseUtils.success("数据查询成功");
        try {
            final List<TestStory> stories = service.listStoryByProjectId(projectId);
            result.setData(stories);
        } catch (Exception e) {
            ResponseUtils.error("数据查询失败");
        }
        return result;
    }

    @RequestMapping(value = "/getStory/{storyId}")
    @ResponseBody
    public ResponseResult getStory(@PathVariable String storyId) {
        ResponseResult result = ResponseUtils.success("数据查询成功");
        result.setData(service.searchStoryById(storyId));
        return result;
    }

    @RequestMapping("/batchDel")
    @ResponseBody
    public ResponseResult batchDel(HttpServletRequest request) {
        return service.batchDelete(request);
    }
}

