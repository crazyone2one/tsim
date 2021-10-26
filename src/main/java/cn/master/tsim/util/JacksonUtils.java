package cn.master.tsim.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Created by 11's papa on 2021/09/23
 * @version 1.0.0
 */
public class JacksonUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
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
     * @param object
     * @return java.lang.String
     */
    public static String convertToString(Object object) {
        String value = "";
        try {
            value = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 字符串转自定义对象
     *
     * @param source
     * @param clazz
     * @return T
     */
    public static <T> T convertToClass(String source, Class<T> clazz) {
        if (StringUtils.isBlank(source) || Objects.isNull(clazz)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(source, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T jsonToObject(String responseJson, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(responseJson, valueTypeRef);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
