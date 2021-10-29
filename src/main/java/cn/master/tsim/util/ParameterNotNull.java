package cn.master.tsim.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Created by 11's papa on 2021/10/29
 * @version 1.0.0
 */
@Target(value = ElementType.FIELD) //注解的范围，这里选ElementType.FIELD，表示作用在属性上
@Retention(value = RetentionPolicy.RUNTIME) // 注解的生命周期，RUNTIME代表编译，运行的时候注解都存在，能影响到程序的逻辑
public @interface ParameterNotNull {
    String message() default "";
}
