package com.library.multimedia.mvprocess;

import com.android.ktv.KTVVideoOutputStream;

/**
 * Created by wy201 on 2017-08-12.
 * 拼接视频输出流
 */

public class VideoJointOutputStream implements KTVVideoOutputStream {
    private int firstVideoWidth;//大视频宽
    private int firstVideoHeight;//大视频高
    private int secondVideoWidth;//小视频宽
    private int secondVideoHeigth;//小视频高
    private int insideBottomRightX;
    private int insideBottomRighY;
    private String filePath;

    public VideoJointOutputStream(int firstVideoWidth, int firstVideoHeight, int secondVideoWidth,
                                  int secondVideoHeigth, int insideBottomRightX, int insideBottomRighY, String filePath) {
        this.firstVideoWidth = firstVideoWidth;
        this.firstVideoHeight = firstVideoHeight;
        this.secondVideoWidth = secondVideoWidth;
        this.secondVideoHeigth = secondVideoHeigth;
        this.insideBottomRightX = insideBottomRightX;
        this.insideBottomRighY = insideBottomRighY;
        this.filePath = filePath;
    }

    @Override
    public int getFirstVideoWidth() {
        return firstVideoWidth;
    }

    @Override
    public int getFirstVideoHeight() {
        return firstVideoHeight;
    }

    @Override
    public int getSecondVideoWidth() {
        return secondVideoWidth;
    }

    @Override
    public int getSecondVideoHeight() {
        return secondVideoHeigth;
    }

    @Override
    public int getInsideBottomRightX() {
        return insideBottomRightX;
    }

    @Override
    public int getInsideBottomRightY() {
        return insideBottomRighY;
    }

    @Override
    public void processData(byte[] bytes, int i, int i1) {

    }

    @Override
    public String getFilePath() {
        return filePath;
    }
}
