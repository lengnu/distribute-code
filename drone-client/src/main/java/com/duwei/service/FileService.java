package com.duwei.service;

import com.duwei.common.R;
import com.duwei.erasure.Encoder;
import com.duwei.model.bo.FileBO;
import com.duwei.model.bo.NodeBO;
import com.duwei.model.bo.ShardsBO;
import com.duwei.model.bo.TransferBO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.handler
 * @Author: duwei
 * @Date: 2022/6/8 15:29
 * @Description: 操作文件的handler
 */
@Slf4j
public class FileService {
    private P2PService p2PService = new P2PService();
    private HttpService httpService = new HttpService();

    /**
     * 对用户编码文件的请求进行处理
     *
     * @param fileName
     */
    public void encodeFile(String fileName, String owner) throws IOException {
        File inputFile = new File(fileName);
        if (!inputFile.exists()) {
            log.error("文件 \"{}\" 不存在，不能进行编码", fileName);
            return;
        }
        //获取文件大小
        long fileSize = inputFile.length();
        R result = httpService.addFile(new FileBO(fileName, fileSize, owner));
        //如果请求错误输入日志直接返回
        if (!result.getCode().equals(200)) {
            log.error(result.getMessage());
            return;
        }

        //获取对于节点和切片数据
        Map<String, Object> data = result.getData();
        ObjectMapper mapper = new ObjectMapper();
        ShardsBO shards = mapper.readValue(mapper.writeValueAsString(data.get("shard")), ShardsBO.class);
        List<NodeBO> nodes = mapper.readValue(mapper.writeValueAsString(data.get("nodes")), new TypeReference<List<NodeBO>>() {
        });
        int totalShards = shards.getDataShards() + shards.getParityShards();
        //要求一个节点只存储一个切片，节点不够就退出，当前不能进行编码
        if (totalShards > nodes.size()) {
            log.error("当前系统节点个数为 {}，分片个数为 {},由于节点个数小于分片大小，不能进行编码", nodes.size(), totalShards);
            return;
        }
        byte[][] encodeShards = Encoder.encode(shards.getDataShards(), shards.getParityShards(), inputFile);
        log.info("文件编码完毕，开始进行同步传输，请稍后(可以改为异步)");

        //将数据切片包装为TransferBO
        TransferBO[] transferBOS = new TransferBO[encodeShards.length];
        for (int i = 0; i < transferBOS.length; i++) {
            TransferBO bo = new TransferBO();
            bo.setType(1);
            bo.setFileName(fileName + "." + i);
            bo.setData(Base64.encodeBase64String(encodeShards[i]));
            transferBOS[i] = bo;
        }

        //进行同步P2P文件传输
        boolean flag = p2PService.transferShards(nodes, transferBOS);
        if (!flag) {
            log.error("文件传输出错，请稍后重试");
            return;
        }
        log.info("文件传输完成");

        //将数据存入区块链

    }
}
