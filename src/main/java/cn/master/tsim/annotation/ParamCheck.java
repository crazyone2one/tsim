package cn.master.tsim.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，用于判断属性不能为空
 *
 * @author Created by 11's papa on 2021/12/21
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME) // 注解的生命周期，RUNTIME代表编译，运行的时候注解都存在，能影响到程序的逻辑
@Target(ElementType.FIELD) //注解的范围，这里选ElementType.FIELD，表示作用在属性上
public @interface ParamCheck {
    /**
     * description:  是否强制校验
     * @return boolean
     * @author 11's papa
     */
    boolean required() default false;
    String errorRequiredMessage() default "该字段不能为空";
}
