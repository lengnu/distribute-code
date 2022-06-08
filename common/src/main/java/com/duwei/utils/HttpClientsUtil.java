package com.duwei.utils;

import com.duwei.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: drone-client
 * @BelongsPackage: com.duwei.utils
 * @Author: duwei
 * @Date: 2022/6/7 15:10
 * @Description: HttpClient工具类
 */
@Slf4j
public class HttpClientsUtil {

    /**
     * 发送get请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  请求url
     * @return
     */
    public static R get(@NotNull String url, Map<String, String> headers, Map<String, String> params) {
        //1.获取http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.将请求参数转化查询字符串
        List<String> queryStringList = new ArrayList<>();
        String queryString = null;
        if (params != null) {
            params.forEach((key, value) -> {
                queryStringList.add(key + "=" + value);
            });
            queryString = String.join("&", queryStringList);
        }

        //3.拼接url
        if (queryString != null) {
            url += queryString;
        }

        //4.创建get请求
        HttpGet httpGet = new HttpGet(url);

        //5.设置请求头
        if (headers != null) {
            headers.forEach(httpGet::setHeader);
        }

        //6.设置响应模型
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);

            //对请求进行解析
            HttpEntity entity = response.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            R r = mapper.readValue(EntityUtils.toString(entity), R.class);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "请求错误");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static R get(@NotNull String url)
    {
        return get(url,null,null);
    };

    public static R get(@NotNull String url,Map<String,String > params)
    {
        return get(url,null,params);
    };

    /**
     * 发送post请求，以json格式传输
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  请求参数
     * @return
     */
    public static R post(String url, Map<String, String> headers, Object params) {

        //1.获取Http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.创建一个POST请求
        HttpPost httpPost = new HttpPost(url);


        CloseableHttpResponse response = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (params != null) {
                //3.转换JSON数据
                String jsonParams = mapper.writeValueAsString(params);

                //4.将数据放入请求体中
                StringEntity entity = new StringEntity(jsonParams, "UTF-8");

                //5.设置数据类型为json
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json;charset=utf8");
            }

            //6.填充请求头
            if (headers != null) {
                headers.forEach(httpPost::setHeader);
            }

            //6.创建响应
            response = httpClient.execute(httpPost);

            //7.获取请求体
            String message = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            //8.转换为通用结果返回
            return mapper.readValue(message, R.class);

        } catch (Exception e) {
            log.error(e.getMessage());
            return R.error(500, e.getMessage());
        } finally {
            //关闭资源
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public static R post(@NotNull String url) {
        return post(url, null, null);
    }

    public static R post(@NotNull String url,Object params) {
        return post(url, null, params);
    }

}
