package cn.master.tsim.service;

import cn.master.tsim.common.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author by 11's papa on 2022年02月07日
 * @version 1.0.0
 */
public interface FileStorageService {
    /***
     * description: 文件保存 <br>
     *
     * @param request HttpServletRequest
     * @param file 待存储的文件

     * @return cn.master.tsim.common.UploadFileResponse
     * @author 11's papa
     */
    UploadFileResponse storeFile(HttpServletRequest request, MultipartFile file);

    /**
     * description: 该方法将以资源对象的形式返回存储或上传的文件 <br>
     *
     * @param filename 文件名称
     * @return org.springframework.core.io.Resource
     * @author 11's papa
     */
    Resource loadFileAsResource(String filename);

    /**
     * description: 删除文件：删除数据库中的记录和对应的文件（若存在） <br>
     *
     * @param docId 文件数据id
     * @return boolean
     * @author 11's papa
     */
    boolean deleteFile(String docId);
}
