package com.clicktech.snsktv.widget.scoreresult;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wy201 on 2017-09-23.
 * 音调的打分
 */

public class ScoreView_Pitch extends View {
    //颜色
    //边框颜色
    private final int COLOR_BORDER = Color.argb(200, 0, 255, 255);
    //黑色
    private final int COLOR_BLACK = Color.argb(200, 0, 0, 0);
    //黄色
    private final int COLOR_YELLOW = Color.argb(200, 255, 255, 129);
    //粉色
    private final int COLOR_RED = Color.argb(200, 255, 128, 140);
    //绿色
    private final int COLOR_INDIGO = Color.argb(200, 140, 255, 255);
    //长方形数量
    private final int NUMBEROFRECTANGLE = 24;
    //度量
    //长方形间隙
    private float mSpaceOfRectangle;
    //圆角
    private float mSizeOfRoundCircle;
    //边框宽度
    private float mSizeOfBorder;

    //画笔
    //背景画笔
    private Paint mPaintOfBackground;
    //前景画笔
    private Paint mPaintOfForeground;
    private RectF mBgRectf;//背景
    private RectF mFgRectf;//前景


    //数据
    private List<Integer> mScoreList;
    private List<Integer> mTempList;
    private int[] colorArr = new int[]{COLOR_BLACK, COLOR_YELLOW, COLOR_RED, COLOR_INDIGO};


    public ScoreView_Pitch(Context context) {
        this(context, null);
    }

    public ScoreView_Pitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView_Pitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initSize();
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (EmptyUtils.isEmpty(mTempList)) {
            return;
        }

        float mWidthOfRectangle = getMeasuredWidth() * 1.0f / NUMBEROFRECTANGLE - mSpaceOfRectangle;
        float mHeightOfRectangle = getMeasuredHeight();

        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {

            float left = mSizeOfBorder / 2 + i * (mWidthOfRectangle + mSpaceOfRectangle);
            float top = mSizeOfBorder / 2;
            float right = left + mWidthOfRectangle - mSizeOfBorder;
            float bottom = top + mHeightOfRectangle - mSizeOfBorder;

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
            mPaintOfForeground.setColor(colorArr[mTempList.get(i)]);
            canvas.drawRoundRect(mFgRectf, mSizeOfRoundCircle / 2, mSizeOfRoundCircle / 2, mPaintOfForeground);
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

    }

    private void initSize() {
        mSizeOfBorder = ConvertUtils.dp2px(getContext(), 1);
        mSpaceOfRectangle = ConvertUtils.dp2px(getContext(), 0.5f);
        mSizeOfRoundCircle = ConvertUtils.dp2px(getContext(), 2);
    }

    public void fillingData(float[] pitch_part) {
        if (EmptyUtils.isEmpty(pitch_part) || pitch_part.length < NUMBEROFRECTANGLE) {
            return;
        }

        mScoreList = new ArrayList<>();
        mTempList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {
            float pitch = pitch_part[i];
            if (0 == pitch) {
                mScoreList.add(0);
            } else {
                mScoreList.add(random.nextInt(3) + 1);
            }
            mTempList.add(0);
        }

        ValueAnimator valueanimator = ValueAnimator.ofInt(0, NUMBEROFRECTANGLE - 1);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTempList.set(value, mScoreList.get(value));
                invalidate();
            }
        });
        valueanimator.setDuration(3000);
        valueanimator.start();
    }
}
