package com.duwei.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.utils
 * @Author: duwei
 * @Date: 2022/6/9 9:23
 * @Description: JSON解析类
 */
public class JsonParseUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 对字节数组进行类型解析
     * @param json
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse(byte[] json,Class<T> tClass) throws IOException {
        return mapper.readValue(json,tClass);
    }

    /**
     *对字符串进行解析
     * @param json
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse(String json,Class<T> tClass) throws IOException {
        return mapper.readValue(json,tClass);
    }

    /**
     * 将对象转化为json字符串
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String stringify(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }


    /**
     * 将对象转化为json字符串对于的数组
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static byte[] byteify(Object object) throws JsonProcessingException {
        return mapper.writeValueAsBytes(object);
    }


}
