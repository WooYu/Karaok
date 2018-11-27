package com.library.multimedia.lyricscontrols.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.library.multimedia.lyricscontrols.parser.LrcRow;

import java.util.List;

/**
 * Created by wy201 on 2017-08-16.
 * 绘制预览的歌词：
 * 歌词颜色：蓝、红、绿
 * 是否可以拖动：可以
 */

public class LyricOfPreviewImpl extends View implements ILrcView {

    /**
     * 无歌词数据的时候 显示的默认文字
     **/
    private static final String DEFAULT_TEXT = "";
    /**
     * 默认缩放比例
     **/
    private static final float DEFAULT_SCALING_FACTOR = 1.0f;
    /***
     * 移动一句歌词的持续时间/歌词放大缩小的时间
     **/
    private static final int DURATION_FOR_LRC_SCROLL = 1000;
    /***
     * 停止触摸时 如果View需要滚动 时的持续时间
     **/
    private static final int DURATION_FOR_ACTION_UP = 400;
    /**
     * 高亮歌词的默认字体颜色
     **/
    private final int DEFAULT_COLOR_FOR_HIGHT_LIGHT_LRC = 0xffe5004f;
    /**
     * 其他歌词的默认字体颜色
     **/
    private final int DEFAULT_COLOR_FOR_OTHERLRC = 0xffffffff;
    /**
     * 所有的歌词
     ***/
    private List<LrcRow> mLrcRows;
    /**
     * 默认文字的字体大小
     **/
    private float DEFAULT_SIZE_FOR_NODATA;
    /**
     * 画高亮歌词的画笔
     ***/
    private Paint mPaintForHighLightLrc;
    /**
     * 高亮歌词的默认字体大小
     ***/
    private float DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC;
    /**
     * 高亮歌词当前的字体大小
     ***/
    private float mCurSizeForHightLightLrc;
    /**
     * 高亮歌词当前的字体颜色
     **/
    private int mCurColorForHightLightLrc = DEFAULT_COLOR_FOR_HIGHT_LIGHT_LRC;
    /**
     * 画其他歌词的画笔
     ***/
    private Paint mPaintForOtherLrc;
    /**
     * 其他歌词的默认字体大小
     ***/
    private float DEFAULT_SIZE_FOR_OTHER_LRC;
    /**
     * 其他歌词当前的字体大小
     ***/
    private float mCurSizeForOtherLrc;
    /**
     * 其他歌词当前的字体颜色
     **/
    private int mCurColorForOtherLrc = DEFAULT_COLOR_FOR_OTHERLRC;
    /**
     * 歌词间默认的行距
     **/
    private float DEFAULT_PADDING;
    /**
     * 歌词当前的行距
     **/
    private float mCurPadding;
    /**
     * 歌词的当前缩放比例
     **/
    private float mCurScalingFactor = DEFAULT_SCALING_FACTOR;
    /**
     * 实现歌词竖直方向平滑滚动的辅助对象
     **/
    private Scroller mScroller;
    /**
     * 控制文字缩放的因子
     **/
    private float mCurFraction = 0;
    /**
     * 判断为滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 是否可拖动歌词
     **/
    private boolean canDrag = false;
    /**
     * 事件的第一次的y坐标
     **/
    private float firstY;
    /**
     * 事件的上一次的y坐标
     **/
    private float lastY;
    private float lastX;
    /**
     * 屏幕上可见的歌词行数
     */
    private int mTotleDrawRow;
    /**
     * 当前高亮歌词的行号
     **/
    private int mCurRow = -1;
    /**
     * 向上滑动的最大距离
     */
    private float mMoveUpMaxDistance;
    /**
     * 描边的画笔
     */
    private Paint mPaintForOutLine;

    private int BORDERCOLOR_BLACK = 0xff160010;//描边颜色黑
    private OnSeekToListener onSeekToListener;
    private OnLrcClickListener onLrcClickListener;

    public LyricOfPreviewImpl(Context context) {
        super(context);
        init(context);
    }

