package cn.master.tsim.service;

import cn.master.tsim.entity.ProjectBugRef;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-08
 */
public interface ProjectBugRefService extends IService<ProjectBugRef> {

    ProjectBugRef addItem(String projectId, String bugId, String workDate);

    ProjectBugRef checkItem(String projectId, String bugId, String workDate);

    List<ProjectBugRef> refList(String projectId, String bugId, String workDate);

}
