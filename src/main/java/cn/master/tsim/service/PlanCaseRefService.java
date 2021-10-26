package cn.master.tsim.service;

import cn.master.tsim.entity.PlanCaseRef;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-26
 */
public interface PlanCaseRefService extends IService<PlanCaseRef> {

    /**
     * 测试计划关联测试用例
     *
     * @param planId  测试计划id
     * @param caseRef 待关联的测试用例id集合
     */
    void addItemRef(String planId, List<String> caseRef);
}
