package cn.master.tsim.util;

import cn.master.tsim.annotation.ParamCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Created by 11's papa on 2021/12/21
 * @version 1.0.0
 */
@Slf4j
public class ValidateUtils {

    public static boolean validateFiled(Object object) {
        AtomicBoolean flag = new AtomicBoolean(true);
        if (Objects.isNull(object)) {
            flag.set(false);
            throw new RuntimeException("object is null");
        }
        // 获取当前对象属性
        final Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (field.isAnnotationPresent(ParamCheck.class)) {
                    if (Objects.isNull(fieldValue) || StringUtils.isBlank(String.valueOf(fieldValue))) {
                        flag.set(false);
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return flag.get();
    }
}
