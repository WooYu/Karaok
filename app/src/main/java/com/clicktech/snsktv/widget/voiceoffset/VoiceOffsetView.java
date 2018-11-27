package com.clicktech.snsktv.widget.voiceoffset;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.clicktech.snsktv.arms.utils.ConvertUtils;

/**
 * Created by wy201 on 2018-01-20.
 */

public class VoiceOffsetView extends View {
    private final int OFFSETRANGE = 40;
    private Paint mPaint;
    private float mSizeOfTriangle;//三角指示器大小
    private float mWidthOfLine;//线宽
    private float mHorizontalSpaceing;
    private int mViewHeight;
    private int offset = OFFSETRANGE / 2;//倒三角的位置


    public VoiceOffsetView(Context context) {
        this(context, null);
    }

    public VoiceOffsetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceOffsetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        mHorizontalSpaceing = (viewWidth - (OFFSETRANGE + 1) * mWidthOfLine) / (OFFSETRANGE + 2);
    }

    //初始化画笔
    private void initPaint(Context context) {
        mSizeOfTriangle = ConvertUtils.dp2px(context, 4);
        mWidthOfLine = ConvertUtils.dp2px(context, 2);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mWidthOfLine);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawATick(canvas);
        drawATriangle(canvas);
    }

    //绘制刻度
    private void drawATick(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //刻度中心点Y坐标
        float startYOfCenter = (mViewHeight - mSizeOfTriangle) / 2;
        //短线的高度
        float startYOfStubLine = (mViewHeight - mSizeOfTriangle) / 3;
        //长线的高度
        float startYOfLongLine = (mViewHeight - mSizeOfTriangle) / 2;

        for (int i = 1; i <= OFFSETRANGE + 1; i++) {
            if (i % 5 == 1) {
                if (i == (OFFSETRANGE / 2 + 1)) {
                    //绘制中间刻度线
                    canvas.drawLine((mWidthOfLine + mHorizontalSpaceing) * i - mWidthOfLine / 2, (startYOfCenter - startYOfLongLine / 2),
                            (mWidthOfLine + mHorizontalSpaceing) * i - mWidthOfLine / 2, (startYOfCenter + startYOfLongLine / 2),
                            mPaint);
                } else {
                    //绘制长线
                    canvas.drawLine((mWidthOfLine + mHorizontalSpaceing) * i - mWidthOfLine / 2, (startYOfCenter - startYOfStubLine / 2),
                            (mWidthOfLine + mHorizontalSpaceing) * i - mWidthOfLine / 2, (startYOfCenter + startYOfStubLine / 2),
                            mPaint);
                }
            } else {
                //绘制短线
                canvas.drawPoint((mWidthOfLine + mHorizontalSpaceing) * i - mWidthOfLine / 2, startYOfCenter, mPaint);
            }

        }
    }

    //绘制三角形
    private void drawATriangle(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#26D4D6"));
        mPaint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        //三角形最低点
        float xOfNadir = (offset + 1) * (mHorizontalSpaceing + mWidthOfLine) - mWidthOfLine / 2;
        path.moveTo(xOfNadir, mSizeOfTriangle);
        path.lineTo(xOfNadir - mSizeOfTriangle / 2, 0);
        path.lineTo(xOfNadir + mSizeOfTriangle / 2, 0);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    public void setVoiceLeftMove() {
        --offset;
        if (offset < 0) {
            offset = OFFSETRANGE;
        }
        invalidate();
    }

    public void setVoiceRightMove() {
        ++offset;
        if (offset > OFFSETRANGE) {
            offset = 0;
        }
        invalidate();
    }

    public int getVoiceMove() {
        return offset - OFFSETRANGE / 2;
    }
}
