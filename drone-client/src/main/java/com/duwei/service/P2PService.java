package com.duwei.service;

import com.duwei.common.Constant;
import com.duwei.erasure.Coder;
import com.duwei.model.bo.FileRecordBO;
import com.duwei.model.bo.NodeBO;
import com.duwei.model.bo.TransferBO;
import com.duwei.utils.JsonParseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.service
 * @Author: duwei
 * @Date: 2022/6/8 17:12
 * @Description: 复制进行文件的读取和传输
 */
@Slf4j
public class P2PService {
    /**
     * 进行切片数据的传输
     *
     * @param nodes
     * @param transferBO
     * @return
     */
    public boolean transferShards(List<NodeBO> nodes, TransferBO[] transferBO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < nodes.size(); i++) {
                SocketChannel socketChannel = SocketChannel.open();
                final String ip = nodes.get(i).getIp();
                final int port = Constant.LISTEN_PORT;
                socketChannel.socket().connect(new InetSocketAddress(ip, port), 5000);
                byte[] transfer = mapper.writeValueAsBytes(transferBO[i]);
                socketChannel.write(ByteBuffer.wrap(transfer));
                log.info("向节点 {} 传输完毕", nodes.get(i).getIp());
            }

        } catch (IOException e) {
            log.error("连接建立失败，文件传输失败，请稍后重试");
            log.error(e.getMessage());
            return false;
        }
        log.info("节点所有切片已经传输完毕");
        return true;
    }

    /**
     * 根据链上的记录信息取寻找切片
     *
     * @param fileRecordBO
     * @return
     */
    public TransferBO[] searchShards(FileRecordBO fileRecordBO) {
        try {
            //1.得到存储的映射信息
            String information = fileRecordBO.getMapping_information();
            Map<String, String> map = JsonParseUtil.parse(information, Map.class);
            int dataShards = fileRecordBO.getData_shards().intValue();
            int parityShards = fileRecordBO.getParity_shards().intValue();
            int totalShards = dataShards + parityShards;
            byte[][] shards = new byte[totalShards][];
            boolean[] shardPresent = new boolean[totalShards];
            log.info("开始寻找切片");
            int index = -1;
            int count = 0;
            int shardsSize = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                index++;
                final String ip = entry.getKey();
                final int port = Constant.LISTEN_PORT;
                final String fileShardsName = entry.getValue();
                SocketChannel socketChannel = SocketChannel.open();
                try {
                    socketChannel.socket().connect(new InetSocketAddress(ip, port), 5000);
                }catch (IOException e){
                    log.error("与节点 {} 连接建立超时，开始寻找下一个节点",ip );
                    continue;
                }
                TransferBO send = new TransferBO();
                send.setType(2);
                send.setFileName(fileShardsName);
                socketChannel.write(ByteBuffer.wrap(JsonParseUtil.byteify(send)));
                log.info("已向 {} 发送请求，正在等待回应",ip);
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                int readSize = socketChannel.read(buffer);
                byte[] data = Arrays.copyOfRange(buffer.array(), 0, readSize);
                //得到返回结果
                TransferBO transferBO = JsonParseUtil.parse(data, TransferBO.class);
                if (transferBO.getType().equals(2)){
                    log.info("拿到 {} 的数据切片",ip);
                    shardPresent[index] = true;
                    //得到当前节点的切片
                    byte[] curShards = Base64.decodeBase64(transferBO.getData());
                    shardsSize = curShards.length;
                    shards[index] = curShards;
                    count++;
                }else {
                    log.info("节点 {} 的数据切片已经丢失，继续向其他节点寻找切片",ip);
                    shardPresent[index] = false;
                }
            }
            log.info("切片已经寻回完毕，开始进行恢复");
            Coder.decode(fileRecordBO.getFile_name(), dataShards,parityShards,shards,shardPresent,shardsSize,count);
            log.info("文件恢复完毕，请及时备份");

        } catch (IOException e) {
            log.error("连接建立失败，文件传输失败，请稍后重试");
            log.error(e.getMessage());
            return null;

        }

        return null;
    }
}
