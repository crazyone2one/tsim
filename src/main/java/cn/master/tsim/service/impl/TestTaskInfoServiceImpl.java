package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestPlan;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.TestTaskInfoMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * 任务汇总表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
@Service
public class TestTaskInfoServiceImpl extends ServiceImpl<TestTaskInfoMapper, TestTaskInfo> implements TestTaskInfoService {
    private final ProjectService projectService;

    @Autowired
    private TestBugService bugService;
    @Autowired
    TestStoryService storyService;
    @Autowired
    TestPlanService planService;

    @Autowired
    public TestTaskInfoServiceImpl(ProjectService projectService, TestCaseService caseService) {
        this.projectService = projectService;
    }

    @Override
    public IPage<TestTaskInfo> taskInfoPage(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestTaskInfo> wrapper = taskInfoQueryWrapper(request);
        final Page<TestTaskInfo> testTaskInfoPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize),
                wrapper);
        testTaskInfoPage.getRecords().forEach(t -> {
            TestStory story = storyService.searchStoryById(t.getStoryId());
            if (Objects.nonNull(story)) {
                t.setReqDoc(story.getStoryName());
                t.setTestStory(story);
            }
            t.setSubBug(bugService.bugMapByProject(t.getProjectId(), t.getStoryId(), "1"));
            t.setFixBug(bugService.bugMapByProject(t.getProjectId(), t.getStoryId(), "4"));
            t.setProjectId(projectService.getProjectById(t.getProjectId()).getProjectName());
            t.setTester(Constants.userMaps.get(t.getTester()).getUsername());
        });
        return testTaskInfoPage;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public TestTaskInfo addItem(HttpServletRequest request, String projectId, TestPlan plan) {
        // TODO: 2021/11/1 0001 保存项目信息时，暂设置负责人为当前登录用户 。后期优化为可配置
        Tester tester = new Tester();
        final Object account = request.getSession().getAttribute("account");
        if (Objects.nonNull(account)) {
            tester = JacksonUtils.convertToClass(JacksonUtils.convertToString(account), Tester.class);
        }
        assert tester != null;
        final TestTaskInfo build = TestTaskInfo.builder().projectId(projectId)
                .storyId(plan.getStoryId()).planId(plan.getId()).summaryDesc(plan.getName())
                .finishStatus("1")
                .reqDoc(Objects.nonNull(plan.getStory()) ? plan.getStory().getDocId() : "")
                .deliveryStatus("1")
                .tester(tester.getId())
                .issueDate(plan.getWorkDate())
                .createDate(new Date())
                .build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public TestTaskInfo saveTaskInfo(HttpServletRequest request, String projectId, String description, String workDate) {
        Tester tester = new Tester();
        final Object account = request.getSession().getAttribute("account");
        if (Objects.nonNull(account)) {
            tester = JacksonUtils.convertToClass(JacksonUtils.convertToString(account), Tester.class);
        }
        final TestTaskInfo build = TestTaskInfo.builder().projectId(projectId).summaryDesc(description).issueDate(workDate).tester(tester.getId())
                .finishStatus("1").deliveryStatus("1").createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public TestTaskInfo updateItemStatue(HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestTaskInfo::getProjectId, parameterMap.get("projectId")[0]);
        wrapper.lambda().eq(TestTaskInfo::getIssueDate, parameterMap.get("workDate")[0]);
        final TestTaskInfo info = baseMapper.selectOne(wrapper);
//        更新任务完成状态
        info.setFinishStatus("0");
        baseMapper.updateById(info);
        return info;
    }

    @Override
    public void updateTask(HttpServletRequest request, TestTaskInfo taskInfo) {
        baseMapper.updateById(taskInfo);
    }

    @Override
    public TestTaskInfo checkReportDoc(String projectId, String storyId, String workDate) {
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestTaskInfo::getProjectId, projectId)
                .eq(TestTaskInfo::getStoryId, storyId)
                .eq(TestTaskInfo::getIssueDate, workDate);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public ResponseResult getTask(String id) {
        ResponseResult success;
        try {
            TestTaskInfo taskInfo = baseMapper.selectById(id);
            taskInfo.setSubBug(bugService.bugMapByProject(taskInfo.getProjectId(), taskInfo.getStoryId(), "1"));
            taskInfo.setFixBug(bugService.bugMapByProject(taskInfo.getProjectId(), taskInfo.getStoryId(), "4"));
            TestStory story = storyService.searchStoryById(taskInfo.getStoryId());
            if (Objects.nonNull(story)) {
                taskInfo.setReqDoc(story.getStoryName());
                taskInfo.setTestStory(story);
            }
            taskInfo.setProjectId(projectService.getProjectById(taskInfo.getProjectId()).getProjectName());
            success = ResponseUtils.success("数据查询成功", taskInfo);
        } catch (NullPointerException e) {
            return ResponseUtils.error(ResponseCode.ERROR_500.getCode(), ResponseCode.ERROR_500.getMessage());
        } catch (TooManyResultsException e) {
            return ResponseUtils.error(ResponseCode.BODY_NOT_MATCH.getCode(), ResponseCode.BODY_NOT_MATCH.getMessage());
        }
        return success;
    }

    @Override
    public ResponseResult checkTaskReport(String id) {
        final TestTaskInfo taskInfo = baseMapper.selectById(id);
        if (Objects.nonNull(taskInfo) && StringUtils.isNotBlank(taskInfo.getReportDoc())) {
            return ResponseUtils.error("报告已存在");
        }
        return ResponseUtils.success("");
    }

    @Override
    public TestTaskInfo queryItem(String projectId, String storyId) {
        return baseMapper.selectOne(new QueryWrapper<TestTaskInfo>()
                .lambda().eq(TestTaskInfo::getProjectId, projectId).eq(TestTaskInfo::getStoryId, storyId));
    }

    @Override
    public TestTaskInfo queryItem(String projectId, String storyId, String planId) {
        return baseMapper.selectOne(new QueryWrapper<TestTaskInfo>()
                .lambda().eq(TestTaskInfo::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(storyId), TestTaskInfo::getStoryId, storyId)
                .eq(StringUtils.isNotBlank(planId), TestTaskInfo::getPlanId, planId));

    }

    @Override
    public ResponseResult updateTaskInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        String taskDesc = request.getParameter("taskDesc");
        String finishStatus = request.getParameter("finishStatus");
        String deliveryStatus = request.getParameter("deliveryStatus");
        String remark = request.getParameter("remark");
        try {
            final TestTaskInfo taskInfo = this.baseMapper.selectOne(new QueryWrapper<TestTaskInfo>()
                    .eq("id", id).eq("del_flag", 0));
            if (Objects.isNull(taskInfo)) {
                return ResponseUtils.error(ResponseCode.ERROR_500.getCode(), ResponseCode.ERROR_500.getMessage());
            }
            taskInfo.setSummaryDesc(taskDesc);
            taskInfo.setFinishStatus(finishStatus);
            taskInfo.setDeliveryStatus(deliveryStatus);
            taskInfo.setRemark(remark);
            taskInfo.setUpdateDate(new Date());
            baseMapper.updateById(taskInfo);
            if (StringUtils.isNotBlank(taskInfo.getPlanId())) {
                TestPlan byId = planService.getById(taskInfo.getPlanId());
                byId.setName(taskDesc);
                if (Objects.equals("0", finishStatus)) {
                    byId.setWorkStatus(1);
                }
                byId.setUpdateDate(new Date());
                planService.updateById(byId);
            }
            return ResponseUtils.success("任务更新成功");
        } catch (Exception e) {
            return ResponseUtils.error("任务更新失败");
        }
    }

    @Override
    public void exportTaskInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 查询数据
            QueryWrapper<TestTaskInfo> wrapper = taskInfoQueryWrapper(request);
            List<TestTaskInfo> taskInfos = baseMapper.selectList(wrapper);
            for (TestTaskInfo taskInfo : taskInfos) {
                taskInfo.setProjectId(projectService.getProjectById(taskInfo.getProjectId()).getProjectName());
                taskInfo.setReqDoc(StringUtils.isNotBlank(taskInfo.getReqDoc()) ? "有" : "无");
                taskInfo.setFinishStatus(Constants.FINISH_STATUS.get(taskInfo.getFinishStatus()));
                taskInfo.setDeliveryStatus(Constants.DELIVERY_STATUS.get(taskInfo.getDeliveryStatus()));
                Tester tester = new Tester();
                final Object account = request.getSession().getAttribute("account");
                if (Objects.nonNull(account)) {
                    tester = JacksonUtils.convertToClass(JacksonUtils.convertToString(account), Tester.class);
                }
                taskInfo.setTester(tester.getUsername());
            }
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), TestTaskInfo.class).autoCloseStream(Boolean.FALSE).sheet("任务汇总")
                    .doWrite(taskInfos);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private QueryWrapper<TestTaskInfo> taskInfoQueryWrapper(HttpServletRequest request) {
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
//        根据项目名称模糊查询
        String projectName = request.getParameter("projectName");
        wrapper.lambda().inSql(StringUtils.isNotBlank(projectName),
                TestTaskInfo::getProjectId, "select id from t_project where project_name like '%" + projectName + "%'");
        String taskDesc = request.getParameter("taskDesc");
        wrapper.lambda().like(StringUtils.isNotBlank(taskDesc), TestTaskInfo::getSummaryDesc, taskDesc);
//        根据完成状态查询
        String status = request.getParameter("finishStatus");
        wrapper.lambda().eq(StringUtils.isNotBlank(status), TestTaskInfo::getFinishStatus, status);
//        根据任务时间查询
        String taskDate = request.getParameter("taskDate");
        wrapper.lambda().eq(StringUtils.isNotBlank(taskDate), TestTaskInfo::getIssueDate, taskDate);
        wrapper.lambda().eq(TestTaskInfo::getDelFlag, 0);
        return wrapper;
    }
}
