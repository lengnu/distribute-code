package com.duwei.erasure;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @BelongsProject: Distributed-Coding
 * @BelongsPackage: com.duwei.erasure
 * @Author: duwei
 * @Date: 2022/6/8 11:00
 * @Description: RS编码
 */
@Slf4j
public class Encoder {
    private static final int BYTES_IN_INT = 4;

    /**
     * 根据编码参数对指定文件进行编码
     * @param dataShards
     * @param parityShards
     * @param inputFile
     * @return
     * @throws IOException
     */
    public static byte[][] encode(int dataShards,int parityShards,File inputFile) throws IOException {
        final int fileSize = (int) inputFile.length();
        final int storedSize = fileSize + BYTES_IN_INT;
        final int shardSize = (storedSize + dataShards - 1) / dataShards;
        
        //创建一个缓存区保存编码的文件
        final int bufferSize = shardSize * dataShards;
        final byte [] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt(fileSize);
        InputStream in = new FileInputStream(inputFile);
        int bytesRead = in.read(allBytes, BYTES_IN_INT, fileSize);
        if (bytesRead != fileSize) {
            log.error("没有足够字节可读......");
            return null;
        }
        in.close();
        
        final int totalShards = dataShards + parityShards;
        //存储编码过后的文件
        byte [][] shards = new byte [totalShards] [shardSize];
        for (int i = 0; i < dataShards; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }
        //进行RS编码
        ReedSolomon reedSolomon = ReedSolomon.create(dataShards, parityShards);
        reedSolomon.encodeParity(shards, 0, shardSize);

        return shards;
    }




}
