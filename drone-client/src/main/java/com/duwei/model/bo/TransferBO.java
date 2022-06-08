package com.duwei.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.model.bo
 * @Author: duwei
 * @Date: 2022/6/8 17:07
 * @Description: 进行P2P传输的BO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferBO {
    /**
     * 1表示存，2表示取
     */
    private Integer type;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 传输资金进行Base64编码后的字符
     */
    private String data;
}
