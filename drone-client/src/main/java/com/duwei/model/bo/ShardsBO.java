package com.duwei.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.model.bo
 * @Author: duwei
 * @Date: 2022/6/8 15:14
 * @Description: 切片数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShardsBO {
    /**
     * 数据分片
     */
    private int dataShards;
    /**
     *奇偶校验分片
     */
    private int parityShards;
}
