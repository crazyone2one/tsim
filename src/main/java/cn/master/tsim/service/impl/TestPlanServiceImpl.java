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
import java.util.LinkedList;
import java.util.List;
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

    private final ProjectService projectService;
    private final TestStoryService storyService;

    @Autowired
    public TestPlanServiceImpl(ProjectService projectService, TestStoryService storyService) {
        this.projectService = projectService;
        this.storyService = storyService;
    }

    @Override
    public IPage<TestPlan> pageList(TestPlan plan, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(plan.getProjectId())) {
            List<String> projectId = new LinkedList<>();
            projectService.findByPartialProjectName(plan.getProjectId()).forEach(p -> projectId.add(p.getId()));
            wrapper.lambda().in(TestPlan::getProjectId, projectId);
        }
        if (StringUtils.isNotBlank(plan.getStoryId())) {

        }
        wrapper.lambda().like(StringUtils.isNotBlank(plan.getName()), TestPlan::getName, plan.getName());
        wrapper.lambda().like(StringUtils.isNotBlank(plan.getDescription()), TestPlan::getDescription, plan.getDescription());
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }

    @Override
    public TestPlan savePlan(HttpServletRequest request,TestPlan plan) {
        final Project project = projectService.addProject(plan.getProjectId(),request );
        final TestStory story = storyService.saveStory(request, TestStory.builder().projectId(plan.getProjectId()).description(plan.getDescription()).build());
        plan.setProjectId(project.getId());
        plan.setStoryId(story.getId());
        plan.setDelFlag("0");
        baseMapper.insert(plan);
        return plan;
    }

    @Override
    public TestPlan updatePlan(String argument) {
        final TestPlan testPlan = baseMapper.selectById(argument);
        testPlan.setDelFlag(Objects.equals(testPlan.getDelFlag(), "0") ? "1" : "0");
        baseMapper.updateById(testPlan);
        return testPlan;
    }
}