    public LyricOfPreviewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化画笔等
     */
    @Override
    public void init(Context context) {
        DEFAULT_SIZE_FOR_NODATA = dip2px(context, 16);
        DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC = dip2px(context, 17);
        DEFAULT_SIZE_FOR_OTHER_LRC = dip2px(context, 14);
        DEFAULT_PADDING = dip2px(context, 15);

        mCurSizeForHightLightLrc = DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC;
        mCurSizeForOtherLrc = DEFAULT_SIZE_FOR_OTHER_LRC;
        mCurPadding = DEFAULT_PADDING;

        mScroller = new Scroller(getContext());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mPaintForHighLightLrc = new Paint();
        mPaintForHighLightLrc.setColor(mCurColorForHightLightLrc);
        mPaintForHighLightLrc.setTextSize(mCurSizeForHightLightLrc);
        mPaintForHighLightLrc.setAntiAlias(true);

        mPaintForOtherLrc = new Paint();
        mPaintForOtherLrc.setColor(mCurColorForOtherLrc);
        mPaintForOtherLrc.setTextSize(mCurSizeForOtherLrc);
        mPaintForOtherLrc.setFakeBoldText(true);
        mPaintForOtherLrc.setAntiAlias(true);

        mPaintForOutLine = new Paint();
        mPaintForOutLine.setTextSize(mPaintForOtherLrc.getTextSize());
        mPaintForOutLine.setFakeBoldText(true);
        mPaintForOutLine.setStyle(Paint.Style.STROKE);
        mPaintForOutLine.setColor(BORDERCOLOR_BLACK);
        mPaintForOutLine.setStrokeWidth(dip2px(context, 2));
        mPaintForOutLine.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLrcRows == null || mLrcRows.size() == 0) {
            //画默认的显示文字
            mPaintForOtherLrc.setColor(DEFAULT_COLOR_FOR_OTHERLRC);
            mPaintForOtherLrc.setTextSize(DEFAULT_SIZE_FOR_NODATA);
            float textWidth = mPaintForOtherLrc.measureText(DEFAULT_TEXT);
            float textX = (getWidth() - textWidth) / 2;
            canvas.drawText(DEFAULT_TEXT, textX, getHeight() / 2, mPaintForOtherLrc);
            return;
        }

        if (mTotleDrawRow == 0) {
            //初始化将要绘制的歌词行数
            mTotleDrawRow = (int) (getHeight() / (mCurSizeForOtherLrc + mCurPadding));
            mMoveUpMaxDistance = (mLrcRows.size() - mTotleDrawRow + 1) * (mCurSizeForOtherLrc + mCurPadding);
            mCurRow = mTotleDrawRow / 2;
        }

        //因为不需要将所有歌词画出来，所以需要知道展示的上下边界
        int minRaw = mCurRow - mTotleDrawRow;
        int maxRaw = mCurRow + mTotleDrawRow;
        minRaw = Math.max(minRaw, 0); //处理上边界
        maxRaw = Math.min(maxRaw, mLrcRows.size() - 1); //处理下边界
        //实现渐变的最大歌词行数
        int count = Math.max(maxRaw - mCurRow, mCurRow - minRaw);
        if (count == 0) {
            return;
        }
        //画出来的第一行歌词的y坐标
        float rowY = (minRaw + 1) * (mCurSizeForOtherLrc + mCurPadding);
        for (int i = minRaw; i <= maxRaw; i++) {

            mPaintForOtherLrc.setTextSize(mCurSizeForOtherLrc);
            String text = mLrcRows.get(i).getContent();
            float textWidth = mPaintForOtherLrc.measureText(text);
            float textX = (getWidth() - textWidth) / 2;
            //如果计算出的textX为负数，将textX置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
            textX = Math.max(textX, 0);
            mPaintForOtherLrc.setColor(mLrcRows.get(i).getLyriccolor());
            canvas.drawText(text, textX, rowY, mPaintForOutLine);
            canvas.drawText(text, textX, rowY, mPaintForOtherLrc);
            //计算出下一行歌词绘制的y坐标
            rowY += mCurSizeForOtherLrc + mCurPadding;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstY = event.getRawY();
                lastX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!canDrag) {
                    if (Math.abs(event.getRawY() - firstY) > mTouchSlop && Math.abs(event.getRawY() - firstY) > Math.abs(event.getRawX() - lastX)) {
                        canDrag = true;
                        mScroller.forceFinished(true);//强制滚动结束
                        mCurFraction = 1;
                    }
                    lastY = event.getRawY();
                }

