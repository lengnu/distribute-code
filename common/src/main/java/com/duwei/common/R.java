package com.duwei.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.common
 * @Author: duwei
 * @Date: 2022/6/8 9:09
 * @Description: 通用结果返回类
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class R {
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回提示信息
     */
    private String message;
    /**
     * 数据
     */
    private Map<String,Object> data;

    private R(){

    };

    /**
     * 成功静态方法
     * @return
     */
    public static R ok(){
        R r = new R();
        r.setCode(Constant.CODE_OK);
        r.setSuccess(true);
        r.setMessage(Constant.MESSAGE_OK);
        return r;
    }

    /**
     * 失败静态方法
     * @return
     */
    public static R error(Integer code,String message){
        R r = new R();
        r.setCode(code);
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }


    /**
     * 向结果中添加一条记录
     * @param key
     * @param value
     * @return
     */
    public R data(String key,Object value){
         this.data.put(key,value);
         return this;
    }

    public R data(Map<String,Object> data){
        this.setData(data);
        return this;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

}
