package cn.master.tsim.service;

import cn.master.tsim.entity.TestStory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 需求表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-29
 */
public interface TestStoryService extends IService<TestStory> {

    IPage<TestStory> pageList(TestStory story, Integer pageCurrent, Integer pageSize);

    TestStory saveStory(TestStory story);

    TestStory updateStory(String argument);
}
