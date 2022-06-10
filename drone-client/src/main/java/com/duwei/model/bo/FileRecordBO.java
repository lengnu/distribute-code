package com.duwei.model.bo;

import lombok.Data;

import java.math.BigInteger;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.model.bo
 * @Author: duwei
 * @Date: 2022/6/9 11:20
 * @Description: TODO
 */
@Data
public class FileRecordBO {
    private String file_name;

    private BigInteger data_shards;

    private BigInteger parity_shards;

    private String mapping_information;

    private String file_owner;

    private String file_size;
}