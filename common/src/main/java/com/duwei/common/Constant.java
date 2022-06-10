package com.duwei.common;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.common
 * @Author: duwei
 * @Date: 2022/6/8 9:05
 * @Description: 常量类
 */
public class Constant {
    public static final String SERVER_ADDRESS = "http://10.193.173.193:8080";
    public static final String ADD_NODE = SERVER_ADDRESS + "/node/addNode";

    public static final String ADD_FILE = SERVER_ADDRESS + "/file/addFile";

    public static final String ADD_FILE_RECORD = SERVER_ADDRESS + "/contract/distributedEncoding/addFile";

    public static final String GET_FILE_RECORD_BY_NAME = SERVER_ADDRESS + "/contract/distributedEncoding/getFileByName";

    public static final Integer CODE_OK = 200;
    public static final String  MESSAGE_OK = "成功";

   // public static final String FILE_PREFIX = "/opt/module/workSpace/";
    public static final String FILE_PREFIX = "";

    public static final int LISTEN_PORT = 6666;

}
