package com.clicktech.snsktv.widget.textureview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.clicktech.snsktv.common.KtvApplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by wy201 on 2018-02-27.
 */

public class VideoTextureView extends TextureView implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener {

    public static final int CENTER_CROP_MODE = 1;//裁剪全屏模式
    public static final int CENTER_MODE = 2;//居中模式
    private static OnVideoPlayingListener mOnVideoPlayingListener;
    private static VideoState mPlayState;//播放状态
    private static MediaPlayer mMediaPlayer;
    public int mVideoMode = 0;
    private boolean bSameLink = false;//是否为相同链接
    private String mPlayUrl = "";//视频地址
    private Surface mSurface;
    private int mVideoWidth;//视频宽度
    private int mVideoHeigh;//视频高度
    private Subscription mPlayProgressSubscription;//播放进度定时器

    public VideoTextureView(Context context) {
        super(context);
        init();
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mPlayState = VideoState.playing;
        if (null != mOnVideoPlayingListener) {
            mOnVideoPlayingListener.onPrepared();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Timber.d("buffering = " + percent);
        if (null != mOnVideoPlayingListener) {
            mOnVideoPlayingListener.onPlayBuffering(percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.start();
        mPlayState = VideoState.initialize;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Timber.e("what = %d , extra = %d", what, extra);
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoWidth = mMediaPlayer.getVideoWidth();
        mVideoHeigh = mMediaPlayer.getVideoHeight();
        updateTextureViewSize(mVideoMode);
        if (null != mOnVideoPlayingListener) {
            mOnVideoPlayingListener.onVideoSizeChanged(mVideoWidth, mVideoHeigh);
        }

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    private void init() {
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Timber.d("onSurfaceTextureAvailable()");

        if (null == surface) {
            Timber.e("surface is null !");
            return;
        }

        if (null == mMediaPlayer) {
            mMediaPlayer = KtvApplication.ktvApplication.getAppComponent().mediaplayer();
        }
        mSurface = new Surface(surface);
        mMediaPlayer.setSurface(mSurface);

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mPlayState = VideoState.playing;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Timber.d("onSurfaceTextureSizeChanged()");
        updateTextureViewSize(mVideoMode);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Timber.d("onSurfaceTextureDestroyed()");
        mMediaPlayer.pause();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        if (null != mOnVideoPlayingListener) {
            mOnVideoPlayingListener.onTextureDestory();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Timber.d("onSurfaceTextureUpdated()");
    }

    //设置播放器地址
    public void setUrl(String url) {
        bSameLink = mPlayUrl.equals(url);
        this.mPlayUrl = url;
    }

    public void setmMediaPlayer(MediaPlayer mediaPlayer) {
        this.mMediaPlayer = mediaPlayer;
        if (null != mMediaPlayer) {
            mMediaPlayer.setSurface(new Surface(this.getSurfaceTexture()));
        }
    }

    public void setVideoMode(int mode) {
        mVideoMode = mode;
    }

    public void setOnVideoPlayingListener(OnVideoPlayingListener listener) {
        this.mOnVideoPlayingListener = listener;
    }

    //开始播放
    private void startPlay() {
        if (null == mMediaPlayer) {
            Timber.e("mMediaPlayer is null !");
            return;
        }

        if (bSameLink) {
            mMediaPlayer.start();
            mPlayState = VideoState.playing;
        } else {
            mMediaPlayer.reset();
            try {
                if (mPlayUrl.contains("http")) {
                    mMediaPlayer.setDataSource(mPlayUrl);
                } else {
                    FileInputStream fis = null;
                    fis = new FileInputStream(new File(mPlayUrl));
                    mMediaPlayer.setDataSource(fis.getFD());
                }
                mMediaPlayer.prepareAsync();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        openAuditionProgressListener();

    }

    //暂停播放
    public void pausePlay() {
        if (mMediaPlayer == null) return;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayState = VideoState.pause;
            if (mOnVideoPlayingListener != null) mOnVideoPlayingListener.onPause();
            closeAuditionProgressListener();
        } else {
            mMediaPlayer.start();
            mPlayState = VideoState.playing;
            if (mOnVideoPlayingListener != null) mOnVideoPlayingListener.onRestart();
            openAuditionProgressListener();
        }
    }

    //停止播放
    public void stopPlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    private void updateTextureViewSize(int mode) {
        if (mode == CENTER_MODE) {
            updateTextureViewSizeCenter();
        } else if (mode == CENTER_CROP_MODE) {
            updateTextureViewSizeCenterCrop();
        }
    }

    //重新计算video的显示位置，裁剪后全屏显示
    private void updateTextureViewSizeCenterCrop() {

        float sx = (float) getWidth() / (float) mVideoWidth;
        float sy = (float) getHeight() / (float) mVideoHeigh;

        Matrix matrix = new Matrix();
        float maxScale = Math.max(sx, sy);

        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((getWidth() - mVideoWidth) / 2, (getHeight() - mVideoHeigh) / 2);

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / (float) getWidth(), mVideoHeigh / (float) getHeight());

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, getWidth() / 2, getHeight() / 2);//后两个参数坐标是以整个View的坐标系以参考的

        setTransform(matrix);
        postInvalidate();
    }

    //重新计算video的显示位置，让其全部显示并据中
    private void updateTextureViewSizeCenter() {

        float sx = (float) getWidth() / (float) mVideoWidth;
        float sy = (float) getHeight() / (float) mVideoHeigh;

        Matrix matrix = new Matrix();

        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((getWidth() - mVideoWidth) / 2, (getHeight() - mVideoHeigh) / 2);

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / (float) getWidth(), mVideoHeigh / (float) getHeight());

        //第3步,等比例放大或缩小,直到视频区的一边和View一边相等.如果另一边和view的一边不相等，则留下空隙
        if (sx >= sy) {
            matrix.postScale(sy, sy, getWidth() / 2, getHeight() / 2);
        } else {
            matrix.postScale(sx, sx, getWidth() / 2, getHeight() / 2);
        }

        setTransform(matrix);
        postInvalidate();
    }

    private void openAuditionProgressListener() {
        if (null == mPlayProgressSubscription || mPlayProgressSubscription.isUnsubscribed()) {
            mPlayProgressSubscription =
                    Observable.interval(0, 30, TimeUnit.MILLISECONDS)
                            .onBackpressureDrop()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Long>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(Long aLong) {
                                    if (null == mOnVideoPlayingListener ||
                                            null == mMediaPlayer || mPlayState != VideoState.playing)
                                        return;
                                    mOnVideoPlayingListener.onPlayProgressMonitoring(mMediaPlayer.getDuration(),
                                            mMediaPlayer.getCurrentPosition());
                                }

                            });
        }

    }

    private void closeAuditionProgressListener() {
        if (null != mPlayProgressSubscription && !mPlayProgressSubscription.isUnsubscribed())
            mPlayProgressSubscription.unsubscribe();
    }

    //播放状态
    public enum VideoState {
        initialize, playing, pause
    }

    //回调监听
    public interface OnVideoPlayingListener {
        void onError();

        void onPrepared();

        void onPlayBuffering(int percent);

        void onVideoSizeChanged(int vWidth, int vHeight);

        void onPlayProgressMonitoring(int duration, int percent);

        void onPause();

        void onRestart();

        void onPlayFinish();

        void onTextureDestory();
    }

}
