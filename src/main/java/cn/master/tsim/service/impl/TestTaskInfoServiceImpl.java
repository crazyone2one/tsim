package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.TestTaskInfoMapper;
import cn.master.tsim.service.*;
import cn.master.tsim.util.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
                TestTaskInfo::getProjectId, "select id from project where project_name like '%" + taskInfo.getProjectId() + "%'");
//        根据完成状态查询
        wrapper.lambda().eq(StringUtils.isNotBlank(taskInfo.getFinishStatus()), TestTaskInfo::getFinishStatus, taskInfo.getFinishStatus());
//        根据任务时间查询
        wrapper.lambda().eq(StringUtils.isNotBlank(taskInfo.getIssueDate()), TestTaskInfo::getIssueDate, taskInfo.getIssueDate());
        final Page<TestTaskInfo> testTaskInfoPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        testTaskInfoPage.getRecords().forEach(t -> {
            t.setSubBug(bugService.bugMapByProject(t.getProjectId(), "1"));
            t.setFixBug(bugService.bugMapByProject(t.getProjectId(), "4"));
            //            设置任务描述为需求内容
            List<TestStory> storyList = storyService.listStoryByProjectAndWorkDate(t.getProjectId(), t.getIssueDate());
            t.setSummaryDesc(CollectionUtils.isNotEmpty(storyList) ? storyList.get(0).getDescription() : "");
            t.setProjectId(projectService.getProjectById(t.getProjectId()).getProjectName());
        });
        return testTaskInfoPage;
    }

    @Override
    public TestTaskInfo addItem(Project project, HttpServletRequest request, String workDate) {
        Tester tester = new Tester();
        final Object account = request.getSession().getAttribute("account");
        if (Objects.nonNull(account)) {
            tester = JacksonUtils.convertToClass(JacksonUtils.convertToString(account), Tester.class);
        }
        assert tester != null;
        final TestTaskInfo build = TestTaskInfo.builder().projectId(project.getId())
                .createCaseCount(caseService.caseCountByStatus(project.getId(), "").get("total"))
                .tester(tester.getAccount())
                .issueDate(workDate)
                .build();
        baseMapper.insert(build);
        return build;
    }
}
