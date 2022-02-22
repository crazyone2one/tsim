package cn.master.tsim.service;

import cn.master.tsim.entity.CaseReviewRef;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
public interface CaseReviewRefService extends IService<CaseReviewRef> {

    void saveRef(HttpServletRequest request);

    Map<String, Integer> queryCaseReviewRef(String reviewId);

    List<CaseReviewRef> listRef(HttpServletRequest request);
}
