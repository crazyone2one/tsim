package cn.master.tsim.service;

/**
 * @author Created by 11's papa on 2021/10/12
 * @version 1.0.0
 */
public interface SystemService {
    /**
     * 初始化用户缓存
     */
    void initUserMap();

    /**
     * 刷新用户缓存
     */
    void refreshUserMap();

    boolean validate(Object object);

    /**
     * 初始化项目名称缓存
     */
    void initProjectName();

    /**
     * 刷新项目名称缓存
     */
    void refreshProjectName();
}
