package cn.master.tsim.util;

import cn.master.tsim.annotation.ParamCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Created by 11's papa on 2021/12/21
 * @version 1.0.0
 */
@Slf4j
public class ValidateUtils {

    public static Map<String, String> validateFiled(Object object) {
        Map<String, String> result = new LinkedHashMap<>();
        if (Objects.isNull(object)) {
            throw new RuntimeException("object is null");
        }
        // 获取当前对象属性
        final Field[] fields = object.getClass().getDeclaredFields();
        StringJoiner msg = new StringJoiner("|");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (field.isAnnotationPresent(ParamCheck.class)) {
                    if (Objects.isNull(fieldValue) || StringUtils.isBlank(String.valueOf(fieldValue))) {
                        ParamCheck annotation = field.getAnnotation(ParamCheck.class);
                        msg.add(annotation.errorRequiredMessage());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(msg.toString())) {
            result.put("code", "0");
            result.put("msg", msg.toString());
        }
        return result;
    }
}
