package cn.master.tsim.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Created by 11's papa on 2021/09/23
 * @version 1.0.0
 */
@Slf4j
public class JacksonUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许出现单引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // 允许对象忽略json中不存在的属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Ignore null values when writing json.
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Write times as a String instead of a Long so its human readable.
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static Map<String, Object> convertStringToJson(String source) {
        Map<String, Object> objectMap = new LinkedHashMap<>();
//       fix MismatchedInputException
        if (StringUtils.isBlank(source)) {
            return objectMap;
        }
        try {
            objectMap = OBJECT_MAPPER.readValue(source, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return objectMap;
    }

    /**
     * 对象转json格式字符串
     *
     * @param object object
     * @return java.lang.String
     */
    public static String convertToString(Object object) {
        String value = "";
        try {
            value = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("序列化失败", e);
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 字符串转自定义对象
     *
     * @param source 反序列化的JSON串
     * @param clazz  反序列化的目标对象
     * @return T
     */
    public static <T> T convertToClass(String source, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(source, clazz);
        } catch (JsonProcessingException e) {
            log.error("反序列化失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * object 转其他类型
     *
     * @param object       Object
     * @param valueTypeRef {@code new TypeReference<?>}
     * @return T
     */
    public static <T> T convertValue(Object object, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.convertValue(object, valueTypeRef);
        } catch (Exception e) {
            log.error("反序列化失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转对象
     *
     * @param responseJson 源字符串
     * @param valueTypeRef {@code new TypeReference<?>}
     * @return T
     */
    public static <T> T jsonToObject(String responseJson, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(responseJson, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("反序列化失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * description: 验证字符串是否为JSON格式。 <br>
     *
     * @param str
     * @return boolean
     * @author 11's papa
     */
    public static boolean validateJSON(String str) {
        return str.startsWith("{") || str.startsWith("[");
    }
}
