package cn.master.tsim.service;

import cn.master.tsim.entity.TestStory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    TestStory saveStory(HttpServletRequest request, TestStory story);

    TestStory getStory(String storyName, String proId);
    TestStory searchStoryById(String storyId);

    TestStory updateStory(String argument);

    List<TestStory> listStoryByProjectId(String projectName);
    List<TestStory> listStory();

    List<TestStory> listStoryByProjectAndWorkDate(String projectId, String workDate);
}
