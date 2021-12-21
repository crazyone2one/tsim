package cn.master.tsim.util;

import cn.master.tsim.annotation.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author Created by 11's papa on 2021/12/21
 * @version 1.0.0
 */
public class ValidateUtils {

    public  static boolean validateFiled(Object object) {
        boolean flag = true;
        if (Objects.isNull(object)) {
            throw new RuntimeException("object is null");
        }
        try {
            // 获取当前对象属性
            final Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(NotNull.class)) {
                    field.setAccessible(true);
                    final Object value = field.get(object);
                    final NotNull annotation = field.getAnnotation(NotNull.class);
//                没有设置当前注解，跳过，不用校验
                    if (Objects.isNull(annotation)) {
                        continue;
                    }
                    if (StringUtils.isBlank(String.valueOf(value))) {
                        flag = false;
                    }
                    if (Objects.isNull(value)) {
                        flag = false;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
