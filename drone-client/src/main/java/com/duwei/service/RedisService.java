package com.duwei.service;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.handler
 * @Author: duwei
 * @Date: 2022/6/8 16:46
 * @Description: Redis操作类
 */
@Slf4j
public class RedisService {
    /**
     * jedis连接
     */
    private static Jedis jedis;

    {
        InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("redis.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            String ip = properties.getProperty("redis.host");
            int port = Integer.parseInt(properties.getProperty("redis.port"));
            jedis = new Jedis(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置String类型数据
     * @param key
     * @param value
     */
    public void setString(String key,String value){
        log.info("Redis 开始写数据");
        jedis.set(key,value);
        log.info("Redis 数据写入完毕");
    }

    /**
     * 得到key对于的值
     * @param key
     * @return
     */
    public String getString(String key){
        return jedis.get(key);
    }

}
