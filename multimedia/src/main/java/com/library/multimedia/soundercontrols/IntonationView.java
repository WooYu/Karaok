package com.library.multimedia.soundercontrols;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.library.multimedia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy201 on 2017-09-06.
 * <p>
 * 外部传进来的数据：
 * 根据mid生成的打分数据、歌词数据
 */

public class IntonationView extends View {

    public static final String TAG = "IntonationView";

    private final int SCORELINECOLOR_YELLOW = Color.parseColor("#FFF45C");
    private final int SCORELINECOLOR_RED = Color.parseColor("#F04F43");
    private final int SCORELINECOLOR_GRAY = Color.parseColor("#898989");
    private final int NUMOFPITCHLINE = 16;  //音调线数量
    private int backgroundColor;//背景色
    private int pitchlineColor;//横向音调线颜色
    private int scorelineBorderColor;//打分线边框颜色
    private int progresslineColor;//移动的竖线颜色
    private int viewWidth;//视图宽
    private int viewHeight;//视图高
    private int minViewHeight;//最小视图高度
    private float minScoreLineHeight;//最小分数线高,不包含边框
    private float scoreBorderWidth;//打分线边框宽度
    private float pitchlineWidth;//音调线宽度
    private float progresslineWidth;//竖线宽度
    private float defaultPadding;//默认四周间距
    private float imgHeight;//图片高度
    private float mLocationOfHLine = 0;//竖线位置
    private int startAndEndPosition;//竖线的起始和结束位置
    private int mCurIndexOfLyric = 0;//当前歌词行数，随歌词切换变化

    private Paint mPitchLinePaint;//横向音调线画笔
    private Paint mScoreLinePaint;//标准打分线画笔
    private Paint mProgressLinePaint;//移动的竖线画笔
    private Paint mRealTimeScalePaint;//实时打分的画笔
    private Paint mIconPaint;//图标的画笔

    private SparseArray<List<GradeBean>> mMidDataForEachPage = new SparseArray<>();//每句歌词对应的mid数据
    private List<GradeBean> mRealTimeGradeList = new ArrayList<>();//实时的打分数据

    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private IconBean mIConBean;

    public IntonationView(Context context) {
        this(context, null);
    }

    public IntonationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntonationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initColor();
        initSize(context);
        initPaint();
        initResource(context);

