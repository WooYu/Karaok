package com.library.multimedia.mvprocess;

import com.android.ktv.KTVConvert;
import com.android.ktv.KTVConvertCallback;
import com.android.ktv.KTVOutputStream;
import com.android.ktv.KTVVideoInputStream;
import com.android.ktv.KTVVideoOutputStream;

/**
 * Created by wy201 on 2017-07-27.
 * MV处理
 * 提供的功能
 * 从音视频中获取pcm流
 * 将两个视频拼接
 * 视频格式转换：ts->mp4
 * 音视频合成：mp3和mp4
 */

public class MVProcess {


    private static final int DEFAULT_SERVER_PORT_MV = 8080;
    private static MVProcess instance = null;

    public static MVProcess getInstance() {
        if (null == instance) {
            synchronized (MVProcess.class) {
                if (null == instance) {
                    instance = new MVProcess();
                    KTVConvert.init(DEFAULT_SERVER_PORT_MV);
                }
            }
        }

        return instance;
    }

    /**
     * 解码音频或者mv获取其中的音频pcm流
     *
     * @param input
     * @param output
     * @param callback
     */
    public void decodeMusic(String input, KTVOutputStream output,
                            KTVConvertCallback callback) {
        KTVConvert.decodeMusic(input, output, callback);
    }

//
//    /**
//     * 两个视频左右合并
//     *
//     * @param leftPath     预览视频KTVInputstream
//     * @param yuvWidth     yuv数据宽
//     * @param yuvHeight    yuv数据高
//     * @param yuvFrameRate 帧率
//     * @param rightPath    播放的视频路径
//     * @param outputPath   输出视频路径
//     * @param leftWidth    左视频宽度
//     * @param leftHeight   左视频高度
//     * @param rigthWidth   右视频宽度
//     * @param rightHeight  右视频高度，必须和左视频高度相同
//     * @param callback     处理结果回调
//     */

    /**
     * 两个视频左右合并
     *
     * @param ktvVideoInputStream  输入视频流
     * @param mp4path              输出视频路径
     * @param ktvVideoOutputStream 输出视频流
     * @param callback
     */
    public void combineVideoLeftRight(KTVVideoInputStream ktvVideoInputStream,
                                      int firstVideoBeginTime, int firstVideoTimeLenght,
                                      String mp4incisePath, String mp4path,
                                      KTVVideoOutputStream ktvVideoOutputStream, KTVConvertCallback callback) {
        KTVConvert.CompositeVideoLeftRight(ktvVideoInputStream, firstVideoBeginTime,
                firstVideoTimeLenght, mp4incisePath, mp4path, ktvVideoOutputStream, callback);
    }

    /**
     * 两个视频前后合并
     *
     * @param firstVideoBeginTime  yuv流起始时间
     * @param firstVideoTimeLenght yuv流持续时间
     * @param mp4incisePath        临时的mp4路径
     * @param mp4path              右下角的视频路径
     * @param ktvVideoInputStream  yuv的输入流
     * @param ktvVideoOutputStream ts的输出流
     * @param callback
     */
    public void combineVideoFrontBack(int firstVideoBeginTime, int firstVideoTimeLenght,
                                      String mp4incisePath, String mp4path,
                                      KTVVideoInputStream ktvVideoInputStream,
                                      KTVVideoOutputStream ktvVideoOutputStream,
                                      KTVConvertCallback callback) {
        KTVConvert.CompositeVideoBottomRight(firstVideoBeginTime, firstVideoTimeLenght, mp4incisePath,
                mp4path,
                ktvVideoInputStream, ktvVideoOutputStream, callback);
    }

    /**
     * 视频格式转换 ts->mp4
     *
     * @param input
     * @param output
     * @param callback
     */
    public void videoFormatConversion(String input, String output, KTVConvertCallback callback) {
        KTVConvert.videoFormatConversion(input, output, callback);
    }

    //音视频合成：mp3和mp4
    public void concatMp3toMp4(String mp3Str, String mp4Str, String output, KTVConvertCallback callback) {
        KTVConvert.concatMP3toMp4(mp3Str, mp4Str, output, callback);
    }

    /**
     * 释放资源
     */
    public void releaseRecource() {
        KTVConvert.unInit();
        instance = null;
    }

    /**
     * 取消mv合成操作。正在合成时取消会走error();
     */
    public void cancleEffect() {
        KTVConvert.Reset();
    }

}
