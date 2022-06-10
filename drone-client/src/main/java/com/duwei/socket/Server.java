package com.duwei.socket;

import com.duwei.common.Constant;
import com.duwei.common.R;
import com.duwei.model.bo.NodeBO;
import com.duwei.service.MessageHandlerService;
import com.duwei.utils.HttpClientsUtil;
import com.duwei.util.NetworkUtil;
import com.duwei.view.MenuView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;


/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.socket
 * @Author: duwei
 * @Date: 2022/6/8 10:14
 * @Description: 无人机服务器
 * 1.一个线程进行监听
 * 2.一个线程和用户进行交互
 */
@Slf4j
@Data
public class Server {
    /**
     * 处理客户端消息
     */
    private MessageHandlerService messageHandlerService;
    /**
     * 监听信道
     */
    private ServerSocketChannel serverSocketChannel;
    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 服务器IP
     */
    private String ip;
    /**
     * 当前服务器域名
     */
    private String host;
    /**
     * 监听端口
     */
    private static final int PORT = Constant.LISTEN_PORT;

    public Server() throws IOException {
        //初始化channel设置非阻塞模式
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        //初始化选择器
        selector = Selector.open();

        //初始化IP和域名
        //ip = NetworkUtil.getHostIp();
         ip = "127.0.0.1";
        host = Inet4Address.getLocalHost().getHostName();

        //绑定select，监听Accept时间
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.socket().bind(new InetSocketAddress(ip,Constant.LISTEN_PORT));
        log.info("{} 在 {} 端口进行监听",ip,Constant.LISTEN_PORT);
    }

    /**
     * 初始化工作，将当前客户端注册到后台服务器
     *
     * @return
     */
    public void init() {
        messageHandlerService = new MessageHandlerService();
        NodeBO node = new NodeBO();
        node.setIp(this.ip);
        node.setHost(this.host);
        node.setRole("1");
        R result = HttpClientsUtil.post(Constant.ADD_NODE, node);
        if (result.getCode().equals(Constant.CODE_OK)) {
            log.info("当前客户端 {}( {} ) 已经成功注册到服务器", this.ip, this.host);
        } else {
            log.error("当前客户端启动失败，失败原因：{}", result.getMessage());
            System.exit(1);
        }
    }

    /**
     * 监听客户端请求并处理
     */
    public void listen() {
        try {
            //等待有时间发生，阻塞 TODO 非阻塞有待考虑
            selector.select();
            log.info("当前服务器监听到了事件");
            //获取当时发送的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                messageHandlerService.dispatcherHandler(key,this.selector);
                iterator.remove();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.init();
        new Thread(() -> {
            try {
                new MenuView(server).showMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "前台线程").start();

        new Thread(() -> {
            while (true){
                server.listen();
            }
        },"监听线程").start();
    }


}
