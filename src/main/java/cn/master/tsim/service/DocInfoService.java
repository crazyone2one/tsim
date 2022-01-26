package cn.master.tsim.service;

import cn.master.tsim.entity.DocInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    /**
     * description: 保存数据 <br>
     *
     * @param request HttpServletRequest
     * @param docInfo 信息
     * @return cn.master.tsim.entity.DocInfo
     * @author 11's papa
     */
    DocInfo saveDocInfo(HttpServletRequest request, Map<String, String> docInfo);

    /**
     * description: 根据id查询 <br>
     *
     * @param id id
     * @return cn.master.tsim.entity.DocInfo
     * @author 11's papa
     */
    DocInfo queryDocById(String id);

    /**
     * description: 自定义查询 <br>
     *
     * @param wrapper QueryWrapper
     * @return cn.master.tsim.entity.DocInfo
     * @author 11's papa
     */
    DocInfo queryDocInfo(QueryWrapper<DocInfo> wrapper);
}
