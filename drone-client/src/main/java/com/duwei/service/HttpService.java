package com.duwei.service;

import com.duwei.common.Constant;
import com.duwei.common.R;
import com.duwei.model.bo.FileBO;
import com.duwei.utils.HttpClientsUtil;


/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.httpservice
 * @Author: duwei
 * @Date: 2022/6/8 15:16
 * @Description: 通过http请求获取节点列表，文件信息等
 */
public class HttpService {
    /**
     * 发送编码请求得到所需要的节点列表
     * @param fileBO
     * @return
     */
    public  R addFile(FileBO fileBO){
        return HttpClientsUtil.post(Constant.ADD_FILE, fileBO);
    }

}
