package com.duwei.test;

import com.duwei.common.Constant;
import com.duwei.common.R;
import com.duwei.utils.HttpClientsUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei
 * @Author: duwei
 * @Date: 2022/6/8 9:38
 * @Description: 文件测试
 */
public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        String url = Constant.SERVER_ADDRESS + "/file/getAllFiles";

        R r = HttpClientsUtil.get(url, null, null);
        System.out.println(r);


    }
}
