package cn.master.tsim.controller;

import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Project;
import cn.master.tsim.entity.TestStory;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.CommonMapper;
import cn.master.tsim.service.ProjectService;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.service.TesterService;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import cn.master.tsim.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Created by 11's papa on 2021/09/23
 * @version 1.0.0
 */
@Slf4j
@Controller
public class DefaultController {
    private final CommonMapper commonMapper;
    private final ProjectService projectService;
    private final TesterService userService;
    @Autowired
    TestStoryService storyService;

    @Autowired
    public DefaultController(CommonMapper commonMapper, ProjectService projectService, TesterService userService) {
        this.commonMapper = commonMapper;
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping({"", "/", "/index","/dashboard"})
    public String index(Model model, HttpServletRequest request) {
        Tester user = (Tester) request.getSession().getAttribute("account");
        if (Objects.nonNull(user)) {
            model.addAttribute("account", user);
            return "index";
        }
        return "login";
    }

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {

        /*时间轴，当前月份往前12个月份*/
        String xAxisSql = "SELECT GROUP_CONCAT(date ORDER BY date) data from (SELECT DATE_FORMAT( DATE_SUB(NOW(),INTERVAL ac - 1 MONTH),'%Y-%m' ) AS date \n" +
                "FROM ( SELECT @ai /*'*/ := /*'*/@ai + 1 AS ac FROM ( SELECT 1  UNION SELECT  2   UNION SELECT 3 ) ac1, (SELECT 1  UNION SELECT 2  UNION SELECT 3  UNION SELECT 4 ) ac2,(SELECT @ai /*'*/ := /*'*/ 0 )xc0) a ORDER BY date ) t1";
        final List<Map<String, Object>> xAxData = commonMapper.findMapBySql(xAxisSql);
        List<Map<String, Object>> xAxi = getSeries(xAxData);
        model.addAttribute("xAxisList", JacksonUtils.convertToString(xAxi));
        String countQuery = "SELECT GROUP_CONCAT(count ORDER BY date) data  from (SELECT t1.date,IFNULL( issueCount.c, 0 ) count FROM ( SELECT DATE_FORMAT( DATE_SUB( NOW(), INTERVAL ac - 1 MONTH ), '%Y-%m' ) AS date FROM (SELECT @ai /*'*/ := /*'*/ @ai + 1 AS ac FROM ( SELECT 1 UNION SELECT 2 UNION SELECT 3 ) ac1, ( SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 ) ac2,( SELECT @ai /*'*/ := /*'*/ 0 ) xc0 ) a ORDER BY date )\n" +
                "t1 LEFT JOIN ( SELECT work_date, COUNT( id ) c FROM t_bug {whereSQL} GROUP BY work_date ) issueCount ON issueCount.work_date = t1.date ORDER BY t1.date) t2";
        // 新增bug
        final List<Map<String, Object>> newBugCount = commonMapper.findMapBySql(countQuery.replace("{whereSQL}", "where bug_status='1'"));
        model.addAttribute("newBugCount", JacksonUtils.convertToString(getSeries(newBugCount)));
        return "index";
    }

    private List<Map<String, Object>> getSeries(List<Map<String, Object>> xAxData) {
        List<Map<String, Object>> xAxi = new ArrayList<>(xAxData);
        for (Map<String, Object> objectMap : xAxi) {
            String dateString = objectMap.get("data").toString();
            objectMap.put("data", dateString.split(","));
        }
        return xAxi;
    }

    @GetMapping("/demo")
    public String demo(HttpServletRequest request) {
        return "demo/demo";
    }


    @PostMapping(value = "/addStory")
    @ResponseBody
    public ResponseResult addStory(HttpServletRequest request) {
        try {
            for (int i = 0; i < 10; i++) {
                final List<Project> projects = projectService.findByPartialProjectName("");
                final Project project = projects.get(new Random().nextInt(projects.size()));
                final TestStory build = TestStory.builder().projectId(project.getProjectName()).description("测试需求数据" + StringUtils.randomCode(6)).workDate("2021-10").build();
                storyService.saveStory(request);
            }
            return ResponseUtils.success("数据添加成功", null);
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据添加失败", e.getMessage());
        }
    }

    @GetMapping(value = "/loadUser")
    @ResponseBody
    public ResponseResult loadUser(HttpServletRequest request) {
        try {
            return ResponseUtils.success("数据加载成功", userService.testList());
        } catch (Exception e) {
            return ResponseUtils.error(400, "数据加载失败", e.getMessage());
        }
    }
}
