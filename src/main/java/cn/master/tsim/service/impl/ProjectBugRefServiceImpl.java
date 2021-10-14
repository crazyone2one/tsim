package cn.master.tsim.service.impl;

import cn.master.tsim.entity.ProjectBugRef;
import cn.master.tsim.mapper.ProjectBugRefMapper;
import cn.master.tsim.service.ProjectBugRefService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-08
 */
@Service
public class ProjectBugRefServiceImpl extends ServiceImpl<ProjectBugRefMapper, ProjectBugRef> implements ProjectBugRefService {

    @Override
    public ProjectBugRef addItem(String projectId, String bugId, String workDate) {
        final ProjectBugRef ref = checkItem(projectId, bugId, workDate);
        if (Objects.nonNull(ref)) {
            return ref;
        }
        final ProjectBugRef build = ProjectBugRef.builder().projectId(projectId).bugId(bugId).workDate(workDate).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public ProjectBugRef checkItem(String projectId, String bugId, String workDate) {
        QueryWrapper<ProjectBugRef> wrapper = getRefQueryWrapper(projectId, bugId, workDate);
        return baseMapper.selectOne(wrapper);
    }

    private QueryWrapper<ProjectBugRef> getRefQueryWrapper(String projectId, String bugId, String workDate) {
        QueryWrapper<ProjectBugRef> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(projectId), ProjectBugRef::getProjectId, projectId);
        wrapper.lambda().eq(StringUtils.isNotBlank(bugId), ProjectBugRef::getBugId, bugId);
        wrapper.lambda().eq(StringUtils.isNotBlank(workDate), ProjectBugRef::getWorkDate, workDate);
        return wrapper;
    }

    @Override
    public List<ProjectBugRef> refList(String projectId, String bugId, String workDate) {
        QueryWrapper<ProjectBugRef> wrapper = getRefQueryWrapper(projectId, bugId, workDate);
        return baseMapper.selectList(wrapper);
    }
}
