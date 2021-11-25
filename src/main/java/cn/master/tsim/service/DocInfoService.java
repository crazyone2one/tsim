package cn.master.tsim.service;

import cn.master.tsim.entity.DocInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 文档信息表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-11-19
 */
public interface DocInfoService extends IService<DocInfo> {

    DocInfo saveDocInfo(HttpServletRequest request, DocInfo docInfo);
    DocInfo saveDocInfo(HttpServletRequest request, Map<String,String> docInfo);
}
