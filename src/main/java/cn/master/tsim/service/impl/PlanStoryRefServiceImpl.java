package cn.master.tsim.service.impl;

import cn.master.tsim.entity.PlanStoryRef;
import cn.master.tsim.mapper.PlanStoryRefMapper;
import cn.master.tsim.service.PlanStoryRefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 测试计划与测试需求关联关系表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2022-01-14
 */
@Service
public class PlanStoryRefServiceImpl extends ServiceImpl<PlanStoryRefMapper, PlanStoryRef> implements PlanStoryRefService {

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addRefItem(String planId, String storyId) {
        PlanStoryRef.PlanStoryRefBuilder builder = PlanStoryRef.builder().planId(planId).storyId(storyId);
        baseMapper.insert(builder.build());
    }
}
