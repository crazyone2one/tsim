package cn.master.tsim.service.impl;

import cn.master.tsim.entity.ProjectCaseRef;
import cn.master.tsim.mapper.ProjectCaseRefMapper;
import cn.master.tsim.service.ProjectCaseRefService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目-测试用例关联表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-11-04
 */
@Service
public class ProjectCaseRefServiceImpl extends ServiceImpl<ProjectCaseRefMapper, ProjectCaseRef> implements ProjectCaseRefService {

    @Override
    public int addRefItem(String projectId, String storyId, String caseId, String workDate) {
        final ProjectCaseRef build = ProjectCaseRef.builder().projectId(projectId).storyId(storyId).caseId(caseId).workDate(workDate).build();
        return baseMapper.insert(build);
    }

    @Override
    public List<ProjectCaseRef> queryRefList(String projectId, String workDate) {
        QueryWrapper<ProjectCaseRef> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ProjectCaseRef::getProjectId, projectId).eq(ProjectCaseRef::getWorkDate, workDate);
        return baseMapper.selectList(wrapper);
    }
}
