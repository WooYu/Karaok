package com.library.multimedia.mvprocess;

import com.android.ktv.KTVVideoInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2017-07-03.
 * 视频拼接输入流
 */

public class VideoJointInputStream implements KTVVideoInputStream {
    public static final String TAG = "VideoJointInputStream";
    private final File file;
    private FileInputStream fs;
    private String intputPath;
    private int yuvlength;
    private int yuvWidth;
    private int yuvHeight;
    private double sampleRate;

    public VideoJointInputStream(int yuvWidth, int yuvHeight, double sampleRate, int yuvlength,
                                 String inputpath) {
        this.yuvWidth = yuvWidth;
        this.yuvHeight = yuvHeight;
        this.sampleRate = sampleRate;
        this.yuvlength = yuvlength;
        this.intputPath = inputpath;
        file = new File(inputpath);
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void begin() {
    }

    @Override
    public long length() {
        return null != file ? file.length() : Long.MAX_VALUE;
    }

    @Override
    public int channels() {
        return 0;
    }

    @Override
    public int delay() {
        return 0;
    }

    @Override
    public void end() {

    }

    @Override
    public String getFileFormat() {
        return "yuv";
    }

    @Override
    public String getFileName() {
        return intputPath;
    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public int available() {
        return yuvlength;
    }

    @Override
    public int readDataOfLength(byte[] arg0, int arg1) {
        int length = 0;
        try {
            length = fs.read(arg0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return length;
    }

    @Override
    public int sampleRate() {
        return 0;
    }

    @Override
    public double volume() {
        return 0;
    }

    public void release() {
        if (null != fs) {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fs = null;
            }
        }

    }

    @Override
    public int getYuvWidth() {
        return yuvWidth;
    }

    @Override
    public int getYuvHeight() {
        return yuvHeight;
    }

    @Override
    public double getYuvFrameRate() {
        return sampleRate;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (null != fs)
            fs.close();
    }
}
