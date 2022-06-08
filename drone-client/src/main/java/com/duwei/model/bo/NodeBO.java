package com.duwei.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.entity
 * @Author: duwei
 * @Date: 2022/6/8 10:13
 * @Description: 节点BO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeBO {
    private String ip;
    private String host;
    private String  role;
}
