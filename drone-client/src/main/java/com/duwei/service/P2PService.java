package com.duwei.service;

import com.duwei.common.Constant;
import com.duwei.model.bo.NodeBO;
import com.duwei.model.bo.TransferBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.service
 * @Author: duwei
 * @Date: 2022/6/8 17:12
 * @Description: 复制进行文件的读取和传输
 */
@Slf4j
public class P2PService {
    public boolean transferShards(List<NodeBO> nodes, TransferBO[] transferBO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < nodes.size(); i++) {
                SocketChannel socketChannel = SocketChannel.open();
                final String ip = nodes.get(i).getIp();
                final int port = Constant.LISTEN_PORT;
                socketChannel.socket().connect(new InetSocketAddress(ip, port), 5000);
                byte[] transfer = mapper.writeValueAsBytes(transferBO);
                socketChannel.write(ByteBuffer.wrap(transfer));
                log.info("向节点 {} 传输完毕",nodes.get(i).getIp());
            }

        } catch (IOException e) {
            log.error("连接建立失败，文件传输失败，请稍后重试");
            return false;
        }
        log.info("节点所有切片已经传输完毕");
        return true;
    }
}
