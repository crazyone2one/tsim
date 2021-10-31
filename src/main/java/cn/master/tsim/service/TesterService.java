package cn.master.tsim.service;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Tester;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-10-11
 */
public interface TesterService extends IService<Tester> {

    ResponseResult login(Tester tester);

    /**
     * 注册用户
     *
     * @param tester
     * @return cn.master.tsim.common.ResponseResult
     */
    ResponseResult register(Tester tester);

    List<Tester> testList();
}
