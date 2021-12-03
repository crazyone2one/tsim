package cn.master.tsim.service;

import cn.master.tsim.entity.ProjectCaseRef;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 项目-测试用例关联表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-11-04
 */
public interface ProjectCaseRefService extends IService<ProjectCaseRef> {

    /**
     * 添加数据
     *
     * @param projectId
     * @param storyId
     * @param caseId
     * @param workDate
     * @return int
     */
    int addRefItem(String projectId, String storyId, String caseId, String workDate);

    List<ProjectCaseRef> queryRefList(String projectId, String workDate);
}
