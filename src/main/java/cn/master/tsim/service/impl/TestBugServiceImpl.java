package cn.master.tsim.service.impl;

import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.mapper.TestBugMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.TestBugService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    private final ModuleService moduleService;

    @Autowired
    public TestBugServiceImpl(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Override
    public List<TestBug> listAllBug(TestBug bug) {
        return baseMapper.selectList(new QueryWrapper<>());
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
        QueryWrapper<TestBug> wrapper = new QueryWrapper<>();
        return baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
    }
}
