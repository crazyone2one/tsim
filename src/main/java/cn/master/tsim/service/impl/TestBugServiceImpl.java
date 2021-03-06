package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.ProjectBugRef;
import cn.master.tsim.entity.TestBug;
import cn.master.tsim.mapper.TestBugMapper;
import cn.master.tsim.service.ModuleService;
import cn.master.tsim.service.ProjectBugRefService;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestBugService;
import cn.master.tsim.util.DateUtils;
import cn.master.tsim.util.ResponseUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProjectBugRefService projectBugRefService;

    @Autowired
    public TestBugServiceImpl(ProjectService posService, ModuleService moduleService, ProjectBugRefService projectBugRefService) {
        this.posService = posService;
        this.moduleService = moduleService;
        this.projectBugRefService = projectBugRefService;
    }

    @Override
    public List<TestBug> listBugByProjectId(String projectId) {
        return baseMapper.selectList(new QueryWrapper<TestBug>().lambda().eq(TestBug::getProjectId, projectId));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public TestBug saveOrUpdateBug(HttpServletRequest request, TestBug testBug) {

        String bugId = testBug.getId();
        if (StringUtils.isNotBlank(bugId)) {
            TestBug byId = getById(bugId);
            byId.setProjectId(testBug.getProjectId());
            byId.setModuleId(testBug.getModuleId());
            byId.setTitle(testBug.getTitle());
            byId.setBugDescription(testBug.getBugDescription());
            byId.setFunc(testBug.getFunc());
            byId.setReproduceSteps(testBug.getReproduceSteps());
            byId.setExpectResult(testBug.getExpectResult());
            byId.setActualResult(testBug.getActualResult());
            byId.setSeverity(testBug.getSeverity());
            byId.setTester(testBug.getTester());
            byId.setBugStatus(testBug.getBugStatus());
            byId.setBugRecurrenceProbability(testBug.getBugRecurrenceProbability());
            byId.setUpdateDate(new Date());
            byId.setNote(testBug.getNote());
            updateById(byId);
            return byId;
        }
        final TestBug build = TestBug.builder()
                .projectId(testBug.getProjectId())
                .moduleId(moduleService.addModule(request,testBug.getProjectId(),testBug.getModuleId()).getId())
                .title(testBug.getTitle()).bugDescription(testBug.getBugDescription()).reproduceSteps(testBug.getReproduceSteps())
                .expectResult(testBug.getExpectResult()).actualResult(testBug.getActualResult()).bugRecurrenceProbability(testBug.getBugRecurrenceProbability())
                .severity(testBug.getSeverity()).func(testBug.getFunc()).bugStatus(testBug.getBugStatus()).note(testBug.getNote())
                .tester(testBug.getTester()).delFlag(0).createDate(new Date())
                .workDate(StringUtils.isBlank(testBug.getWorkDate()) ? DateUtils.parse2String(new Date(), "yyyy-MM") : testBug.getWorkDate())
                .build();
        baseMapper.insert(build);
        final String tempStoryId = testBug.getStoryId();
        if (StringUtils.isNotBlank(tempStoryId)) {
            projectBugRefService.addItem(build.getProjectId(), build.getId(), build.getWorkDate(), tempStoryId);
        }
        return build;
    }

    @Override
    public IPage<TestBug> pageListBug(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        QueryWrapper<TestBug> wrapper = getTestBugQueryWrapper(request);
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
    public Map<String, Integer> bugMapByProject(String projectId, String storyId, String bugStatus) {
        final List<ProjectBugRef> bugRefList = projectBugRefService.refList(projectId, null, null, storyId);
        final List<String> bugList = bugRefList.stream().map(ProjectBugRef::getBugId).collect(Collectors.toList());
        Map<String, Integer> result = new LinkedHashMap<>();
        QueryWrapper<TestBug> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TestBug::getProjectId, projectId).eq(TestBug::getBugStatus, bugStatus);
        wrapper.lambda().in(CollectionUtils.isNotEmpty(bugList), TestBug::getId, bugList);
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

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public ResponseResult batchDelete(HttpServletRequest request) {
        try {
            final List<String> idList = JSONArray.parseArray(request.getParameter("ids"), String.class);
            idList.forEach(t -> {
                TestBug bug = baseMapper.selectById(t);
                bug.setDelFlag(1);
                bug.setUpdateDate(new Date());
                baseMapper.updateById(bug);
            });
            return ResponseUtils.success("数据删除成功");
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据删除失败", e.getMessage());
        }
    }

    private QueryWrapper<TestBug> getTestBugQueryWrapper(HttpServletRequest request) {
        QueryWrapper<TestBug> wrapper = new QueryWrapper<>();
        List<String> tempProjectId = new LinkedList<>();
        List<String> tempModuleId = new LinkedList<>();
        final String projectName = request.getParameter("projectName");
        if (StringUtils.isNotBlank(projectName)) {
            posService.findByPartialProjectName(projectName).forEach(temp -> tempProjectId.add(temp.getId()));
            wrapper.lambda().in(TestBug::getProjectId, tempProjectId);
        }
        final String moduleName = request.getParameter("moduleName");
        if (StringUtils.isNotBlank(moduleName)) {
            moduleService.findByPartialModuleName(moduleName).forEach(temp -> tempModuleId.add(temp.getId()));
            wrapper.lambda().in(TestBug::getModuleId, tempModuleId);
        }
        final String titleDesc = request.getParameter("titleDesc");
        if (StringUtils.isNotBlank(titleDesc)) {
            wrapper.lambda().like(TestBug::getBugDescription, titleDesc);
        }
        String severity = request.getParameter("severity");
        wrapper.lambda().eq(StringUtils.isNotBlank(severity), TestBug::getSeverity, severity);
        String status = request.getParameter("status");
        wrapper.lambda().eq(StringUtils.isNotBlank(status), TestBug::getBugStatus, status);
        wrapper.lambda().eq(TestBug::getDelFlag, 0).orderByDesc(TestBug::getDelFlag);
        return wrapper;
    }
}
