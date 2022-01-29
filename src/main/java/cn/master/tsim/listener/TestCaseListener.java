package cn.master.tsim.listener;

import cn.master.tsim.common.ExcelImportErrDto;
import cn.master.tsim.entity.TestCase;
import cn.master.tsim.service.TestCaseService;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.StreamUtils;
import cn.master.tsim.util.ValidateUtils;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Created by 11's papa on 2021/10/14
 * @version 1.0.0
 */
@Slf4j
public class TestCaseListener implements ReadListener<TestCase> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private final List<TestCase> cachedDataList = new ArrayList<>(BATCH_COUNT);
    private final List<ExcelImportErrDto> errorDataList = new LinkedList<>();

    private final TestCaseService caseService;

    public TestCaseListener(TestCaseService caseService) {
        this.caseService = caseService;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    @Override
    public void invoke(TestCase data, AnalysisContext context) {
        Map<String, String> validateResults = ValidateUtils.validateFiled(data);
        if (Objects.equals(validateResults.get("code"), "0")) {
            Map<Integer, String> map = new LinkedHashMap<>();
            map.put((context.readRowHolder().getRowIndex()), validateResults.get("msg"));
            ExcelImportErrDto dto = new ExcelImportErrDto(data, map);
            errorDataList.add(dto);
        } else {
            log.info("解析到一条数据:{}", JSON.toJSONString(data));
            cachedDataList.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                cachedDataList.clear();
            }
        }
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        // 过滤文件中未导入的数据
        final List<TestCase> collect = cachedDataList.stream().filter(c -> !c.isRefFlag()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            int imported = cachedDataList.size();
            int unImport = cachedDataList.size() - collect.size();
            log.info("{}条数据，其中{}条数据已导入，开始存储数据库！", imported, unImport);
            caseService.importCase(request, collect);
            log.info("存储数据库成功！");
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        if (CollectionUtils.isNotEmpty(errorDataList)) {
            List<String> resultList = new LinkedList<>();
            StreamUtils.forEach(errorDataList,(index,value)->{
                final Map<Integer, String> mapMap = JacksonUtils.convertValue(value.getCellMap(), new TypeReference<Map<Integer, String>>() {});
                for (Map.Entry<Integer, String> entry : mapMap.entrySet()) {
                    resultList.add("第[" + entry.getKey() + "]行解析数据出错.--" + entry.getValue());
                }
            });
            throw new ExcelAnalysisException(resultList.toString());
        }
    }
}
