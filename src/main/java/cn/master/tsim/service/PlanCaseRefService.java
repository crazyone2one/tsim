package cn.master.tsim.service;

import cn.master.tsim.entity.PlanCaseRef;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    /**
     * 更新数据
     *
     * @param request HttpServletRequest
     * @param params  参数
     * @return cn.master.tsim.entity.PlanCaseRef
     */
    PlanCaseRef uploadItemRef(HttpServletRequest request, Map<String, Object> params);

    /**
     * 查询关联数据
     *
     * @param request HttpServletRequest
     * @param params  参数
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.tsim.entity.PlanCaseRef>
     */
    IPage<PlanCaseRef> loadRefRecords(HttpServletRequest request, Map<String, Object> params);
}
