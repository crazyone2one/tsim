package cn.master.tsim.mapper;

import cn.master.tsim.entity.CaseReviewRef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-02-09
 */
@Mapper
public interface CaseReviewRefMapper extends BaseMapper<CaseReviewRef> {

    @Select("SELECT review_status as s, COUNT(1) as c FROM t_case_review_ref where review_id=#{reviewId} GROUP BY review_status;")
    List<Map<String, String>> queryData(String reviewId);

    @Select("SELECT * FROM t_case_review_ref where review_id=#{reviewId} and review_status!='2';")
    List<CaseReviewRef> queryByReviewId(String reviewId);
}
