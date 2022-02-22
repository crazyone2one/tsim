package cn.master.tsim.service.impl;

import cn.master.tsim.entity.CaseReviewRef;
import cn.master.tsim.mapper.CaseReviewRefMapper;
import cn.master.tsim.service.CaseReviewRefService;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class CaseReviewRefServiceImpl extends ServiceImpl<CaseReviewRefMapper, CaseReviewRef> implements CaseReviewRefService {

    private final TestCaseService testCaseService;

    public CaseReviewRefServiceImpl(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveRef(HttpServletRequest request) {
        String reviewId = request.getParameter("reviewId");
        String caseIds = request.getParameter("caseId");
        final List<String> idList = JacksonUtils.jsonToObject(caseIds, new TypeReference<List<String>>() {
        });
        if (CollectionUtils.isNotEmpty(idList)) {
            idList.forEach(t -> {
                CaseReviewRef build = CaseReviewRef.builder().reviewId(reviewId).caseId(t).reviewStatus(0).build();
                baseMapper.insert(build);
            });
        }
    }

    @Override
    public Map<String, Integer> queryCaseReviewRef(String reviewId) {
        Map<String, Integer> map = new LinkedHashMap<>();
        QueryWrapper<CaseReviewRef> wrapper = new QueryWrapper<>();
        wrapper.select("review_status", "COUNT(1) as c");
        wrapper.lambda().eq(CaseReviewRef::getReviewId, reviewId).groupBy(CaseReviewRef::getReviewStatus);
        List<Map<String, Object>> maps = baseMapper.selectMaps(wrapper);
        if (CollectionUtils.isNotEmpty(maps)) {
            maps.forEach(m -> {
                map.put(m.get("review_status").toString(), Integer.valueOf(m.get("c").toString()));
            });
        }
        return map;
    }

    @Override
    public List<CaseReviewRef> listRef(HttpServletRequest request) {
        String reviewId = request.getParameter("reviewId");
        List<CaseReviewRef> reviewRefs = baseMapper.queryByReviewId(reviewId);
        reviewRefs.forEach(t->{
            t.setTestCase(testCaseService.getById(t.getCaseId()));
        });
        return reviewRefs;
    }
}