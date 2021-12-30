package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.TestStory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * @param story
     * @return cn.master.tsim.entity.TestStory
     */
    TestStory saveStory(HttpServletRequest request, TestStory story);

    /**
     * 查询需求数据
     *
     * @param description 需求内容
     * @param workDate    需求时间
     * @param proId       项目id
     * @return cn.master.tsim.entity.TestStory
     */
    TestStory getStory(String description, String workDate, String proId);

    List<TestStory> checkUniqueStory(TestStory story);

    TestStory searchStoryById(String storyId);

    /**
     * 更新需求状态
     *
     * @param storyId story id
     * @return cn.master.tsim.entity.TestStory
     */
    TestStory updateStory(String storyId);

    List<TestStory> listStoryByProjectId(String projectId);

    List<TestStory> listStory();

    /**
     * 上传需求文件
     *
     * @param request HttpServletRequest
     * @param file    文件
     * @return cn.master.tsim.common.ResponseResult
     */
    ResponseResult upload(HttpServletRequest request, MultipartFile file);

    /**
     * 下载需求文件
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param fileName 文件名称
     * @param uuidName uuid名称
     * @return cn.master.tsim.common.ResponseResult
     */
    ResponseResult downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String uuidName);
}
