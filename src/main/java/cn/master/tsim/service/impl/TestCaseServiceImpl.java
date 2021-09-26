package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-22
 */
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements TestCaseService {

    private final ProjectService projectService;
    private final ModuleService moduleService;

    @Autowired
    public TestCaseServiceImpl(ProjectService projectService, ModuleService moduleService) {
        this.projectService = projectService;
        this.moduleService = moduleService;
    }

    @Override
    public TestCase saveCase(TestCase testCase) {
        final Project project = projectService.addProject(testCase.getProjectId());
        final Module module = moduleService.addModule(testCase.getProjectId(), testCase.getModuleId());
        testCase.setActive("0");
        testCase.setProjectId(project.getId());
        testCase.setModuleId(module.getId());
        testCase.setCreateDate(new Date());
        baseMapper.insert(testCase);
        return testCase;
    }

    @Override
    public List<TestCase> listTestCase(String projectId, String moduleId) {
        return baseMapper.selectList(new QueryWrapper<TestCase>().lambda()
                .eq(StringUtils.isNotBlank(projectId), TestCase::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(moduleId), TestCase::getModuleId, moduleId));
    }
}
