package com.duwei.view;

import com.duwei.common.Constant;
import com.duwei.service.FileService;
import com.duwei.socket.Server;

import java.io.IOException;

import static com.duwei.utils.Utility.*;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.view
 * @Author: duwei
 * @Date: 2022/6/8 15:06
 * @Description: 主菜单界面
 */
public class MenuView {
    private FileService fileService;
    private Server server;

    public MenuView(Server server) {
        fileService = new FileService();
        this.server = server;
    }

    public void showMenu() throws IOException {
        while (true) {
            System.out.println("----------请输入选择----------");
            System.out.println("\t\t1.文件编码\t\t");
            System.out.println("\t\t2.文件译码\t\t");
            System.out.println("\t\t3.退出系统t\t");
            char select = readChar();
            switch (select) {
                case '1':
                    fileEncodingMenu();
                    break;
                case '2':
                    fileDecodingMenu();
                    break;
                case '3':
                    break;
                default:

            }
        }
    }

    public void fileEncodingMenu() throws IOException {
        System.out.println("----------你已经选择文件编码选项----------");
        System.out.print("请输入要编码的文件名称，不超过100字符:");
        String fileName = readString(100);
        fileName = Constant.FILE_PREFIX + fileName;
        fileService.encodeFile(fileName,server.getIp());
    }


    //TODO 文件解码部分
    public void fileDecodingMenu() throws IOException {
        System.out.println("----------你已经选择文件编码选项----------");
        System.out.print("请输入要解码的文件名称，不超过100字符:");
        String fileName = readString(100);
        fileName = Constant.FILE_PREFIX + fileName;
        fileService.decodeFile(fileName);
    }

}

