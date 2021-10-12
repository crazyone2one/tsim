package cn.master.tsim.service.impl;

import cn.master.tsim.common.Constants;
import cn.master.tsim.mapper.TesterMapper;
import cn.master.tsim.service.SystemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Created by 11's papa on 2021/10/12
 * @version 1.0.0
 */
@Service
public class SystemServiceImpl implements SystemService {
    private final TesterMapper testerMapper;

    @Autowired
    public SystemServiceImpl(TesterMapper testerMapper) {
        this.testerMapper = testerMapper;
    }

    @Override
    @PostConstruct
    public void initUserMap() {
        testerMapper.selectList(new QueryWrapper<>()).forEach(u->{
            Constants.userMaps.put(u.getAccount(), u.getUsername());
        });
    }

    @Override
    public void refreshUserMap() {
        Constants.userMaps.clear();
        initUserMap();
    }
}
