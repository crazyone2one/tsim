package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.entity.CaseReview;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.CaseReviewMapper;
import cn.master.tsim.service.CaseReviewRefService;
import cn.master.tsim.service.CaseReviewService;
import cn.master.tsim.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
@Slf4j
@Service
public class CaseReviewServiceImpl extends ServiceImpl<CaseReviewMapper, CaseReview> implements CaseReviewService {
    private final CaseReviewRefService reviewRefService;

    public CaseReviewServiceImpl(CaseReviewRefService reviewRefService) {
        this.reviewRefService = reviewRefService;
    }

    @Override
    public IPage<CaseReview> dataList(HttpServletRequest request, Integer pageCurrent, Integer pageSize) {
        Tester user = (Tester) request.getSession().getAttribute("account");
        QueryWrapper<CaseReview> wrapper = new QueryWrapper<>();
        if (!Objects.equals("admin", user.getAccount())) {
            wrapper.lambda().eq(CaseReview::getReviewUser, user.getId());
        }
        Page<CaseReview> page = baseMapper.selectPage(new Page<>(Objects.equals(pageCurrent, 0) ? 1 : pageCurrent, Objects.equals(pageSize, 0) ? 10 : pageSize), wrapper);
        page.getRecords().forEach(t -> {
            t.setReviewUser(Constants.userMaps.get(t.getReviewUser()).getUsername());
            Map<String, Integer> statusMap = reviewRefService.queryCaseReviewRef(t.getId());
            if (MapUtils.isNotEmpty(statusMap)) {
                if (Objects.equals(0, statusMap.get("0")) && Objects.equals(0, statusMap.get("1")) && !Objects.equals(0, statusMap.get("2"))) {
                    t.setFinishStatus(2);
                }
                if (!Objects.equals(0, statusMap.get("1"))) {
                    t.setFinishStatus(1);
                }
                if (!Objects.equals(0, statusMap.get("0")) && Objects.equals(0, statusMap.get("1")) && Objects.equals(0, statusMap.get("2"))) {
                    t.setFinishStatus(0);
                }
            } else {
                t.setFinishStatus(0);
            }
        });
        return page;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public CaseReview saveOrUpdate(HttpServletRequest request) {
        String reviewName = request.getParameter("reviewName");
        String reviewLabel = request.getParameter("reviewLabel");
        String reviewMan = request.getParameter("reviewMan");
        String reviewFinishDate = request.getParameter("reviewFinishDate");
        String reviewRemark = request.getParameter("reviewRemark");
        CaseReview build = CaseReview.builder()
                .reviewName(reviewName).reviewUser(reviewMan).reviewRemark(reviewRemark)
                .finishDate(DateUtils.parseStrToDate(DateUtils.DATEFORMAT_DAY, reviewFinishDate))
                .finishStatus(0).createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
    }
}
