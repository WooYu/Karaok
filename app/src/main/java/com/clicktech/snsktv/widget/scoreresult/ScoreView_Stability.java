package com.clicktech.snsktv.widget.scoreresult;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy201 on 2017-09-23.
 * 稳定性
 */

public class ScoreView_Stability extends View {
    //颜色
    //边框颜色
    private final int COLOR_BORDER = Color.argb(255, 0, 255, 255);
    //黑色
    private final int COLOR_BLACK = Color.argb(255, 0, 0, 0);
    //绿色
    private final int COLOR_INDIGO = Color.argb(187, 140, 255, 255);
    //长方形数量
    private final int NUMBEROFRECTANGLE = 10;
    //度量
    //长方形间隙
    private int mSpaceOfRectangle;
    //圆角
    private float mSizeOfRoundCircle;
    //边框宽度
    private float mSizeOfBorder;

    //画笔
    //背景画笔
    private Paint mPaintOfBackground;
    //前景画笔
    private Paint mPaintOfForeground;
    private int[] colorArr = new int[]{COLOR_BLACK, COLOR_INDIGO};
    private RectF mBgRectf;//背景
    private RectF mFgRectf;//前景
    //    private RectF mFgRectfA;//前景A
//    private RectF mFgRectfB;//前景B
    private LinearGradient mLinearGradient;
    private int mColourPalette[] = new int[]{COLOR_INDIGO, COLOR_INDIGO, COLOR_BLACK, COLOR_BLACK};
    private float mPosition[] = new float[4];

    //数据
    //余数部分
    private float mRemainderPart;
    //整数部分
    private int mIntegerPart;
    private List<Integer> mScoreList;

    public ScoreView_Stability(Context context) {
        this(context, null);
    }

    public ScoreView_Stability(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView_Stability(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSize();
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (EmptyUtils.isEmpty(mScoreList)) {
            return;
        }

        //长方形宽度
        int mWidthOfRectangle = getMeasuredWidth() / NUMBEROFRECTANGLE - mSpaceOfRectangle;
        //长方形高度
        int mHeightOfRectangle = getMeasuredHeight();

        float heightOfCurRect = mHeightOfRectangle * 1f / 2;
        float incrementOfHeight = (mHeightOfRectangle - heightOfCurRect) / NUMBEROFRECTANGLE;
        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {

            float left = mSizeOfBorder / 2 + i * (mWidthOfRectangle + mSpaceOfRectangle);
            float top = (getMeasuredHeight() - heightOfCurRect + mSizeOfBorder) / 2;
            float right = left + mWidthOfRectangle - mSizeOfBorder;
            float bottom = top + heightOfCurRect - mSizeOfBorder;

            mBgRectf.left = left;
            mBgRectf.top = top;
            mBgRectf.right = right;
            mBgRectf.bottom = bottom;

            mFgRectf.left = left + mSizeOfBorder / 2;
            mFgRectf.top = top + mSizeOfBorder / 2;
            mFgRectf.right = right - mSizeOfBorder / 2;
            mFgRectf.bottom = bottom - mSizeOfBorder / 2;

            //绘制背景
            canvas.drawRoundRect(mBgRectf, mSizeOfRoundCircle, mSizeOfRoundCircle, mPaintOfBackground);

            //绘制前景
            if (i == mIntegerPart && mRemainderPart > 0) {
                //绘制余数的部分,分两部分绘制


                // 第1个点
                mPosition[0] = 0;
                // 第2个点
                mPosition[1] = mRemainderPart * 0.1f;
                // 第3个点
                mPosition[2] = mRemainderPart * 0.1f;
                // 第4个点
                mPosition[3] = 1;

                mLinearGradient = new LinearGradient(
                        mFgRectf.left, mFgRectf.top,
                        mFgRectf.right, mFgRectf.top,
                        mColourPalette,
                        mPosition,
                        Shader.TileMode.MIRROR);

                mPaintOfForeground.setShader(mLinearGradient);
                canvas.drawRoundRect(mFgRectf, mSizeOfRoundCircle / 2, mSizeOfRoundCircle / 2, mPaintOfForeground);

//                mFgRectfA.left = mFgRectf.left;
//                mFgRectfA.top = mFgRectf.top;
//                mFgRectfA.right = mFgRectf.left + (mWidthOfRectangle - mSizeOfBorder * 2) * mRemainderPart / 10;
//                mFgRectfA.bottom = mFgRectf.bottom;
//
//                mPaintOfForeground.setColor(colorArr[1]);
//                canvas.drawRect(mFgRectfA, mPaintOfForeground);
//
//                mFgRectfB.left = mFgRectfA.right;
//                mFgRectfB.top = mFgRectf.top;
//                mFgRectfB.right = mFgRectf.right;
//                mFgRectfB.bottom = mFgRectf.bottom;
//
//                mPaintOfForeground.setColor(colorArr[0]);
//                canvas.drawRect(mFgRectfB, mPaintOfForeground);
            } else {
                mPaintOfForeground.setShader(null);
                mPaintOfForeground.setColor(colorArr[mScoreList.get(i)]);
                canvas.drawRoundRect(mFgRectf, mSizeOfRoundCircle / 2, mSizeOfRoundCircle / 2, mPaintOfForeground);
            }

            //更改变量
            heightOfCurRect += incrementOfHeight;
        }
    }

    private void initPaint() {
        mPaintOfBackground = new Paint();
        mPaintOfBackground.setAntiAlias(true);
        mPaintOfBackground.setStrokeWidth(mSizeOfBorder);
        mPaintOfBackground.setStyle(Paint.Style.STROKE);
        mPaintOfBackground.setColor(COLOR_BORDER);

        mPaintOfForeground = new Paint();
        mPaintOfForeground.setStyle(Paint.Style.FILL);
        mPaintOfForeground.setAntiAlias(true);

        mBgRectf = new RectF();
        mFgRectf = new RectF();
//        mFgRectfA = new RectF();
//        mFgRectfB = new RectF();

    }

    private void initSize() {
        mSizeOfBorder = ConvertUtils.dp2px(getContext(), 1);
        mSpaceOfRectangle = ConvertUtils.dp2px(getContext(), 1);
        mSizeOfRoundCircle = ConvertUtils.dp2px(getContext(), 2);
    }

    public void fillingData(float stability) {
        mRemainderPart = stability % 10;
        mIntegerPart = (int) (stability / 10);
        mScoreList = new ArrayList<>();
        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {
            mScoreList.add(0);
        }

        for (int i = 0; i < mIntegerPart; i++) {
            mScoreList.set(i, 1);
        }
    }

}