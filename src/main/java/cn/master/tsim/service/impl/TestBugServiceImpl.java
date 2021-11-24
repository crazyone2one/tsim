package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Module;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.mapper.TestBugMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<TestBug> listBugByProjectId(String projectId) {
        return baseMapper.selectList(new QueryWrapper<TestBug>().lambda().eq(TestBug::getProjectId, projectId));
    }

    @Override
    public TestBug addBug(HttpServletRequest request, TestBug testBug) {
        final Module module = moduleService.addModule(testBug.getProjectId(), testBug.getModuleId(), request);
        testBug.setProjectId(module.getProjectId());
        testBug.setModuleId(module.getId());
        testBug.setCreateDate(new Date());
        if (StringUtils.isBlank(testBug.getWorkDate())) {
            testBug.setWorkDate(DateUtils.parse2String(new Date(), "yyyy-MM"));
        } else {
            testBug.setWorkDate(testBug.getWorkDate());
        }
        baseMapper.insert(testBug);
        return testBug;
    }

    @Override
    public TestBug updateBug(TestBug testBug) {
        return null;
    }

    @Override
    public IPage<TestBug> pageListBug(TestBug bug, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestBug> wrapper = getTestBugQueryWrapper(bug);
        final Page<TestBug> selectPage = baseMapper.selectPage(
                new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 15 : pageSize),
                wrapper);
        selectPage.getRecords().forEach(temp -> {
            temp.setProject(posService.getProjectById(temp.getProjectId()));
            temp.setModule(moduleService.getModuleById(temp.getModuleId()));
            temp.setTesterEntity(Constants.userMaps.get(temp.getTester()));
        });
        return selectPage;
    }

    @Override
    public Map<String, Integer> bugMapByProject(String projectId, String bugStatus) {
        Map<String, Integer> result = new LinkedHashMap<>();
        QueryWrapper<TestBug> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestBug::getProjectId, projectId).eq(TestBug::getBugStatus, bugStatus);
        List<TestBug> bugs = baseMapper.selectList(wrapper);
        Map<Integer, Integer> collect = bugs.stream().collect(Collectors.groupingBy(TestBug::getSeverity, Collectors.summingInt(p -> 1)));
        result.put("level1", collect.getOrDefault(1, 0));
        result.put("level2", collect.getOrDefault(2, 0));
        result.put("level3", collect.getOrDefault(3, 0));
        result.put("level4", collect.getOrDefault(4, 0));
        result.put("total", collect.values().stream().mapToInt(Integer::intValue).sum());
        return result;
    }

    @Override
    public ResponseResult getBugById(String id) {
        try {
            final TestBug testBug = baseMapper.selectById(id);
            testBug.setProject(posService.getProjectById(testBug.getProjectId()));
            testBug.setModule(moduleService.getModuleById(testBug.getModuleId()));
            testBug.setTesterEntity(Constants.userMaps.get(testBug.getTester()));
            return ResponseUtils.success("数据查询成功", testBug);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据查询失败", e.getMessage());
        }
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
        wrapper.lambda().eq(Objects.nonNull(bug.getSeverity()), TestBug::getSeverity, bug.getSeverity());
        wrapper.lambda().eq(Objects.nonNull(bug.getBugStatus()), TestBug::getBugStatus, bug.getBugStatus());
        return wrapper;
    }
}
