package cn.master.tsim.controller;

import cn.master.tsim.common.Constants;
import cn.master.tsim.common.ResponseResult;
import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.CommonMapper;
import cn.master.tsim.service.TestStoryService;
import cn.master.tsim.service.TesterService;
import cn.master.tsim.util.JacksonUtils;
import cn.master.tsim.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final TesterService userService;
    @Autowired
    TestStoryService storyService;

    @Autowired
    public DefaultController(CommonMapper commonMapper, TesterService userService) {
        this.commonMapper = commonMapper;
        this.userService = userService;
    }

    @RequestMapping({"", "/", "/index", "/dashboard"})
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
        String xAxisSql = "SELECT GROUP_CONCAT(date ORDER BY date) result from (SELECT DATE_FORMAT( DATE_SUB(NOW(),INTERVAL ac - 1 MONTH),'%Y-%m' ) AS date \n" +
                "FROM ( SELECT @ai /*'*/ := /*'*/@ai + 1 AS ac FROM ( SELECT 1  UNION SELECT  2   UNION SELECT 3 ) ac1, (SELECT 1  UNION SELECT 2  UNION SELECT 3  UNION SELECT 4 ) ac2,(SELECT @ai /*'*/ := /*'*/ 0 )xc0) a ORDER BY date ) t1";
        final List<Map<String, Object>> xAxData = commonMapper.findMapBySql(xAxisSql);
        List<Map<String, Object>> xAxi = getSeries(xAxData);
        model.addAttribute("xAxisList", JacksonUtils.convertToString(xAxi));
        return "index";
    }

    private List<Map<String, Object>> getSeries(List<Map<String, Object>> xAxData) {
        List<Map<String, Object>> xAxi = new ArrayList<>(xAxData);
        for (Map<String, Object> objectMap : xAxi) {
            String dateString = objectMap.get("result").toString();
            objectMap.put("result", dateString.split(","));
        }
        return xAxi;
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

    @RequestMapping("/getAnalysis")
    @ResponseBody
    public ResponseResult getAnalysis() {
        Map<String, Object> resultAttributes = new LinkedHashMap<>();
        Map<String, Object> caseAttributes = new LinkedHashMap<>();
        Map<String, Object> bugAttributes = new LinkedHashMap<>();
        try {
            String caseSql = "SELECT GROUP_CONCAT(count ORDER BY date) result FROM( SELECT t1.date, IFNULL(issueCount.c, 0) count FROM( SELECT DATE_FORMAT( DATE_SUB(NOW(), INTERVAL ac - 1 MONTH), '%Y-%m') AS date FROM ( SELECT @ai /*'*/ := /*'*/ @ai + 1 AS ac FROM ( SELECT 1 UNION SELECT 2 UNION SELECT 3) ac1, ( SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 ) ac2, (SELECT @ai /*'*/ := /*'*/ 0) xc0 ) a ORDER BY date ) t1 LEFT JOIN ( SELECT DATE_FORMAT(t1.create_date, '%Y-%m') cdt, COUNT(1) c FROM {from} t1 INNER JOIN t_project t2 on t2.id=t1.project_id {whereSQL} GROUP BY cdt ) issueCount ON issueCount.cdt = t1.date ORDER BY t1.date ) t2;";
            Constants.projectNames.forEach(temp -> {
                final List<Map<String, Object>> tempResultList = commonMapper.findMapBySql(caseSql.replace("{whereSQL}", "where t2.project_name='" + temp + "'").replace("{from}", "t_case"));
                caseAttributes.put(temp, JacksonUtils.convertToString(getSeries(tempResultList)));
                final List<Map<String, Object>> tempBugList = commonMapper.findMapBySql(caseSql.replace("{whereSQL}", "where t2.project_name='" + temp + "'").replace("{from}", "t_bug"));
                bugAttributes.put(temp, JacksonUtils.convertToString(getSeries(tempBugList)));
            });

            resultAttributes.put("case", caseAttributes);
            resultAttributes.put("bug", bugAttributes);
            return ResponseUtils.success(resultAttributes);
        } catch (Exception e) {
            resultAttributes.put("analysisData", new LinkedHashMap<>());
            return ResponseUtils.error(400, e.getMessage(), resultAttributes);
        }
    }
}
