package com.clicktech.snsktv.widget.progressbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.ConvertUtils;

/**
 * Created by wy201 on 2018-02-26.
 */

public class RoundProgress extends ProgressBar {
    //第一层颜色
    private final int COLOR_FIRSTPROGRESS = Color.parseColor("#313131");
    //第二层颜色
    private final int COLOR_SECONDPROGRESS = Color.parseColor("#E50150");
    //画笔
    private Paint mPaint;
    //环形宽度
    private int mRingWidth;
    //播放、暂停大小
    private int mSizeButton;
    //环形半径
    private int mRadius;
    //中心xy坐标
    private int mXOfCenter;
    private int mYOfCenter;
    //播放
    private Bitmap mBitmapOfPlay;
    //暂停
    private Bitmap mBitmapOfPause;
    //播放状态
    private boolean mStatus;

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRingWidth = ConvertUtils.dp2px(context, 3);
        mSizeButton = ConvertUtils.dp2px(context, 13);
        mRadius = ConvertUtils.dp2px(context, 16);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        mBitmapOfPause = BitmapFactory.decodeResource(context.getResources(), R.mipmap.mini_pause);
        mBitmapOfPlay = BitmapFactory.decodeResource(context.getResources(), R.mipmap.mini_play);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widhtSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desireWidth = mRadius * 2 + getPaddingLeft() + getPaddingRight();
        int desireHeight = mRadius * 2 + getPaddingTop() + getPaddingBottom();

        int finalWidth;
        int finalHeigh;

        if (widthMode == MeasureSpec.AT_MOST) {
            finalWidth = desireWidth;
        } else {
            finalWidth = Math.max(desireWidth, widhtSize);
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            finalHeigh = desireHeight;
        } else {
            finalHeigh = Math.max(desireHeight, heightSize);
        }

        setMeasuredDimension(finalWidth, finalHeigh);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        getCoordinateOfCenter();
        drawFirstProgress(canvas);
        drawSecondProgress(canvas);
        drawPlayPauseButton(canvas);
    }

    //获取中心坐标
    private void getCoordinateOfCenter() {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        mRadius = Math.min(width, height) / 2;
        mXOfCenter = getWidth() / 2;
        mYOfCenter = getHeight() / 2;
    }

    //绘制第一层进度
    private void drawFirstProgress(Canvas canvas) {
        mPaint.setColor(COLOR_FIRSTPROGRESS);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mXOfCenter, mYOfCenter, mRadius, mPaint);
    }

    //绘制第二层进度
    private void drawSecondProgress(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(mXOfCenter - mRadius, mYOfCenter - mRadius,
                mXOfCenter + mRadius, mYOfCenter + mRadius);
        float ratio = getProgress() * 1.0f / getMax();
        int sweepAngle = (int) (ratio * 360);
        mPaint.setColor(COLOR_SECONDPROGRESS);
        mPaint.setAntiAlias(true);
        canvas.drawArc(rectF, -90, sweepAngle, false, mPaint);
    }

    //绘制播放、暂停按钮
    private void drawPlayPauseButton(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.set(mXOfCenter - mSizeButton / 2, mYOfCenter - mSizeButton / 2,
                mXOfCenter + mSizeButton / 2, mYOfCenter + mSizeButton / 2);

        if (mStatus) {
            canvas.drawBitmap(mBitmapOfPlay, null, rectF, null);
        } else {
            canvas.drawBitmap(mBitmapOfPause, null, rectF, null);
        }

    }

    //更新播放图标
    public void updatePlayPauseButton(boolean status) {
        this.mStatus = status;
        invalidate();
    }


}
