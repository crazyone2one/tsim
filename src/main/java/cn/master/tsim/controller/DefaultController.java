package cn.master.tsim.controller;

import cn.master.tsim.entity.Tester;
import cn.master.tsim.mapper.CommonMapper;
import cn.master.tsim.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Created by 11's papa on 2021/09/23
 * @version 1.0.0
 */
@Slf4j
@Controller
public class DefaultController {
    private final CommonMapper commonMapper;

    @Autowired
    public DefaultController(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    @RequestMapping({"", "/", "/index"})
    public String index(Model model, HttpServletRequest request) {
        Tester user = (Tester) request.getSession().getAttribute("account");
        model.addAttribute("account", user);
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
                "t1 LEFT JOIN ( SELECT work_date, COUNT( id ) c FROM test_bug {whereSQL} GROUP BY work_date ) issueCount ON issueCount.work_date = t1.date ORDER BY t1.date) t2";
        // 新增bug
        final List<Map<String, Object>> newBugCount = commonMapper.findMapBySql(countQuery.replace("{whereSQL}","where bug_status='1'"));
        model.addAttribute("newBugCount", JacksonUtils.convertToString(getSeries(newBugCount)));
        return "dashboard";
    }

    private List<Map<String, Object>> getSeries(List<Map<String, Object>> xAxData) {
        List<Map<String, Object>> xAxi = new ArrayList<>(xAxData);
        for (Map<String, Object> objectMap : xAxi) {
            String dateString = objectMap.get("data").toString();
            objectMap.put("data", dateString.split(","));
        }
        return xAxi;
    }
}
