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
    /***
     * description: 查询项目相关问题单数据 <br>
     *
     * @param projectId 项目id
     * @return java.util.List<cn.master.tsim.entity.TestBug>
     * @author 11's papa
     */
    List<TestBug> listBugByProjectId(String projectId);

    TestBug addBug(HttpServletRequest request, TestBug testBug);

    /***
     * description: 分页查询数据 <br>
     *
     * @param request HttpServletRequest
     * @param pageCurrent 当前页面数据
     * @param pageSize 每页展示数量

     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.TestBug>
     * @author 11's papa
     */
    IPage<TestBug> pageListBug(HttpServletRequest request, Integer pageCurrent, Integer pageSize);

    Map<String, Integer> bugMapByProject(String projectId, String storyId, String bugStatus);

    /**
     * 查询bug信息
     *
     * @param id bug id
     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */
    ResponseResult getBugById(String id);

    /***
     * description:  批量删除问题单数据
     *
     * @param request HttpServletRequest

     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */
    ResponseResult batchDelete(HttpServletRequest request);
}
