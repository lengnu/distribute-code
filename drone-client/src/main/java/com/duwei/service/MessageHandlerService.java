package com.duwei.service;

import com.duwei.model.bo.TransferBO;
import com.duwei.utils.JsonParseUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.service
 * @Author: duwei
 * @Date: 2022/6/9 9:13
 * @Description: 监听客户端消息进行处理
 */
@Slf4j
public class MessageHandlerService {

    private final RedisService redisService = new RedisService();

    public void dispatcherHandler(SelectionKey selectionKey, Selector selector) {
        try {
            //1.客户端连接事件
            if (selectionKey.isAcceptable()) {
                connectionHandler(selectionKey, selector);
            }
            //2.读取到客户端发来的消息
            if (selectionKey.isReadable()) {
                readableHandler(selectionKey);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            //出现异常代表客户端掉线
            log.info("客户端下线");
        }
    }

    /**
     * 监听客户端进行了连接
     *
     * @param selectionKey
     * @throws IOException
     */
    private void connectionHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        log.info("{} 进行了连接", channel.getRemoteAddress().toString());
    }

    /**
     * 监听到了客户端发送数据
     *
     * @param selectionKey
     */
    private void readableHandler(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        log.info("监听到节点 {} 发来数据",channel.getRemoteAddress().toString());
        channel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        int readSize = channel.read(buffer);
        //切换为读模式
        buffer.flip();
        byte[] data = Arrays.copyOfRange(buffer.array(), 0, readSize);
        TransferBO bo = JsonParseUtil.parse(data, TransferBO.class);
        //1.客户端要存数据
        if (bo.getType().equals(1)) {
            this.storeShard(bo);
        }
        //2.客户端要取数据
        if (bo.getType().equals(2)) {
            log.info("监听到客户端要数据切片");
            readShard(channel, bo);
        }
    }

    /**
     * 将数据存入redis
     *
     * @param bo
     */
    private void storeShard(TransferBO bo) {
        redisService.setString(bo.getFileName(), bo.getData());
    }

    /**
     * 将输入从redis取出并且传回客户端
     *
     * @param socketChannel
     * @param bo
     */
    private void readShard(SocketChannel socketChannel, TransferBO bo) throws IOException {
        String fileName = bo.getFileName();
        String data = redisService.getString(fileName);
        TransferBO response = new TransferBO();
        response.setType(3);
        if (data != null) {
            response.setType(2);
            response.setData(data);
            response.setFileName(fileName);
        }
        socketChannel.write(ByteBuffer.wrap(JsonParseUtil.byteify(response)));
    }

}
