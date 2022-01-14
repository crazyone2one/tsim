package cn.master.tsim.service;

import cn.master.tsim.entity.PlanStoryRef;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 测试计划与测试需求关联关系表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-01-14
 */
public interface PlanStoryRefService extends IService<PlanStoryRef> {
    void addRefItem(String planId, String storyId);
}