        setBackgroundColor(backgroundColor);
    }

    //工具类
    public static int dp2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static float dp2pxF(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    //初始化颜色
    private void initColor() {
        backgroundColor = Color.parseColor("#7d110912");
        pitchlineColor = Color.parseColor("#20ffffff");
        scorelineBorderColor = Color.parseColor("#0101DA");
        progresslineColor = Color.WHITE;
    }

    //初始化尺寸
    private void initSize(Context context) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        minScoreLineHeight = dp2pxF(context, 3);
        scoreBorderWidth = dp2pxF(context, 1);
        defaultPadding = dp2pxF(context, 0);
        pitchlineWidth = dp2pxF(context, 1);
        progresslineWidth = dp2pxF(context, 1);
        startAndEndPosition = dp2px(context, 45);

        minViewHeight = (int) ((NUMOFPITCHLINE - 1) * (minScoreLineHeight + scoreBorderWidth * 2 + pitchlineWidth) +
                defaultPadding * 2);
        viewWidth = screenWidth;
    }

    //初始化画笔
    private void initPaint() {
        mPitchLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPitchLinePaint.setColor(pitchlineColor);
        mPitchLinePaint.setStrokeWidth(pitchlineWidth);

        mScoreLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRealTimeScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRealTimeScalePaint.setColor(SCORELINECOLOR_RED);
        mRealTimeScalePaint.setStyle(Paint.Style.FILL);

        mProgressLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressLinePaint.setStrokeWidth(progresslineWidth);
        mProgressLinePaint.setColor(progresslineColor);

        mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    //初始化资源
    private void initResource(Context context) {

        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.rating_01);//kobushi
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.rating_02);//vibrato
        bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.rating_03);//fall
        bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.rating_04);//shakuri
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            viewHeight = Math.max(heightSize, minViewHeight);
        } else {
            viewHeight = minViewHeight;
        }
        minScoreLineHeight = (viewHeight - defaultPadding * 2) / (NUMOFPITCHLINE - 1)
                - scoreBorderWidth * 2 - pitchlineWidth;
        imgHeight = (minScoreLineHeight + scoreBorderWidth * 2) * 3 / 2 + pitchlineWidth;
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawIntonationLine(canvas);
        drawStandardScore(canvas);
        drawMovingVerticalLine(canvas);
        drawRealTimeRatingData(canvas);
    }

    //绘制音准线
    private void drawIntonationLine(Canvas canvas) {
        canvas.save();
        float yCoordinate;//y轴坐标
        for (int i = 0; i < NUMOFPITCHLINE - 1; i++) {
            yCoordinate = defaultPadding + minScoreLineHeight + scoreBorderWidth * 2 + pitchlineWidth / 2 +
                    (pitchlineWidth + minScoreLineHeight + 2 * scoreBorderWidth) * i;
            canvas.drawLine(0, yCoordinate, viewWidth, yCoordinate, mPitchLinePaint);
        }
        canvas.restore();
    }

    //绘制评分标准
    private void drawStandardScore(Canvas canvas) {
        if (null == mMidDataForEachPage || mMidDataForEachPage.size() == 0) {
            return;
        }

        if (mCurIndexOfLyric > mMidDataForEachPage.size()) {
            Log.e(TAG, "drawStandardScore: 歌词显示到最后一句");
            return;
        }

        List<GradeBean> standardScoreList = mMidDataForEachPage.get(mCurIndexOfLyric);
        if (null == standardScoreList || standardScoreList.size() == 0) {
            return;
        }

        canvas.save();
        for (int i = 0; i < standardScoreList.size(); i++) {
            GradeBean gradeBean = standardScoreList.get(i);
            int noteY = gradeBean.getNoteY();
            gradeBean.setNoteY(noteY > (NUMOFPITCHLINE - 2) ? (NUMOFPITCHLINE - 2) : noteY);
            //先绘制内部矩形，再绘制边框
            float left = gradeBean.getNoteX1() + startAndEndPosition;
            float top = defaultPadding + scoreBorderWidth +
                    gradeBean.getNoteY() * (minScoreLineHeight + scoreBorderWidth * 2 + pitchlineWidth);
            float right = gradeBean.getNoteX2() + startAndEndPosition;
            float bottom = top + minScoreLineHeight;

            mScoreLinePaint.setColor(SCORELINECOLOR_GRAY);
            mScoreLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(left, top, right, bottom, mScoreLinePaint);

            mScoreLinePaint.setStyle(Paint.Style.STROKE);
            mScoreLinePaint.setStrokeWidth(scoreBorderWidth);
            mScoreLinePaint.setColor(scorelineBorderColor);
            canvas.drawRect(left - scoreBorderWidth / 2, top - scoreBorderWidth / 2,
                    right + scoreBorderWidth / 2, bottom + scoreBorderWidth / 2, mScoreLinePaint);

        }
        canvas.restore();
    }

    //绘制实时打分
    private void drawRealTimeRatingData(Canvas canvas) {
        if (null == mRealTimeGradeList || mRealTimeGradeList.size() == 0) {
            return;
        }

        canvas.save();
        for (int i = 0; i < mRealTimeGradeList.size(); i++) {
            GradeBean gradeBean = mRealTimeGradeList.get(i);
            int noteY = gradeBean.getNoteY();

            float left = gradeBean.getNoteX1() + startAndEndPosition;
            float right;
            float diffOfHLine = mLocationOfHLine - gradeBean.getCurPositionOfHLine();
            float diffOfNoteX = gradeBean.getNoteX2() - gradeBean.getNoteX1();
            if (i == mRealTimeGradeList.size() - 1 && diffOfHLine < diffOfNoteX) {
                right = left + diffOfHLine;
            } else {
                right = left + diffOfNoteX;
            }
            float top;
            float bottom;
            if (gradeBean.isMatch()) {
                noteY = noteY > (NUMOFPITCHLINE - 2) ? (NUMOFPITCHLINE - 2) : noteY;
                top = defaultPadding + scoreBorderWidth +
                        noteY * (minScoreLineHeight + scoreBorderWidth * 2 + pitchlineWidth);
                mRealTimeScalePaint.setColor(SCORELINECOLOR_YELLOW);
            } else {
                noteY = noteY > (NUMOFPITCHLINE - 3) ? (NUMOFPITCHLINE - 3) : (noteY + 1);
                Log.d(TAG, "drawRealTimeRatingData: noteY = " + gradeBean.getNoteY());
                top = defaultPadding + scoreBorderWidth +
                        noteY * (minScoreLineHeight + scoreBorderWidth * 2 + pitchlineWidth);
                mRealTimeScalePaint.setColor(SCORELINECOLOR_RED);
            }
            bottom = top + minScoreLineHeight;
            mRealTimeScalePaint.setStyle(Paint.Style.FILL);
            //绘制矩形
            canvas.drawRect(left, top, right, bottom, mRealTimeScalePaint);

            mRealTimeScalePaint.setStyle(Paint.Style.STROKE);
            mRealTimeScalePaint.setStrokeWidth(scoreBorderWidth);
            mRealTimeScalePaint.setColor(scorelineBorderColor);
            //绘制边框
            canvas.drawRect(left - scoreBorderWidth / 2, top - scoreBorderWidth / 2,
                    right + scoreBorderWidth / 2, bottom + scoreBorderWidth / 2, mRealTimeScalePaint);

            //绘制图标
            Bitmap bitmap = null;
            switch (gradeBean.getIndexOfIcon()) {
                case 1:
                    bitmap = bitmap1;
                    break;
                case 2:
                    bitmap = bitmap2;
                    break;
                case 3:
                    bitmap = bitmap3;
                    break;
                case 4:
                    bitmap = bitmap4;
                    break;
            }

            if (null == bitmap || null == mIConBean) {
                continue;
            }
            if (i == mRealTimeGradeList.size() - 1 && diffOfHLine < diffOfNoteX) {
                drawImage(canvas, bitmap, left + mIConBean.getLeft(),
                        bottom - mIConBean.getTop(), mIConBean.getWidth(), mIConBean.getHeight(), true);
            } else {
                drawImage(canvas, bitmap, left + (gradeBean.getNoteX2() - gradeBean.getNoteX1() - imgHeight) / 2,
                        bottom, imgHeight * 4 / 3, imgHeight, false);
            }
        }
        canvas.restore();
    }

    //设置移动的竖线
    private void drawMovingVerticalLine(Canvas canvas) {
        if (mLocationOfHLine < 0) {
//            Log.e(TAG, "竖线绘制超过了指定区域");
            return;
        }
        canvas.save();
        canvas.drawLine(mLocationOfHLine + startAndEndPosition, 0,
                mLocationOfHLine + startAndEndPosition, viewHeight, mProgressLinePaint);
        canvas.restore();
    }

    //用于设置数据
    public void setData(SparseArray<List<GradeBean>> hashMap) {
        if (null == hashMap || hashMap.size() == 0) {
            return;
        }

        this.mMidDataForEachPage = hashMap;

        requestLayout();
        invalidate();
    }

    //更新打分数据
    public void updateRatingData(GradeBean gradeBean, float positionSecX) {

        mLocationOfHLine = positionSecX;
        if (null != gradeBean) {
            gradeBean.setCurPositionOfHLine(positionSecX);
            mRealTimeGradeList.add(gradeBean);
            startIconAnimation(gradeBean);
        }

        invalidate();
    }

    //设置图标动画
    private void startIconAnimation(final GradeBean gradeBean) {
        mIConBean = new IconBean();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                float value2 = currentValue > currentValue * 3 / 5 ? 1 : currentValue;
                mIConBean.setAlpha((int) (255 * value2));
                mIConBean.setHeight(value2 * imgHeight);
                mIConBean.setWidth(mIConBean.getHeight() * 4 / 3);
                mIConBean.setLeft((gradeBean.getNoteX2() - gradeBean.getNoteX1() - imgHeight) / 2);
                mIConBean.setTop((1 - currentValue) * (minScoreLineHeight + scoreBorderWidth * 2));

            }
        });

        animator.start();
    }

    //获取竖线的起始和终止位置
    public int getStartingPositionOfVLine() {
        return startAndEndPosition;
    }

    //获取音准器的宽度
    public int getWidthOfIntonationView() {
        return viewWidth - startAndEndPosition * 2;
    }

    //获取音准线的数量
    public int getNumOfPitchLine() {
        return NUMOFPITCHLINE;
    }

    //切换到下一句歌词
    public void jumpToTheNextSentence() {
        //清空打分数据和mid数据
        ++mCurIndexOfLyric;
        mRealTimeGradeList.clear();
    }

    /*---------------------------------
     * 绘制图片
     * @param       x屏幕上的x坐标
     * @param       y屏幕上的y坐标
     * @param       w要绘制的图片的宽度
     * @param       h要绘制的图片的高度
     * @param       bx图片上的x坐标
     * @param       by图片上的y坐标
     *
     * @return      null
     ------------------------------------*/

    private void drawImage(Canvas canvas, Bitmap blt, float x, float y,
                           float w, float h, boolean isLast) {
        RectF dst = new RectF();// 屏幕 >>目标矩形

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        if (isLast) {
            mIconPaint.setAlpha(mIConBean.getAlpha());
        } else {
            mIConBean.setAlpha(255);
        }

        canvas.drawBitmap(blt, null, dst, mIconPaint);
    }

}
