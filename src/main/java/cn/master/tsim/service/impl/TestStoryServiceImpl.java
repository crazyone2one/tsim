package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.DocInfo;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.mapper.TestStoryMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 需求表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Service
public class TestStoryServiceImpl extends ServiceImpl<TestStoryMapper, TestStory> implements TestStoryService {
    @Autowired
    private ProjectService projectService;
    @Autowired
    SystemService systemService;
    @Autowired
    TestTaskInfoService taskInfoService;
    private final DocInfoService docInfoService;

    @Autowired
    public TestStoryServiceImpl(DocInfoService docInfoService) {
        this.docInfoService = docInfoService;
    }

    @Override
    public IPage<TestStory> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
        List<String> tempProp = new LinkedList<>();
        // 按照项目模糊查询
        final String projectName = request.getParameter("projectName");
        wrapper.lambda().inSql(StringUtils.isNotBlank(projectName),TestStory::getProjectId, "select id from t_project where project_name like '%" + projectName + "%'");
//        按照需求内容模糊查询
        final String storyDesc = request.getParameter("storyDesc");
        wrapper.lambda().like(StringUtils.isNotBlank(storyDesc),TestStory::getDescription, storyDesc);
//        按照需求完成状态查询
        String status = request.getParameter("status");
        wrapper.lambda().eq(StringUtils.isNotBlank(status),TestStory::getStoryStatus, status);
        wrapper.lambda().eq(TestStory::getDelFlag,0).orderByAsc(TestStory::getStoryStatus);
        Page<TestStory> storyPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        storyPage.getRecords().forEach(t -> {
            if (StringUtils.isNotBlank(t.getDocId())) {
                t.setDocInfo(docInfoService.queryDocById(t.getDocId()));
            }
            t.setProject(projectService.getProjectById(t.getProjectId()));
        });
        return storyPage;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResponseResult saveOrUpdateStory(HttpServletRequest request, TestStory story) {
        if (StringUtils.isNotBlank(story.getId())) {
            try {
                final TestStory testStory = searchStoryById(story.getId());
                testStory.setDescription(story.getDescription());
                testStory.setUpdateDate(new Date());
                testStory.setWorkDate(story.getWorkDate());
                testStory.setStoryStatus(story.getStoryStatus());
                baseMapper.updateById(testStory);
                final TestTaskInfo itemByProject = taskInfoService.queryItem(story.getProjectId(), story.getId());
                itemByProject.setUpdateDate(new Date());
                itemByProject.setIssueDate(testStory.getWorkDate());
                itemByProject.setSummaryDesc(testStory.getDescription());
//                itemByProject.setFinishStatus(String.valueOf(testStory.getStoryStatus()));
                taskInfoService.updateTask(request, itemByProject);
                return ResponseUtils.success("数据更新成功", testStory);
            } catch (Exception e) {
                return ResponseUtils.error(400, "数据更新失败", e.getCause().getMessage());
            }
        }
        try {
            TestStory build = TestStory.builder().projectId(story.getProjectId())
                    .description(story.getDescription()).workDate(story.getWorkDate()).storyStatus(story.getStoryStatus())
                    .docId(story.getDocId()).delFlag(0).createDate(new Date()).build();
            baseMapper.insert(build);
            taskInfoService.addItem(request, story.getProjectId(), build);
            return ResponseUtils.success("数据添加成功", build);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getCause().getMessage());
        }
    }

    @Override
    public TestStory getStory(String description, String workDate, String proId) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
//        完全匹配
        wrapper.lambda().eq(StringUtils.isNotBlank(description), TestStory::getDescription, description);
        wrapper.lambda().eq(StringUtils.isNotBlank(workDate), TestStory::getWorkDate, workDate);
        // 验证所属项目
        wrapper.lambda().eq(StringUtils.isNotBlank(proId), TestStory::getProjectId, proId);
        //        if (Objects.nonNull(story)) {
//            story.setProject(projectService.getProjectById(story.getProjectId()));
//        }
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<TestStory> checkUniqueStory(TestStory story) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
//        完全匹配
        wrapper.lambda().eq(StringUtils.isNotBlank(story.getDescription()), TestStory::getDescription, story.getDescription());
        wrapper.lambda().eq(StringUtils.isNotBlank(story.getWorkDate()), TestStory::getWorkDate, story.getWorkDate());
        // 验证所属项目
        wrapper.lambda().eq(StringUtils.isNotBlank(story.getProjectId()), TestStory::getProjectId, story.getProjectId());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public TestStory searchStoryById(String storyId) {
        final TestStory story = baseMapper.selectById(storyId);
        if (Objects.nonNull(story)) {
            story.setProject(projectService.getProjectById(story.getProjectId()));
        }
        return story;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public TestStory updateStory(String storyId) {
        final TestStory story = baseMapper.selectById(storyId);
        story.setDelFlag(Objects.equals(story.getDelFlag(), 0) ? 1 : 0);
        story.setUpdateDate(new Date());
        baseMapper.updateById(story);
        return story;
    }

    @Override
    public List<TestStory> listStoryByProjectId(String projectId) {
        return baseMapper.selectList(new QueryWrapper<TestStory>().lambda().eq(TestStory::getProjectId, projectId));
    }

    @Override
    public List<TestStory> listStory() {
        return baseMapper.selectList(new QueryWrapper<TestStory>().lambda().eq(TestStory::getDelFlag, "0"));
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResponseResult upload(HttpServletRequest request, MultipartFile file) {
        try {
            ResponseResult result = systemService.storeFile(request, file);
            if (Objects.equals(ResponseCode.SUCCESS.getCode(), result.getCode())) {
                Map<String, String> map = JacksonUtils.convertValue(result.getData(), new TypeReference<Map<String, String>>() {
                });
                final String[] docNames = map.get("docName").split("\\.");
                DocInfo docInfo = docInfoService.saveDocInfo(request, map);
                Map<String, String> tempMap = new LinkedHashMap<>();
                tempMap.put("docId", docInfo.getId());
                tempMap.put("docName", docNames[0]);
                result.setData(tempMap);
            }
            return result;
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResponseResult batchDelete(HttpServletRequest request) {
        try {
            List<String> ids = JacksonUtils.convertValue(request.getParameter("ids"), new TypeReference<List<String>>() {
            });
            ids.forEach(t->{
                TestStory story = getById(t);
                story.setDelFlag(1);
                story.setUpdateDate(new Date());
                updateById(story);
                TestTaskInfo testTaskInfo = taskInfoService.queryItem(story.getProjectId(), story.getId());
                testTaskInfo.setDelFlag(1);
                taskInfoService.updateById(testTaskInfo);
            });
            return ResponseUtils.success("删除成功");
        }
        catch (Exception e) {
            return ResponseUtils.error("删除失败");
        }
    }

    @Override
    public ResponseResult downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String uuidName) {
        try {
            systemService.downloadFile(request, response, fileName, uuidName);
//            response.sendRedirect("/story/list");
            return ResponseUtils.success("文件下载成功");
        } catch (Exception e) {
            return ResponseUtils.error("文件下载失败");
        }
    }
}
