package com.library.multimedia.mvprocess;

/**
 * Created by wy201 on 2017-08-12.
 * 视频拼接实体类
 */

public class VideoJointBean {

    private double sampleRate;//采样率
    private String yuvInputPath;//yuv数据输入路径
    private String videoOutputPath;//视频合成输出路径
    private int firstVideoBeginTime;//视频开始时间
    private int firstVideoTimeLength;//视频时长

    public int getFirstVideoBeginTime() {
        return firstVideoBeginTime;
    }

    public void setFirstVideoBeginTime(int firstVideoBeginTime) {
        this.firstVideoBeginTime = firstVideoBeginTime;
    }

    public int getFirstVideoTimeLength() {
        return firstVideoTimeLength;
    }

    public void setFirstVideoTimeLength(int firstVideoTimeLength) {
        this.firstVideoTimeLength = firstVideoTimeLength;
    }

    public double getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(double sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getYuvInputPath() {
        return yuvInputPath;
    }

    public void setYuvInputPath(String yuvInputPath) {
        this.yuvInputPath = yuvInputPath;
    }

    public String getVideoOutputPath() {
        return videoOutputPath;
    }

    public void setVideoOutputPath(String videoOutputPath) {
        this.videoOutputPath = videoOutputPath;
    }
}
