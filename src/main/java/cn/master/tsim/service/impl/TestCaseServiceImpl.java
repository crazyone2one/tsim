package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.mapper.TestCaseMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestCaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    public List<TestCase> listTestCase(TestCase testCase, String projectId, String moduleId) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        if (Objects.nonNull(testCase)) {
//            项目名称模糊查询
            if (StringUtils.isNotBlank(testCase.getProjectId())) {
                final List<Project> projects = projectService.findByPartialProjectName(testCase.getProjectId());
                projects.forEach(temp -> tempProjectId.add(temp.getId()));
                wrapper.lambda().in(TestCase::getProjectId, tempProjectId);
            }
//            模块模糊查询
            if (StringUtils.isNotBlank(testCase.getModuleId())) {
                moduleService.findByPartialModuleName(testCase.getModuleId()).forEach(temp -> tempModuleId.add(temp.getId()));
                wrapper.lambda().in(TestCase::getModuleId, tempModuleId);
            }
//            测试用例标题模糊查询
            wrapper.lambda().like(StringUtils.isNotBlank(testCase.getName()), TestCase::getName, testCase.getName());
//            优先级
            if (StringUtils.isNotBlank(testCase.getPriority())) {
                wrapper.lambda().eq(TestCase::getPriority, testCase.getPriority());
            }
//            是否删除
            wrapper.lambda().eq(StringUtils.isNotBlank(testCase.getActive()), TestCase::getActive, testCase.getActive());
        } else {
            wrapper.lambda().eq(StringUtils.isNotBlank(projectId), TestCase::getProjectId, projectId);
            wrapper.lambda().eq(StringUtils.isNotBlank(moduleId), TestCase::getModuleId, moduleId);
        }
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void updateCase(String caseId) {
        final TestCase aCase = getById(caseId);
        aCase.setActive(Objects.equals(aCase.getActive(), "0") ? "1" : "0");
        aCase.setUpdateDate(new Date());
        baseMapper.updateById(aCase);
    }

    @Override
    public IPage<TestCase> pageList(TestCase testCase, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestCase> wrapper = new QueryWrapper<>();
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }
}
