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

    /**
     * 分页查询
     *
     * @param request     HttpServletRequest
     * @param pageCurrent 页码
     * @param pageSize    数量
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.TestStory>
     */
    IPage<TestStory> pageList(HttpServletRequest request, Integer pageCurrent, Integer pageSize);

    /**
     * 保存、更新需求数据
     *
     * @param request HttpServletRequest
     * @return cn.master.tsim.entity.TestStory
     */
    ResponseResult saveOrUpdateStory(HttpServletRequest request);

    /**
     * description:  根据需求名称、时间、所属项目，验证需求数据的唯一性<br>
     *
     * @param request HttpServletRequest
     * @return java.util.List<cn.master.tsim.entity.TestStory>
     * @author 11's papa
     */
    List<TestStory> checkUniqueStory(HttpServletRequest request);

    /**
     * description: 查询需求数据 <br>
     *
     * @param storyId 需求数据id
     * @return cn.master.tsim.entity.TestStory
     * @author 11's papa
     */
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

    ResponseResult batchDelete(HttpServletRequest request);

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
