package com.duwei.common;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.common
 * @Author: duwei
 * @Date: 2022/6/8 9:05
 * @Description: 常量类
 */
public class Constant {
    public static final String SERVER_ADDRESS = "http://localhost:8080";
    public static final String ADD_NODE = SERVER_ADDRESS + "/node/addNode";

    public static final String ADD_FILE = SERVER_ADDRESS + "/file/addFile";

    public static final Integer CODE_OK = 200;
    public static final String  MESSAGE_OK = "成功";

    public static final int LISTEN_PORT = 6666;

}
