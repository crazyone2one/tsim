package cn.master.tsim.common;

import cn.master.tsim.entity.Tester;

import java.util.*;

/**
 * @author Created by 11's papa on 2021/10/12
 * @version 1.0.0
 */
public class Constants {

    public static Map<String, Tester> userMaps = new LinkedHashMap<>();
    public static List<String> projectNames = new LinkedList<>();
    /**
     * 任务完成状态
     */
    public static final HashMap<String, String> FINISH_STATUS = new HashMap<String, String>() {{
        put("0", "已完成");
        put("1", "进行中");
        put("2", "待回测");
        put("3", "已回测");
    }};
    /**
     * 任务交付状态
     */
    public static final HashMap<String, String> DELIVERY_STATUS = new HashMap<String, String>() {{
        put("0", "是");
        put("1", "否");
        put("2", "不确定");
    }};
}
