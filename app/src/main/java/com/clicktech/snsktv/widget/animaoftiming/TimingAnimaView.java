package com.clicktech.snsktv.widget.animaoftiming;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.clicktech.snsktv.module_enter.listener.ITimingCallBack;

/**
 * Created by wy201 on 2017-10-11.
 * 计时动画
 */

public class TimingAnimaView extends View {

    private int mBorderWidth;//外边框宽度
    private int mRadiusOfInCircle;//内圆半径
    private int mRadiusOfOutCircle;//外圆半径
    private int mRadiusOfMidCircle;//中间圆半径
    private int mWidthOfFan;//扇形宽度

    private int mCenterX;
    private int mCenterY;

    private Paint mPaint;

    private int sweepAngle;
    private ValueAnimator valueAnimator;

    public TimingAnimaView(Context context) {
        this(context, null);
    }

    public TimingAnimaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimingAnimaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        mCenterX = viewWidth / 2;
        mCenterY = viewHeight / 2;

        mRadiusOfInCircle = viewWidth / 15;
        mBorderWidth = viewWidth / 20;
        mRadiusOfOutCircle = viewWidth / 2 - mBorderWidth / 2;

        mWidthOfFan = viewWidth / 2 - mBorderWidth * 2 - mRadiusOfInCircle;
        mRadiusOfMidCircle = mRadiusOfInCircle + mBorderWidth / 2 + mWidthOfFan / 2;

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制内圆
        drawInnerCircle(canvas);

        //绘制外圆
        drawOutCircle(canvas);

        //绘制中间圆
        drawMidCircle(canvas);
    }

    private void drawOutCircle(Canvas canvas) {
        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mBorderWidth);
        canvas.drawCircle(mCenterX, mCenterY, mRadiusOfOutCircle, mPaint);
        canvas.restore();
    }

    private void drawInnerCircle(Canvas canvas) {
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, mRadiusOfInCircle, mPaint);
        canvas.restore();
    }

    private void drawMidCircle(Canvas canvas) {
        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mWidthOfFan);
        float left = mCenterX - mRadiusOfMidCircle;
        float top = mCenterY - mRadiusOfMidCircle;
        float right = mCenterX + mRadiusOfMidCircle;
        float bottom = mCenterY + mRadiusOfMidCircle;
        RectF rectf = new RectF(left, top, right, bottom);
        canvas.drawArc(rectf, -90, sweepAngle, false, mPaint);
//        canvas.drawCircle(mCenterX,mCenterY,mRadiusOfMidCircle,mPaint);
        canvas.restore();
    }

    public void startTimeAnima(final ITimingCallBack callBack) {
        valueAnimator = ValueAnimator.ofInt(0, 360);
        valueAnimator.setDuration(15000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                callBack.startNextTiming();
            }
        });
        valueAnimator.start();
    }

    public void onPauseAnima() {
        if (null != valueAnimator) {
            valueAnimator.pause();
        }
    }

    public void onResumeAniam() {
        if (null != valueAnimator) {
            valueAnimator.resume();
        }
    }

    public void onDestoryAniam() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }
}
