package cn.master.tsim.controller;


import cn.master.tsim.common.Constants;
import cn.master.tsim.entity.TestTaskInfo;
import cn.master.tsim.service.TestTaskInfoService;
import cn.master.tsim.util.DateUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 任务汇总表 前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2021-09-30
 */
@Controller
@RequestMapping("/task")
public class TestTaskInfoController {
    private final TestTaskInfoService taskInfoService;

    @Autowired
    public TestTaskInfoController(TestTaskInfoService taskInfoService) {
        this.taskInfoService = taskInfoService;
    }

    @GetMapping("/list")
    public String taskList(TestTaskInfo taskInfo,Model model,@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                           @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        final IPage<TestTaskInfo> iPage = taskInfoService.taskInfoPage(taskInfo, pageCurrent, pageSize);
        model.addAttribute("iPage", iPage);
        model.addAttribute("redirecting", "/task/list?pageCurrent=");
        model.addAttribute("monthList", DateUtils.currentYearMonth());
        model.addAttribute("users", Constants.userMaps);
        return "task/task_info_list";
    }
}

