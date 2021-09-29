package cn.master.tsim.service;

import cn.master.tsim.entity.TestBug;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    TestBug addBug(TestBug testBug);

    TestBug updateBug(TestBug testBug);

}
