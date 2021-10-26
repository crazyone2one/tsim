package cn.master.tsim.service.impl;

import cn.master.tsim.entity.PlanCaseRef;
import cn.master.tsim.mapper.PlanCaseRefMapper;
import cn.master.tsim.service.PlanCaseRefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-26
 */
@Service
public class PlanCaseRefServiceImpl extends ServiceImpl<PlanCaseRefMapper, PlanCaseRef> implements PlanCaseRefService {

    @Override
    public void addItemRef(String planId, List<String> caseRef) {
        for (String caseId : caseRef) {
            final PlanCaseRef build = PlanCaseRef.builder().planId(planId).caseId(caseId).runStatus(0).runResult(1).build();
            baseMapper.insert(build);
        }
    }
}
