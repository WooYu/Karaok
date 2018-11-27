package com.library.multimedia.mvprocess;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by wy201 on 2017-08-12.
 * 多线程写文件，文件写入位置怎么决定
 * 由输入文件的大小决定
 */

public class CopyFileThread extends Thread {

    private String srcPath;
    private String destPath;
    private long start, end;
    private int pos;
    private ICopyFileCallBack callBack;

    public CopyFileThread(int pos, String scrPath, String destPath, long start, long copysection, ICopyFileCallBack callBack) {
        this.srcPath = scrPath;
        this.destPath = destPath;
        this.start = start;
        this.end = copysection;
        this.pos = pos;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            long beginTime = System.currentTimeMillis();
            //创建只读的随机访问文件
            RandomAccessFile in = new RandomAccessFile(srcPath, "r");
            //创建可读可写的随机访问文件
            RandomAccessFile out = new RandomAccessFile(destPath, "rw");
            //将输入跳转到指定位置
            in.seek(0);
            //从指定位置开始写
            out.seek(start);
            //文件输入通道
            FileChannel inChannel = in.getChannel();
            //文件输出通道
            FileChannel outChannel = out.getChannel();
            //锁住需要操作的区域，false代表锁住
            FileLock lock = outChannel.lock(start, end, true);
            //将字节从此通道的文件传输到指定的可写入字节的输出通道
            inChannel.transferTo(0, end, outChannel);
            //释放锁
            lock.release();
            //从里到外关闭文件
            out.close();
            in.close();
            //计算耗时
            long endTime = System.currentTimeMillis();
            System.out.println("复制第" + pos + "个耗时：" + (endTime - beginTime));
            callBack.onSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
