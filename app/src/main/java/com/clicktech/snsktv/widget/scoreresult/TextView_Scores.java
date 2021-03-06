package com.clicktech.snsktv.widget.scoreresult;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.common.KtvApplication;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by w201 on 2017-09-19.
 * 分数
 */

public class TextView_Scores extends View {

    private Paint mScorePaint;
    private Paint mStrokePaint;
    private Paint mUnitPaint;
    private String unitStr;
    private int unitSize;
    private int scoreSize;

    private float score = 000.000f;
    private long duration = 3000;
    private ArrayList<String> mTempData;
    private Random mRandom;

    private float mViewHeight;
    private float mViewWidth;
    private float mSpaceOfText;//文字之间间距
    private ValueAnimator valueAnimator;

    public TextView_Scores(Context context) {
        super(context);
        init(context);
    }

    public TextView_Scores(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private static void playSound() {
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(KtvApplication.ktvApplication, R.raw.sound_tum, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(1,  //声音id
                        1, //左声道
                        1, //右声道
                        1, //优先级
                        0, // 0表示不循环，-1表示循环播放
                        1);//播放比率，0.5~2，一般为1
            }
        });

    }

    private void init(Context context) {
        scoreSize = ConvertUtils.sp2px(context, 44);
        unitSize = ConvertUtils.sp2px(context, 24);

        mScorePaint = new Paint();
        mScorePaint.setAntiAlias(true);
        mScorePaint.setColor(Color.WHITE);
        mScorePaint.setFakeBoldText(true);
        mScorePaint.setTextSize(scoreSize);

        mUnitPaint = new Paint();
        mUnitPaint.setAntiAlias(true);
        mUnitPaint.setColor(Color.WHITE);
        mUnitPaint.setFakeBoldText(true);
        mUnitPaint.setTextSize(unitSize);

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mStrokePaint.setFakeBoldText(true);
        mStrokePaint.setStrokeWidth(ConvertUtils.dp2px(context, 4));
        mStrokePaint.setColor(Color.parseColor("#e400ff"));

        unitStr = context.getString(R.string.score_synthetical_unit);

        Paint.FontMetrics fontMetrics = mScorePaint.getFontMetrics();
        mViewHeight = fontMetrics.bottom - fontMetrics.top;
        mViewWidth = getResources().getDisplayMetrics().widthPixels;
        mSpaceOfText = ConvertUtils.dp2pxF(getContext(), 5);

        fillingData();

    }

    //设置最终的分数
    public void setFinalScore(float score) {
        this.score = score;
        fillingData();
    }

    private void fillingData() {
        clearAnimation();
        String scoreStr = String.valueOf(score);

        mTempData = new ArrayList<>();
        if (score == 0) {
            mTempData.add("0");
            mTempData.add(".");
            mTempData.add("0");
            invalidate();
            return;
        }

        mRandom = new Random();
        playSound();
        for (int i = 0; i < scoreStr.length(); i++) {
            char bit = scoreStr.charAt(i);
            if (".".equals(String.valueOf(bit))) {
                mTempData.add(".");
            } else {
                mTempData.add("0");
                startRolling(i, String.valueOf(bit));
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension((int) mViewWidth, (int) mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawActualTotalScore(canvas);
    }

    private void drawActualTotalScore(Canvas canvas) {
        if (EmptyUtils.isEmpty(mTempData)) {
            return;
        }

        //绘制分数
        mStrokePaint.setTextSize(scoreSize);
        String scoreStr = String.valueOf(score);
        float startX = (getMeasuredWidth() - mScorePaint.measureText(scoreStr)
                - mUnitPaint.measureText(unitStr) - mSpaceOfText * scoreStr.length()) / 2;
        Paint.FontMetrics fontMetrics = mScorePaint.getFontMetrics();
        float baselineY = -fontMetrics.ascent;

        for (int i = 0; i < mTempData.size(); i++) {
            String curScoreStr = mTempData.get(i);
            canvas.drawText(curScoreStr, startX, baselineY, mStrokePaint);
            canvas.drawText(curScoreStr, startX, baselineY, mScorePaint);
            startX += mScorePaint.measureText(curScoreStr) + mSpaceOfText;
        }

        //绘制单位
        mStrokePaint.setTextSize(unitSize);
        canvas.drawText(String.valueOf(unitStr), startX, baselineY, mStrokePaint);
        canvas.drawText(String.valueOf(unitStr), startX, baselineY, mUnitPaint);
    }

    //数字开始滚动
    private void startRolling(final int position, final String endvalue) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.1f, Float.parseFloat(endvalue));
        valueAnimator.setDuration(duration);

        valueAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        if (Integer.parseInt(endvalue) == value) {
                            mTempData.set(position, endvalue);
                        } else {
                            mTempData.set(position, String.valueOf(mRandom.nextInt(10)));
                        }
                        invalidate();
                    }

                });

        valueAnimator.start();
    }

}
