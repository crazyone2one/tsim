package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.mapper.TestPlanMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PlanStoryRefService planStoryRefService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    TestTaskInfoService taskInfoService;

    @Autowired
    public TestPlanServiceImpl(TestStoryService storyService, PlanStoryRefService planStoryRefService) {
        this.storyService = storyService;
        this.planStoryRefService = planStoryRefService;
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
        wrapper.lambda().orderByAsc(TestPlan::getDelFlag);
        wrapper.lambda().orderByAsc(TestPlan::getCreateDate);
        final Page<TestPlan> iPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        iPage.getRecords().forEach(t -> {
            t.setProject(projectService.getProjectById(t.getProjectId()));
            if (StringUtils.isNotBlank(t.getStoryId())) {
                t.setStory(storyService.searchStoryById(t.getStoryId()));
            }
        });
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public ResponseResult savePlan(HttpServletRequest request) {
        String tempProjectId = request.getParameter("projectId");
        String tempStoryId = request.getParameter("storyId");
        String tempPlanName = request.getParameter("name");
        String description = request.getParameter("description").trim();
//        同一项目下不允许出现相同的测试计划
        final TestPlan uniquePlan = uniquePlan(tempStoryId, tempPlanName, tempProjectId);
        if (Objects.nonNull(uniquePlan)) {
            return ResponseUtils.error(ResponseCode.PARAMS_ERROR.getCode(), "存在相同测试计划");
        }
        TestPlan build = TestPlan.builder().description(description).name(tempPlanName)
                .projectId(tempProjectId).storyId(tempStoryId).workDate(DateUtils.parse2String(new Date(), "yyyy-MM"))
                .delFlag(0).createDate(new Date()).build();
        baseMapper.insert(build);
        planStoryRefService.addRefItem(build.getId(), tempStoryId);
        taskInfoService.addItem(request, tempProjectId, build);
        return ResponseUtils.error(ResponseCode.SUCCESS.getCode(), "数据添加成功", build);
    }

    @Override
    public TestPlan uniquePlan(String storyId, String planName, String projectId) {
        return baseMapper.selectOne(new QueryWrapper<TestPlan>().lambda()
                .eq(TestPlan::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(storyId), TestPlan::getStoryId, storyId)
                .eq(TestPlan::getName, planName));
//                .eq(StringUtils.isNotBlank(projectId),TestPlan::getDescription, projectId));
    }

    @Override
    public TestPlan queryPlanById(String id) {
        TestPlan byId = getById(id);
        byId.setStory(storyService.searchStoryById(byId.getStoryId()));
        return byId;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public TestPlan updatePlan(String planId) {
        final TestPlan testPlan = baseMapper.selectById(planId);
        testPlan.setDelFlag(Objects.equals(testPlan.getDelFlag(), 0) ? 1 : 0);
        testPlan.setUpdateDate(new Date());
        baseMapper.updateById(testPlan);
        return testPlan;
    }
}
