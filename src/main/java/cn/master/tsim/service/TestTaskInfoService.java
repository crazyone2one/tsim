package cn.master.tsim.service;

import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestTaskInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 任务汇总表 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
public interface TestTaskInfoService extends IService<TestTaskInfo> {

    IPage<TestTaskInfo> taskInfoPage(TestTaskInfo taskInfo, Integer pageCurrent, Integer pageSize);

    TestTaskInfo addItem(Project project, HttpServletRequest request, String workDate);
}
