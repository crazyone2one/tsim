package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestStory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 保存需求数据
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.entity.TestStory
     */
    TestStory saveStory(HttpServletRequest request);

    /**
     * 查询需求数据
     *
     * @param description 需求内容
     * @param workDate    需求时间
     * @param proId       项目id
     * @return cn.master.tsim.entity.TestStory
     */
    TestStory getStory(String description, String workDate, String proId);

    TestStory searchStoryById(String storyId);

    /**
     * 更新需求状态
     *
     * @param storyId story id
     * @return cn.master.tsim.entity.TestStory
     */
    TestStory updateStory(String storyId);

    List<TestStory> listStoryByProjectId(String projectName);

    List<TestStory> listStory();

    List<TestStory> listStoryByProjectAndWorkDate(String projectId, String workDate);

    ResponseResult upload(HttpServletRequest request, MultipartFile file);
}