                if (canDrag) {
                    float offset = event.getRawY() - lastY;//偏移量
                    if (getScrollY() - offset < 0) {
                        //第一行歌词显示且向下滑动
                        if (offset > 0) {
                            offset = offset / 3;
                        }
                    } else if (getScrollY() - offset > mMoveUpMaxDistance) {
                        // 最后一行歌词已显示，并且向上滑动
                        if (offset < 0) {//向上滑动
                            offset = offset / 3;
                        }
                    }
                    scrollBy(getScrollX(), -(int) offset);
                    lastY = event.getRawY();
                    //根据滑动的距离算出要显示的当前行
                    int currentRow = (int) (getScrollY() / (mCurSizeForOtherLrc + mCurPadding) + mTotleDrawRow / 2);
                    currentRow = Math.min(currentRow, mLrcRows.size() - 1);
                    currentRow = Math.max(currentRow, 0);
                    seekTo(mLrcRows.get(currentRow).getTime(), false, false);
                    return true;
                }
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!canDrag) {
                    if (onLrcClickListener != null) {
                        onLrcClickListener.onClick();
                    }
                } else {
                    if (onSeekToListener != null && mCurRow != -1) {
                        onSeekToListener.onSeekTo(mLrcRows.get(mCurRow).getTime());
                    }
                    if (getScrollY() < 0) {
                        smoothScrollTo(0, DURATION_FOR_ACTION_UP);
                    } else if (getScrollY() > mMoveUpMaxDistance) {
                        smoothScrollTo((int) mMoveUpMaxDistance, DURATION_FOR_ACTION_UP);
                    }

                    canDrag = false;
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 为LrcView设置歌词List集合数据
     */
    @Override
    public void setLrcRows(List<LrcRow> lrcRows) {
        reset();
        this.mLrcRows = lrcRows;
        invalidate();
    }

    @Override
    public void seekTo(int progress, boolean fromSeekBar, boolean fromSeekBarByUser) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }
        //如果是由seekbar的进度改变触发 并且这时候处于拖动状态，则返回
        if (fromSeekBar && canDrag) {
            return;
        }
        for (int i = mLrcRows.size() - 1; i >= 0; i--) {
            if (progress >= mLrcRows.get(i).getTime()) {
                if (mCurRow != i) {
                    mCurRow = i;
                    if (fromSeekBarByUser) {
                        if (!mScroller.isFinished()) {
                            mScroller.forceFinished(true);
                        }
                        scrollTo(getScrollX(), getScrollY());
                    } else {
                        smoothScrollTo(getScrollY(), DURATION_FOR_LRC_SCROLL);
                    }
                    invalidate();
                }
                break;
            }
        }

    }

    /**
     * 设置歌词的缩放比例
     */
    @Override
    public void setLrcScalingFactor(float scalingFactor) {
        mCurScalingFactor = scalingFactor;
        mCurSizeForHightLightLrc = DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC * mCurScalingFactor;
        mCurSizeForOtherLrc = DEFAULT_SIZE_FOR_OTHER_LRC * mCurScalingFactor;
        mCurPadding = DEFAULT_PADDING * mCurScalingFactor;
        mTotleDrawRow = (int) (getHeight() / (mCurSizeForOtherLrc + mCurPadding)) + 3;
        scrollTo(getScrollX(), (int) (mCurRow * (mCurSizeForOtherLrc + mCurPadding)));
        invalidate();
        mScroller.forceFinished(true);
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        mLrcRows = null;
        scrollTo(getScrollX(), 0);
        invalidate();
    }

    /**
     * 平滑的移动到某处
     *
     * @param dstY
     */
    private void smoothScrollTo(int dstY, int duration) {
        int oldScrollY = getScrollY();
        int offset = dstY - oldScrollY;
        mScroller.startScroll(getScrollX(), oldScrollY, getScrollX(), offset, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                int oldY = getScrollY();
                int y = mScroller.getCurrY();
                if (oldY != y && !canDrag) {
                    scrollTo(getScrollX(), y);
                }
                mCurFraction = mScroller.timePassed() * 3f / DURATION_FOR_LRC_SCROLL;
                log(mCurFraction);
                mCurFraction = Math.min(mCurFraction, 1F);
                invalidate();
            }
        }
    }

    /**
     * 返回当前的歌词缩放比例
     *
     * @return
     */
    public float getmCurScalingFactor() {
        return mCurScalingFactor;
    }

    public void setOnSeekToListener(OnSeekToListener onSeekToListener) {
        this.onSeekToListener = onSeekToListener;
    }

    public void setOnLrcClickListener(OnLrcClickListener onLrcClickListener) {
        this.onLrcClickListener = onLrcClickListener;
    }

    public void log(Object o) {
        Log.d("LrcView", o + "");
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnSeekToListener {
        void onSeekTo(int progress);
    }

    public interface OnLrcClickListener {
        void onClick();
    }
}
