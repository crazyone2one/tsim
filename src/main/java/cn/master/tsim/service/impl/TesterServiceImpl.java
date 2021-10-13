package cn.master.tsim.service.impl;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.TesterMapper;
import cn.master.tsim.service.SystemService;
import cn.master.tsim.service.TesterService;
import cn.master.tsim.util.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-11
 */
@Service
public class TesterServiceImpl extends ServiceImpl<TesterMapper, Tester> implements TesterService {

    @Autowired
    private SystemService service;

    @Override
    public ResponseResult login(Tester tester) {
        QueryWrapper<Tester> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Tester::getAccount, tester.getAccount()).eq(Tester::getPassword,tester.getPassword());
        final Tester result = baseMapper.selectOne(wrapper);
        if (Objects.isNull(result)) {
            return ResponseUtils.error("用户名或密码错误");
        }
        return ResponseUtils.success(result);
    }

    @Override
    public ResponseResult register(Tester tester) {
        QueryWrapper<Tester> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotBlank(tester.getAccount()), Tester::getAccount, tester.getAccount());
        wrapper.lambda().eq(StringUtils.isNotBlank(tester.getUsername()), Tester::getUsername, tester.getUsername());
        final Tester result = baseMapper.selectOne(wrapper);
        if (Objects.nonNull(result)) {
            return ResponseUtils.error("用户已存在");
        }
        final Tester build = Tester.builder().account(tester.getAccount())
                .username(tester.getUsername())
                .password(tester.getPassword())
                .delFlag("0").build();
        baseMapper.insert(build);
        service.refreshUserMap();
        return ResponseUtils.success(build);
    }
}
