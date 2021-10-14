package cn.master.tsim.listener;

import cn.master.tsim.entity.TestCase;
import cn.master.tsim.service.TestCaseService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Created by 11's papa on 2021/10/14
 * @version 1.0.0
 */
@Slf4j
public class TestCaseListener extends AnalysisEventListener<TestCase> {

    private static final int BATCH_COUNT = 3000;
    List<TestCase> list = new ArrayList<>();

    private final TestCaseService caseService;

    public TestCaseListener(TestCaseService caseService) {
        this.caseService = caseService;
    }

    @Override
    public void invoke(TestCase data, AnalysisContext context) {
        log.info("invoke test case {}", data.toString());
        list.add(data);
//        达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (Objects.equals(list.size(), BATCH_COUNT)) {
            saveData();
//            存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(list)) {
            saveData();
            list.clear();
            log.info("所有数据解析完成！");
        }
    }

    private void saveData() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        for (int i = 0; i < list.size(); i++) {
            caseService.saveCase(list.get(i), request);
            log.info("第" + i + "条");
        }
    }
}
