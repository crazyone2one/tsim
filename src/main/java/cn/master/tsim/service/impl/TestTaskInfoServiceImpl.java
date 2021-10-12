package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.mapper.TestTaskInfoMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.service.TestTaskInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public TestTaskInfoServiceImpl(ProjectService projectService, TestCaseService caseService) {
        this.projectService = projectService;
        this.caseService = caseService;
    }

    @Override
    public IPage<TestTaskInfo> taskInfoPage(TestTaskInfo taskInfo, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestTaskInfo> wrapper = new QueryWrapper<>();
        final Page<TestTaskInfo> testTaskInfoPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        testTaskInfoPage.getRecords().forEach(t->{
            t.setSubBug(bugService.bugMapByProject(t.getProjectId(),"1"));
            t.setFixBug(bugService.bugMapByProject(t.getProjectId(),"4"));
            t.setProjectId(projectService.getProjectById(t.getProjectId()).getProjectName());
        });
        return testTaskInfoPage;
    }

    @Override
    public TestTaskInfo addItem(Project project) {
        final TestTaskInfo build = TestTaskInfo.builder().projectId(project.getId())
                .createCaseCount(caseService.caseCountByStatus(project.getId(), "").get("total")).build();
        baseMapper.insert(build);
        return build;
    }
}
