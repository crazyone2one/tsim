package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseCode;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.TestTaskInfoMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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
    private final TestCaseService caseService;

    @Autowired
    private TestBugService bugService;
    @Autowired
    private TestStoryService storyService;

    @Autowired
    public TestTaskInfoServiceImpl(ProjectService projectService, TestCaseService caseService) {
        this.projectService = projectService;
        this.caseService = caseService;
    }

    @Override
    public IPage<TestTaskInfo> taskInfoPage(TestTaskInfo taskInfo, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
//        根据项目名称模糊查询
        wrapper.lambda().inSql(StringUtils.isNotBlank(taskInfo.getProjectId()),
                TestTaskInfo::getProjectId, "select id from t_project where project_name like '%" + taskInfo.getProjectId() + "%'");
//        根据完成状态查询
        wrapper.lambda().eq(StringUtils.isNotBlank(taskInfo.getFinishStatus()), TestTaskInfo::getFinishStatus, taskInfo.getFinishStatus());
//        根据任务时间查询
        wrapper.lambda().eq(StringUtils.isNotBlank(taskInfo.getIssueDate()), TestTaskInfo::getIssueDate, taskInfo.getIssueDate());
        final Page<TestTaskInfo> testTaskInfoPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize),
                wrapper);
        testTaskInfoPage.getRecords().forEach(t -> {
            t.setSubBug(bugService.bugMapByProject(t.getProjectId(), "1"));
            t.setFixBug(bugService.bugMapByProject(t.getProjectId(), "4"));
            t.setProjectId(projectService.getProjectById(t.getProjectId()).getProjectName());
        });
        return testTaskInfoPage;
    }

    @Override
    public TestTaskInfo addItem(HttpServletRequest request, Project project, TestStory story) {
        // TODO: 2021/11/1 0001 保存项目信息时，暂设置负责人为当前登录用户 。后期优化为可配置
        Tester tester = new Tester();
        final Object account = request.getSession().getAttribute("account");
        if (Objects.nonNull(account)) {
            tester = JacksonUtils.convertToClass(JacksonUtils.convertToString(account), Tester.class);
        }
        assert tester != null;
        final TestTaskInfo taskInfo = getItemByProject(request, project, story);
        if (Objects.nonNull(taskInfo)) {
            return taskInfo;
        }
        final TestTaskInfo build = TestTaskInfo.builder().projectId(project.getId()).summaryDesc(story.getDescription())
                .createCaseCount(caseService.caseCountByStatus(project.getId(), "").get("total"))
                .finishStatus("1")
                .reqDoc(StringUtils.isNotBlank(story.getDocId()) ? story.getDocId() : "")
                .tester(tester.getId())
                .issueDate(story.getWorkDate())
                .createDate(new Date())
                .build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public TestTaskInfo getItemByProject(HttpServletRequest request, Project project, TestStory story) {
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestTaskInfo::getProjectId, project.getId());
        wrapper.lambda().eq(TestTaskInfo::getIssueDate, story.getWorkDate());
        wrapper.lambda().eq(TestTaskInfo::getSummaryDesc, story.getDescription());
        return baseMapper.selectOne(wrapper);
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
    public boolean checkReportDoc(String projectId, String workDate) {
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestTaskInfo::getProjectId, projectId).eq(TestTaskInfo::getIssueDate, workDate);
        final TestTaskInfo taskInfo = baseMapper.selectOne(wrapper);
        if (Objects.nonNull(taskInfo)) {
            return StringUtils.isNotBlank(taskInfo.getReportDoc());
        }
        return false;
    }

    @Override
    public ResponseResult getTask(String id) {
        ResponseResult success;
        try {
            TestTaskInfo taskInfo = baseMapper.selectById(id);
            taskInfo.setSubBug(bugService.bugMapByProject(taskInfo.getProjectId(), "1"));
            taskInfo.setFixBug(bugService.bugMapByProject(taskInfo.getProjectId(), "4"));
//            TestStory story = storyService.getStory("", taskInfo.getIssueDate(), taskInfo.getProjectId());
//            taskInfo.setReqDoc(Objects.nonNull(story) ? story.getDescription() : "");
            taskInfo.setProjectId(projectService.getProjectById(taskInfo.getProjectId()).getProjectName());
            success = ResponseUtils.success("数据查询成功", taskInfo);
        } catch (NullPointerException e) {
            return ResponseUtils.error(ResponseCode.ERROR_500.getCode(), ResponseCode.ERROR_500.getMessage());
        } catch (TooManyResultsException e) {
            return ResponseUtils.error(ResponseCode.BODY_NOT_MATCH.getCode(), ResponseCode.BODY_NOT_MATCH.getMessage());
        }
        return success;
    }
}
