package cn.master.tsim.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * excel数据导入错误封装对象
 * @author by 11's papa on 2022年01月29日
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportErrDto {
    private Object object;
    private Map<Integer,String> cellMap = null;
}
