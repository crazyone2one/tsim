package cn.master.tsim.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Created by 11's papa on 2021/10/26
 * @version 1.0.0
 */
public class StreamUtils {

    /**
     * 从HttpServletRequest中获取body中的json格式参数
     *
     * @param request HttpServletRequest
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    public static Map<String, Object> getParamsFromRequest(HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String inputStr;
            while ((inputStr = reader.readLine()) != null) {
                builder.append(inputStr);
            }
            map = JacksonUtils.convertStringToJson(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 自定义for each循环 可使用index
     *
     * @param es
     * @param action
     */
    public static <E> void forEach(Iterable<? extends E> es, BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(es);
        Objects.requireNonNull(action);
        int index = 0;
        for (E e : es) {
            action.accept(index++, e);
        }
    }
}
