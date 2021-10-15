package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.mapper.TestStoryMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestStoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    private final ProjectService projectService;

    @Autowired
    public TestStoryServiceImpl(ProjectService projectService) {
        this.projectService = projectService;
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
        if (StringUtils.isNotBlank(story.getDelFlag())) {
            wrapper.lambda().eq(TestStory::getDelFlag, story.getDelFlag());
        }
        wrapper.lambda().orderByAsc(TestStory::getDelFlag);
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }

    @Override
    public TestStory saveStory(HttpServletRequest request, TestStory story) {
        final Project project = projectService.addProject(story.getProjectId(), request);
        final TestStory testStory = getStory(story.getDescription(), project.getId());
        if (Objects.nonNull(testStory)) {
            return testStory;
        }
        TestStory build = TestStory.builder().projectId(project.getId()).description(story.getDescription()).workDate(story.getWorkDate())
                .delFlag("0").createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public TestStory getStory(String storyName, String proId) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
//        完全匹配
        wrapper.lambda().eq(StringUtils.isNotBlank(storyName), TestStory::getDescription, storyName);
        wrapper.lambda().eq(StringUtils.isNotBlank(proId), TestStory::getProjectId, proId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public TestStory searchStoryById(String storyId) {
        return baseMapper.selectById(storyId);
    }

    @Override
    public TestStory updateStory(String argument) {
        final TestStory story = baseMapper.selectById(argument);
        story.setDelFlag(Objects.equals(story.getDelFlag(), "0") ? "1" : "0");
        story.setUpdateDate(new Date());
        baseMapper.updateById(story);
        return story;
    }

    @Override
    public List<TestStory> listStoryByProjectId(String projectName) {
        return baseMapper.selectList(new QueryWrapper<TestStory>().lambda().eq(TestStory::getProjectId, projectService.getProjectByName(projectName).getId()));
    }

    @Override
    public List<TestStory> listStoryByProjectAndWorkDate(String projectId, String workDate) {
        QueryWrapper<TestStory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestStory::getProjectId, projectId);
        wrapper.lambda().eq(StringUtils.isNotBlank(workDate), TestStory::getWorkDate, workDate);
        return baseMapper.selectList(wrapper);
    }
}
