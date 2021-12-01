package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.mapper.TestPlanMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestPlanService;
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
import java.util.Objects;

/**
 * <p>
 * 测试计划表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
@Service
public class TestPlanServiceImpl extends ServiceImpl<TestPlanMapper, TestPlan> implements TestPlanService {

    private final TestStoryService storyService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    public TestPlanServiceImpl(TestStoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public IPage<TestPlan> pageList(TestPlan plan, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<>();
//        按照项目查询
        if (StringUtils.isNotBlank(plan.getProjectId())) {
            wrapper.lambda().eq(TestPlan::getProjectId, plan.getProjectId());
        }
        //        根据测试计划名称查询
        wrapper.lambda().like(StringUtils.isNotBlank(plan.getName()), TestPlan::getName, plan.getName());
        //        根据测试计划描述内容查询
        wrapper.lambda().like(StringUtils.isNotBlank(plan.getDescription()), TestPlan::getDescription, plan.getDescription());
        wrapper.lambda().eq(Objects.nonNull(plan.getDelFlag()), TestPlan::getDelFlag, plan.getDelFlag());
        wrapper.lambda().orderByAsc(TestPlan::getDelFlag);
        wrapper.lambda().orderByAsc(TestPlan::getCreateDate);
        final Page<TestPlan> iPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        iPage.getRecords().forEach(t -> {
            final Project project = projectService.getProjectById(t.getProjectId());
            t.setProject(project);
            t.setStory(storyService.searchStoryById(t.getStoryId()));
        });
        return iPage;
    }

    @Override
    public TestPlan savePlan(HttpServletRequest request, TestPlan plan) {
        final TestStory story = storyService.searchStoryById(plan.getStoryId());
        TestPlan build = TestPlan.builder().description(plan.getDescription()).name(plan.getName())
                .projectId(story.getProject().getId()).storyId(story.getId()).workDate(story.getWorkDate()).delFlag(0).createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public TestPlan updatePlan(String planId) {
        final TestPlan testPlan = baseMapper.selectById(planId);
        testPlan.setDelFlag(Objects.equals(testPlan.getDelFlag(), 0) ? 1 : 0);
        testPlan.setUpdateDate(new Date());
        baseMapper.updateById(testPlan);
        return testPlan;
    }
}
