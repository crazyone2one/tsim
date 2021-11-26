package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    /**
     * 上传文件
     *
     * @param request HttpServletRequest
     * @param file    上传的文件
     * @return cn.master.tsim.common.ResponseResult
     * @author 11's papa
     */
    ResponseResult storeFile(HttpServletRequest request, MultipartFile file);

    /**
     * 文件下载功能
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param fileName 文件名称
     * @param uuidName 文件上传时生成的uuid名称
     */
    void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String uuidName);
}
