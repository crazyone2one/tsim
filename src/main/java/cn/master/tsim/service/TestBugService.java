package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestBug;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问题单(bug) 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-28
 */
public interface TestBugService extends IService<TestBug> {

    List<TestBug> listBugByProjectId(String projectId);

    TestBug addBug(HttpServletRequest request, TestBug testBug);

    TestBug updateBug(TestBug testBug);

    IPage<TestBug> pageListBug(TestBug bug, Integer pageCurrent, Integer pageSize);

    Map<String, Integer> bugMapByProject(String projectId, String storyId, String bugStatus);

    /**
     * 查询bug信息
     *
     * @param id bug id
     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */
    ResponseResult getBugById(String id);
}
