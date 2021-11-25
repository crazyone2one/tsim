package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.DocInfo;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.mapper.TestStoryMapper;
import cn.master.tsim.service.DocInfoService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.SystemService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import cn.master.tsim.util.StreamUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    private  ProjectService projectService;
    @Autowired
     SystemService systemService;
    private final DocInfoService docInfoService;
    @Autowired
    public TestStoryServiceImpl(DocInfoService docInfoService) {
        this.docInfoService = docInfoService;
    }

    @Override
    public IPage<TestStory> pageList(TestStory story, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
        List<String> tempProp = new LinkedList<>();
        // 按照项目模糊查询
        if (StringUtils.isNotBlank(story.getProjectId())) {
            projectService.findByPartialProjectName(story.getProjectId()).forEach(temp -> tempProp.add(temp.getId()));
            wrapper.lambda().in(TestStory::getProjectId, tempProp);
        }
//        按照需求内容模糊查询
        if (StringUtils.isNotBlank(story.getDescription())) {
            wrapper.lambda().like(TestStory::getDescription, story.getDescription());
        }
//        按照需求完成状态查询
        if (Objects.nonNull(story.getDelFlag())) {
            wrapper.lambda().eq(TestStory::getDelFlag, story.getDelFlag());
        }
        wrapper.lambda().orderByAsc(TestStory::getDelFlag);
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }

    @Override
    public TestStory saveStory(HttpServletRequest request) {
        final Map<String, Object> objectMap = StreamUtils.getParamsFromRequest(request);
        final String description = String.valueOf(objectMap.get("description"));
        final String date = String.valueOf(objectMap.get("date"));
        final Project project = projectService.getProjectByName(String.valueOf(objectMap.get("name")));
        final TestStory testStory = getStory(description, date, project.getId());
        if (Objects.nonNull(testStory)) {
            return testStory;
        }
        String doc = String.valueOf(objectMap.get("doc"));
        TestStory build = TestStory.builder().projectId(project.getId()).description(description).workDate(date)
                .docId(doc).delFlag(0).createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
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
    public TestStory searchStoryById(String storyId) {
        final TestStory story = baseMapper.selectById(storyId);
        if (Objects.nonNull(story)) {
            story.setProject(projectService.getProjectById(story.getProjectId()));
        }
        return story;
    }

    @Override
    public TestStory updateStory(String storyId) {
        final TestStory story = baseMapper.selectById(storyId);
        story.setDelFlag(Objects.equals(story.getDelFlag(), 0) ? 1 : 0);
        story.setUpdateDate(new Date());
        baseMapper.updateById(story);
        return story;
    }

    @Override
    public List<TestStory> listStoryByProjectId(String projectName) {
        return baseMapper.selectList(new QueryWrapper<TestStory>().lambda().eq(TestStory::getProjectId, projectService.getProjectByName(projectName).getId()));
    }

    @Override
    public List<TestStory> listStory() {
        return baseMapper.selectList(new QueryWrapper<TestStory>().lambda().eq(TestStory::getDelFlag, "0"));
    }

    @Override
    public List<TestStory> listStoryByProjectAndWorkDate(String projectId, String workDate) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestStory::getProjectId, projectId);
        wrapper.lambda().eq(StringUtils.isNotBlank(workDate), TestStory::getWorkDate, workDate);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public ResponseResult upload(HttpServletRequest request, MultipartFile file) {
        try {
            ResponseResult result = systemService.uploadFile(request, file);
            if (Objects.equals(ResponseCode.SUCCESS.getCode(), result.getCode())) {
                Map<String, String> map = JacksonUtils.convertValue(result.getData(), new TypeReference<Map<String, String>>() {
                });
                map.put("flag", "story");
                map.put("docPath", "");
                DocInfo docInfo = docInfoService.saveDocInfo(request, map);
                result.setData(docInfo.getId());
            }
            return result;
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }
}
