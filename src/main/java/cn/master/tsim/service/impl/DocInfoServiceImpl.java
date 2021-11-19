package cn.master.tsim.service.impl;

import cn.master.tsim.entity.DocInfo;
import cn.master.tsim.mapper.DocInfoMapper;
import cn.master.tsim.service.DocInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    public DocInfo saveDocInfo(HttpServletRequest request, DocInfo docInfo) {
        DocInfo build = DocInfo.builder().docName(docInfo.getDocName())
                .docFlag(docInfo.getDocFlag()).docPath(docInfo.getDocPath()).delFlag(0)
                .createDate(new Date()).build();
        baseMapper.insert(build);
        return build;
    }
}
