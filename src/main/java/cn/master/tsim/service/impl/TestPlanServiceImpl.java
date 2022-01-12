package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.mapper.TestPlanMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestPlanService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.util.ResponseUtils;
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
    public IPage<TestPlan> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestPlan> wrapper = new QueryWrapper<>();
//        按照项目查询
        final String projectName = request.getParameter("projectName");
        if (StringUtils.isNotBlank(projectName)) {
            List<String> tempProjectId = new LinkedList<>();
            projectService.findByPartialProjectName(projectName).forEach(t -> tempProjectId.add(t.getId()));
            wrapper.lambda().in(TestPlan::getProjectId, tempProjectId);
        }
        //        根据测试计划名称查询
        final String planName = request.getParameter("planName");
        wrapper.lambda().like(StringUtils.isNotBlank(planName), TestPlan::getName, planName);
        //        根据测试计划描述内容查询
        final String planDesc = request.getParameter("planDesc");
        wrapper.lambda().like(StringUtils.isNotBlank(planDesc), TestPlan::getDescription, planDesc);
//        wrapper.lambda().eq(Objects.nonNull(request.getDelFlag()), TestPlan::getDelFlag, request.getDelFlag());
        wrapper.lambda().orderByAsc(TestPlan::getDelFlag);
        wrapper.lambda().orderByAsc(TestPlan::getCreateDate);
        final Page<TestPlan> iPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        iPage.getRecords().forEach(t -> {
            final Project project = projectService.getProjectById(t.getProjectId());
            t.setProject(project);
            t.setProjectId(project.getProjectName());
            final TestStory story = storyService.searchStoryById(t.getStoryId());
            t.setStory(story);
            t.setStoryId(story.getDescription());
        });
        return iPage;
    }

    @Override
    public ResponseResult savePlan(HttpServletRequest request, String storyId, String planName, String planDesc) {
        final TestPlan uniquePlan = uniquePlan(storyId, planName, planDesc);
        if (Objects.nonNull(uniquePlan)) {
            return ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(), "存在相同测试计划");
        }
        final TestStory story = storyService.searchStoryById(storyId);
        TestPlan build = TestPlan.builder().description(planDesc).name(planName)
                .projectId(story.getProject().getId()).storyId(story.getId()).workDate(story.getWorkDate()).delFlag(0).createDate(new Date()).build();
        baseMapper.insert(build);
        return ResponseUtils.error(ResponseCode.SUCCESS.getCode(), "数据添加成功", build);
    }

    @Override
    public TestPlan uniquePlan(String storyId, String planName, String planDesc) {
        return baseMapper.selectOne(new QueryWrapper<TestPlan>().lambda()
                .eq(TestPlan::getStoryId, storyId).eq(TestPlan::getName, planName).eq(TestPlan::getDescription, planDesc));
    }

    @Override
    public TestPlan queryPlanById(String id) {
        TestPlan byId = getById(id);
        byId.setStory(storyService.searchStoryById(byId.getStoryId()));
        return byId;
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
