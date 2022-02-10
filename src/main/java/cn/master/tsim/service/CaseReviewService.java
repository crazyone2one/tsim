package cn.master.tsim.service;

import cn.master.tsim.entity.CaseReview;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
public interface CaseReviewService extends IService<CaseReview> {
    /**
     * description: 查询数据 <br>
     *
     * @param request HttpServletRequest
     * @param pageCurrent 页数
     * @param pageSize 每页数量
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.CaseReview>
     * @author 11's papa
     */
    IPage<CaseReview> dataList(HttpServletRequest request, Integer pageCurrent, Integer pageSize);

    CaseReview saveOrUpdate(HttpServletRequest request);
}
