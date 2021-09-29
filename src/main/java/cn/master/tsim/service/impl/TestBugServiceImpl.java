package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.mapper.TestBugMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestBugService;
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
 * 问题单(bug) 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-28
 */
@Service
public class TestBugServiceImpl extends ServiceImpl<TestBugMapper, TestBug> implements TestBugService {

    private final ProjectService posService;
    private final ModuleService moduleService;

    @Autowired
    public TestBugServiceImpl(ProjectService posService, ModuleService moduleService) {
        this.posService = posService;
        this.moduleService = moduleService;
    }

    @Override
    public List<TestBug> listAllBug(TestBug bug) {
        QueryWrapper<TestBug> wrapper = getTestBugQueryWrapper(bug);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public TestBug addBug(TestBug testBug) {
        final Module module = moduleService.addModule(testBug.getProjectId(), testBug.getModuleId());
        testBug.setProjectId(module.getProjectId());
        testBug.setModuleId(module.getId());
        testBug.setCreateDate(new Date());
        baseMapper.insert(testBug);
        return testBug;
    }

    @Override
    public TestBug updateBug(TestBug testBug) {
        return null;
    }

    @Override
    public IPage<TestBug> pageListBug(TestBug bug,Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestBug> wrapper = getTestBugQueryWrapper(bug);
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }

    private QueryWrapper<TestBug> getTestBugQueryWrapper(TestBug bug) {
        QueryWrapper<TestBug> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        if (StringUtils.isNotBlank(bug.getProjectId())) {
            posService.findByPartialProjectName(bug.getProjectId()).forEach(temp -> tempProjectId.add(temp.getId()));
            wrapper.lambda().in(TestBug::getProjectId, tempProjectId);
        }
        if (StringUtils.isNotBlank(bug.getModuleId())) {
            moduleService.findByPartialModuleName(bug.getModuleId()).forEach(temp -> tempModuleId.add(temp.getId()));
            wrapper.lambda().in(TestBug::getModuleId, tempModuleId);
        }
        if (StringUtils.isNotBlank(bug.getTitle())) {
            wrapper.lambda().like(TestBug::getTitle, bug.getTitle());
        }
        wrapper.lambda().eq(StringUtils.isNotBlank(bug.getSeverity()), TestBug::getSeverity, bug.getSeverity());
        wrapper.lambda().eq(StringUtils.isNotBlank(bug.getBugStatus()), TestBug::getBugStatus, bug.getBugStatus());
        return wrapper;
    }
}
