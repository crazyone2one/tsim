package cn.master.tsim.service.impl;

import cn.master.tsim.entity.DocInfo;
import cn.master.tsim.mapper.DocInfoMapper;
import cn.master.tsim.service.DocInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 文档信息表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-11-19
 */
@Service
public class DocInfoServiceImpl extends ServiceImpl<DocInfoMapper, DocInfo> implements DocInfoService {

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public DocInfo saveDocInfo(HttpServletRequest request, Map<String, String> docInfo) {
        DocInfo build = DocInfo.builder().docName(docInfo.get("docName")).uuidName(docInfo.get("uuidName"))
                .docFlag(docInfo.get("flag")).docPath(docInfo.get("docPath")).delFlag(0)
                .createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
    }

    @Override
    public DocInfo queryDocById(String id) {
        return baseMapper.selectById(id);
    }
}
