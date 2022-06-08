package com.duwei.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.model.bo
 * @Author: duwei
 * @Date: 2022/6/8 15:18
 * @Description: 文件BO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileBO {
    /**
     * 文件名
     */
    private String name;
    /**
     * 大小(字节)
     */
    private long size;
    /**
     * 编码节点IP
     */
    private String ownerIp;
}
