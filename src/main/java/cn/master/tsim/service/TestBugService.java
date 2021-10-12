package cn.master.tsim.service;

import cn.master.tsim.entity.TestBug;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<TestBug> listAllBug(TestBug bug);
    List<TestBug> listBugByProjectId(String projectId);

    TestBug addBug(TestBug testBug);

    TestBug updateBug(TestBug testBug);

    IPage<TestBug> pageListBug(TestBug bug, Integer pageCurrent, Integer pageSize);

    Map<String, Integer> bugMapByProject(String projectId, String bugStatus);
}
