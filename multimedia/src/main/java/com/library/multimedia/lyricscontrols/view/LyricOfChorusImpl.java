package com.library.multimedia.lyricscontrols.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.library.multimedia.R;
import com.library.multimedia.lyricscontrols.parser.LrcRow;
import com.library.multimedia.uitls.BitmapUtils;

import java.util.List;

import static android.graphics.BitmapFactory.decodeResource;

/***
 * 合唱歌词：
 *      歌词颜色：蓝、绿、红
 *      角色切换那行歌词带头像，合唱时绘制两个头像
 *      当前的歌词放大
 *
 */
public class LyricOfChorusImpl extends View implements ILrcView {
    /**
     * 歌词的最大缩放比例
     **/
    public final float MAX_SCALING_FACTOR = 1.5f;
    /**
     * 歌词的最小缩放比例
     **/
    public final float MIN_SCALING_FACTOR = 0.5f;
    /**
     * 高亮歌词的默认字体颜色
     **/
    private final int DEFAULT_COLOR_FOR_HIGHT_LIGHT_LRC = 0xffe5004f;
    /**
     * 其他歌词的默认字体颜色
     **/
    private final int DEFAULT_COLOR_FOR_OTHER_LRC = 0x80ffffff;
    /**
     * 默认缩放比例
     **/
    private final float DEFAULT_SCALING_FACTOR = 1.0f;
    /***
     * 移动一句歌词的持续时间/歌词放大缩小的时间
     **/
    private final int DURATION_FOR_LRC_SCROLL = 1000;
    /***
     * 停止触摸时 如果View需要滚动 时的持续时间
     **/
    private final int DURATION_FOR_ACTION_UP = 400;
    /**
     * 所有的歌词
     ***/
    private List<LrcRow> mLrcRows;
    /**
     * 无歌词数据的时候 显示的默认文字
     **/
    private String DEFAULT_TEXT = "";
    /**
     * 默认文字的字体大小
     **/
    private float DEFAULT_SIZE_FOR_NODATA;
    /**
     * 描边画笔
     */
    private Paint mPaintForOutLine;
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
    private float mCurSizeForHightLightLrc = DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC;
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
    private float mCurSizeForOtherLrc = DEFAULT_SIZE_FOR_OTHER_LRC;
    /**
     * 其他歌词当前的字体颜色
     **/
    private int mCurColorForOtherLrc = DEFAULT_COLOR_FOR_OTHER_LRC;
    /**
     * 歌词间默认的行距
     **/
    private float DEFAULT_PADDING;
    /**
     * 歌词当前的行距
     **/
    private float mCurPadding = DEFAULT_PADDING;
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
    private int mTouchSlop;


    private Bitmap roleABitmap;//角色A的头像
    private Bitmap roleBBitmap;//角色B的头像
    private Bitmap avatarDefault;//默认头像
    private Bitmap roleABackground;//角色A的背景
    private Bitmap roleBBackground;//角色B的背景
    private int backgroundSize;//背景尺寸
    private int avatarSize;//头像尺寸
    private int intervalOfAvatarLyric;//头像和歌词之间的间隙

    private int BORDERCOLOR_BLACK = 0xff160010;//描边颜色黑
    private int mTotleDrawRow;
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
     * 当前高亮歌词的行号
     **/
    private int mCurRow = -1;
    /**
     * 上一次的高亮歌词的行号
     **/
    private int mLastRow = -1;
    /**
     * 控制歌词水平滚动的属性动画
     ***/
    private ValueAnimator mAnimator;
    /**
     * 高亮歌词当前的其实x轴绘制坐标
     **/
    private float mCurTextXForHighLightLrc;
    /***
     * 监听属性动画的数值值的改变
     */
    AnimatorUpdateListener updateListener = new AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            mCurTextXForHighLightLrc = (Float) animation.getAnimatedValue();
            log("mCurTextXForHighLightLrc=" + mCurTextXForHighLightLrc);
            invalidate();
        }
    };
    private OnSeekToListener onSeekToListener;
    private OnLrcClickListener onLrcClickListener;
    private OnLrcMoveToNextListener onLrcMoveToNextListener;

    public LyricOfChorusImpl(Context context) {
        super(context);

        init(context);
    }

    public LyricOfChorusImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化画笔等
     */
    @Override
    public void init(Context context) {
        DEFAULT_SIZE_FOR_NODATA = dip2px(context, 16);
        DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC = dip2px(context, 20);
        DEFAULT_SIZE_FOR_OTHER_LRC = dip2px(context, 16);
        DEFAULT_PADDING = dip2px(context, 15);
        mCurSizeForHightLightLrc = DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC;
        mCurSizeForOtherLrc = DEFAULT_SIZE_FOR_OTHER_LRC;
        mCurPadding = DEFAULT_PADDING;
        backgroundSize = dip2px(context, 16);
        avatarSize = dip2px(context, 10);
        intervalOfAvatarLyric = dip2px(context, 5);
        DEFAULT_TEXT = context.getString(R.string.nolyricdata);

        mScroller = new Scroller(getContext());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();//16

        mPaintForHighLightLrc = new Paint();
        mPaintForHighLightLrc.setColor(mCurColorForHightLightLrc);
        mPaintForHighLightLrc.setTextSize(mCurSizeForHightLightLrc);
        mPaintForHighLightLrc.setFakeBoldText(true);
        mPaintForHighLightLrc.setAntiAlias(true);

        mPaintForOtherLrc = new Paint();
        mPaintForOtherLrc.setColor(mCurColorForOtherLrc);
        mPaintForOtherLrc.setFakeBoldText(true);
        mPaintForOtherLrc.setTextSize(mCurSizeForOtherLrc);
        mPaintForOtherLrc.setAntiAlias(true);

        mPaintForOutLine = new Paint();
        mPaintForOutLine.setTextSize(mPaintForOtherLrc.getTextSize());
        mPaintForOutLine.setTypeface(mPaintForOtherLrc.getTypeface());
        mPaintForOutLine.setFlags(mPaintForOtherLrc.getFlags());
        mPaintForOutLine.setAlpha(mPaintForOtherLrc.getAlpha());
        mPaintForOutLine.setFakeBoldText(true);
        mPaintForOutLine.setStyle(Paint.Style.STROKE);
        mPaintForOutLine.setColor(Color.WHITE);
        mPaintForOutLine.setStrokeWidth(dip2px(context, 2));

        roleABackground = decodeResource(context.getResources(), R.mipmap.red_bg);
        roleBBackground = decodeResource(context.getResources(), R.mipmap.blue_bg);
        avatarDefault = decodeResource(context.getResources(), R.mipmap.def_circle_avatar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        //初始化将要绘制的歌词行数
        mTotleDrawRow = (int) (viewHeight / (mCurSizeForOtherLrc + mCurPadding)) + 4;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLrcRows == null || mLrcRows.size() == 0) {
            //画默认的显示文字
            mPaintForOtherLrc.setTextSize(DEFAULT_SIZE_FOR_NODATA);
            float textWidth = mPaintForOtherLrc.measureText(DEFAULT_TEXT);
            float textX = (getWidth() - textWidth) / 2;
            canvas.drawText(DEFAULT_TEXT, textX, getHeight() / 2, mPaintForOtherLrc);
            return;
        }

        //因为不需要将所有歌词画出来
        int minRaw = mCurRow - (mTotleDrawRow - 1) / 2;
        int maxRaw = mCurRow + (mTotleDrawRow - 1) / 2;
        minRaw = Math.max(minRaw, 0); //处理上边界
        maxRaw = Math.min(maxRaw, mLrcRows.size() - 1); //处理下边界
        //实现渐变的最大歌词行数
        int count = Math.max(maxRaw - mCurRow, mCurRow - minRaw);
        if (count == 0) {
            return;
        }

        //获取头像
        roleABitmap = null == roleABitmap ? avatarDefault : roleABitmap;
        roleBBitmap = null == roleBBitmap ? avatarDefault : roleBBitmap;

        //画出来的第一行歌词的y坐标
        float rowY = getHeight() / 2 + minRaw * (mCurSizeForOtherLrc + mCurPadding);
        for (int i = minRaw; i <= maxRaw; i++) {
            LrcRow curLrcRow = mLrcRows.get(i);
            if (i == mCurRow) {//画高亮歌词
                //因为有缩放效果，所有需要动态设置歌词的字体大小
                float textSize = mCurSizeForOtherLrc + (mCurSizeForHightLightLrc - mCurSizeForOtherLrc) * mCurFraction;
                mPaintForHighLightLrc.setTextSize(textSize);

                String text = curLrcRow.getContent();//获取到高亮歌词
                float textWidth = mPaintForHighLightLrc.measureText(text);//用画笔测量歌词的宽度
                mPaintForOutLine.setTextSize(mPaintForHighLightLrc.getTextSize());
                mPaintForOutLine.setColor(Color.WHITE);

                if (textWidth > getWidth()) {
                    //如果歌词宽度大于view的宽，则需要动态设置歌词的起始x坐标，以实现水平滚动
                    canvas.drawText(text, mCurTextXForHighLightLrc, rowY, mPaintForOutLine);
                    canvas.drawText(text, mCurTextXForHighLightLrc, rowY, mPaintForHighLightLrc);
                } else {
                    //如果歌词宽度小于view的宽，则让歌词居中显示
                    float textX = (getWidth() - textWidth) / 2;
                    canvas.drawText(text, textX, rowY, mPaintForOutLine);
                    canvas.drawText(text, textX, rowY, mPaintForHighLightLrc);
                }
            } else {
                if (i == mLastRow) {//画高亮歌词的上一句
                    //因为有缩放效果，所有需要动态设置歌词的字体大小。当字体大小不一致时，滑动就可以看出效果
                    float textSize = mCurSizeForHightLightLrc - (mCurSizeForHightLightLrc - mCurSizeForOtherLrc) * mCurFraction;
                    mPaintForOtherLrc.setTextSize(textSize);
                } else {//画其他的歌词
                    mPaintForOtherLrc.setTextSize(mCurSizeForOtherLrc);
                }
                String text = curLrcRow.getContent();
                float textWidth = mPaintForOtherLrc.measureText(text);
                float textX = (getWidth() - textWidth) / 2;
                //如果计算出的textX为负数，将textX置为0(实现：如果歌词宽大于view宽，则居左显示，否则居中显示)
                textX = Math.max(textX, 0);
                mPaintForOtherLrc.setColor(curLrcRow.getLyriccolor());

                mPaintForOutLine.setTextSize(mPaintForOtherLrc.getTextSize());
                mPaintForOutLine.setColor(BORDERCOLOR_BLACK);
                canvas.drawText(text, textX, rowY, mPaintForOutLine);
                canvas.drawText(text, textX, rowY, mPaintForOtherLrc);


                //绘制头像
                Paint.FontMetrics fontMetrics = mPaintForOtherLrc.getFontMetrics();
                if (i > mCurRow && curLrcRow.isSwitch()) {
                    int bgStartX = (int) Math.max((textX - backgroundSize - intervalOfAvatarLyric), 0);
                    int bgStartY = (int) (fontMetrics.ascent + rowY);
                    int avatarpadding = (backgroundSize - avatarSize) / 2;
                    int avatarX = bgStartX + avatarpadding;
                    int avatarY = bgStartY + avatarpadding;

                    if (curLrcRow.getRoletype() == 1) {
                        drawImage(canvas, roleABackground, bgStartX, bgStartY, backgroundSize * 6 / 5, backgroundSize);
                        drawImage(canvas, roleABitmap, avatarX, avatarY, avatarSize, avatarSize);
                    } else if (curLrcRow.getRoletype() == 2) {
                        drawImage(canvas, roleBBackground, bgStartX, bgStartY, backgroundSize * 6 / 5, backgroundSize);
                        drawImage(canvas, roleBBitmap, avatarX, avatarY, avatarSize, avatarSize);
                    } else if (curLrcRow.getRoletype() == 3) {
                        drawImage(canvas, roleBBackground, bgStartX, bgStartY, backgroundSize * 6 / 5, backgroundSize);
                        drawImage(canvas, roleBBitmap, avatarX, avatarY, avatarSize, avatarSize);

                        drawImage(canvas, roleABackground, bgStartX - backgroundSize, bgStartY, backgroundSize * 6 / 5, backgroundSize);
                        drawImage(canvas, roleABitmap, avatarX - backgroundSize, avatarY, avatarSize, avatarSize);
                    }
                }

            }

            //计算出下一行歌词绘制的y坐标
            rowY += mCurSizeForOtherLrc + mCurPadding;
        }

    }

    //关闭拖动歌词的功能
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (mLrcRows == null || mLrcRows.size() == 0) {
//            return false;
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                firstY = event.getRawY();
//                lastX = event.getRawX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!canDrag) {
//                    if (Math.abs(event.getRawY() - firstY) > mTouchSlop && Math.abs(event.getRawY() - firstY) > Math.abs(event.getRawX() - lastX)) {
//                        canDrag = true;
//                        mScroller.forceFinished(true);//强制滚动结束
//                        stopScrollLrc();
//                        mCurFraction = 1;
//                    }
//                    lastY = event.getRawY();
//                }
//
//                if (canDrag) {
//                    float offset = event.getRawY() - lastY;//偏移量
//                    if (getScrollY() - offset < 0) {
//                        //第一行歌词显示且向下滑动
//                        if (offset > 0) {
//                            offset = offset / 3;
//                        }
//                    } else if (getScrollY() - offset > mLrcRows.size() * (mCurSizeForOtherLrc + mCurPadding) - mCurPadding) {
//                        // 最后一行歌词已显示，并且向上滑动
//                        if (offset < 0) {//向上滑动
//                            offset = offset / 3;
//                        }
//                    }
//                    scrollBy(getScrollX(), -(int) offset);
//                    lastY = event.getRawY();
//                    //根据滑动的距离算出要显示的当前行
//                    int currentRow = (int) (getScrollY() / (mCurSizeForOtherLrc + mCurPadding));
////                    log("getScrollY() =" + getScrollY() +", currentRow = " + currentRow);
//                    currentRow = Math.min(currentRow, mLrcRows.size() - 1);
//                    currentRow = Math.max(currentRow, 0);
//                    seekTo(mLrcRows.get(currentRow).getTime(), false, false);
//                    return true;
//                }
//                lastY = event.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if (!canDrag) {
//                    if (onLrcClickListener != null) {
//                        onLrcClickListener.onClick();
//                    }
//                } else {
//                    if (onSeekToListener != null && mCurRow != -1) {
//                        onSeekToListener.onSeekTo(mLrcRows.get(mCurRow).getTime());
//                    }
//                    if (getScrollY() < 0) {
//                        smoothScrollTo(0, DURATION_FOR_ACTION_UP);
//                    } else if (getScrollY() > mLrcRows.size() * (mCurSizeForOtherLrc + mCurPadding) - mCurPadding) {
//                        smoothScrollTo((int) (mLrcRows.size() * (mCurSizeForOtherLrc + mCurPadding) - mCurPadding), DURATION_FOR_ACTION_UP);
//                    }
//
//                    canDrag = false;
//                    invalidate();
//                }
//                break;
//        }
//        return true;
//    }

    /**
     * 为LrcView设置歌词List集合数据
     */
    @Override
    public void setLrcRows(List<LrcRow> lrcRows) {
        reset();
        this.mLrcRows = lrcRows;
        invalidate();
    }

    public void setRoleAAvatar(Bitmap bitmapA) {
        if (null == bitmapA) {
            return;
        }
        roleABitmap = BitmapUtils.toRoundBitmap(bitmapA);
        invalidate();
    }

    public void setRoleBBitmap(Bitmap bitmapB) {
        if (null == bitmapB) {
            return;
        }
        roleBBitmap = BitmapUtils.toRoundBitmap(bitmapB);
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
                    mLastRow = mCurRow;
                    mCurRow = i;
                    log("mCurRow = i = " + i);
                    if (fromSeekBarByUser) {
                        if (!mScroller.isFinished()) {
                            mScroller.forceFinished(true);
                        }
                        scrollTo(getScrollX(), (int) (mCurRow * (mCurSizeForOtherLrc + mCurPadding)));
                    } else {
                        smoothScrollTo((int) (mCurRow * (mCurSizeForOtherLrc + mCurPadding)), DURATION_FOR_LRC_SCROLL);
                        if (null != onLrcMoveToNextListener) {
                            onLrcMoveToNextListener.moveToNext(progress);
                        }
                    }
                    //如果高亮歌词的宽度大于View的宽，就需要开启属性动画，让它水平滚动
                    float textWidth = mPaintForHighLightLrc.measureText(mLrcRows.get(mCurRow).getContent());
//                    log("textWidth=" + textWidth + "，getWidth()=" + getWidth());
                    if (textWidth > getWidth()) {
                        if (fromSeekBarByUser) {
                            mScroller.forceFinished(true);
                        }
                        log("开始水平滚动歌词:" + mLrcRows.get(mCurRow).getContent());
                        startScrollLrc(getWidth() - textWidth, (long) (mLrcRows.get(mCurRow).getTotalTime() * 0.6));
                    }
                    invalidate();
                }
                break;
            }
        }

    }

    /**
     * 开始水平滚动歌词
     *
     * @param endX     歌词第一个字的最终的x坐标
     * @param duration 滚动的持续时间
     */
    private void startScrollLrc(float endX, long duration) {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, endX);
            mAnimator.addUpdateListener(updateListener);
        } else {
            mCurTextXForHighLightLrc = 0;
            mAnimator.cancel();
            mAnimator.setFloatValues(0, endX);
        }
        mAnimator.setDuration(duration);
        mAnimator.setStartDelay((long) (duration * 0.3)); //延迟执行属性动画
        mAnimator.start();
    }

    /**
     * 停止歌词的横向滚动
     */
    private void stopScrollLrc() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mCurTextXForHighLightLrc = 0;
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
        log("mRowTotal=" + mTotleDrawRow);
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
        log("smoothScrollTo(): dstY = " + dstY);
        int oldScrollY = getScrollY();
        int offset = dstY - oldScrollY;
        mScroller.startScroll(getScrollX(), oldScrollY, getScrollX(), offset, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        log("computeScroll()");
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                int oldY = getScrollY();
                int y = mScroller.getCurrY();
                if (oldY != y && !canDrag) {
                    scrollTo(getScrollX(), y);
                }
                log("oldY = " + oldY + " , y = " + y + ", mScroller.timePassed()=" + mScroller.timePassed());
                mCurFraction = mScroller.timePassed() * 3f / DURATION_FOR_LRC_SCROLL;//TODO:为什么这么算mCurFraction?
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

    public void setOnLrcMoveToNextListener(OnLrcMoveToNextListener listener) {
        this.onLrcMoveToNextListener = listener;
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

    private void drawImage(Canvas canvas, Bitmap blt, int x, int y,
                           int w, int h) {
        RectF dst = new RectF();// 屏幕 >>目标矩形

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;

        canvas.drawBitmap(blt, null, dst, mPaintForOtherLrc);
        dst = null;
    }

    public interface OnSeekToListener {
        void onSeekTo(int progress);
    }

    public interface OnLrcClickListener {
        void onClick();
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

    public interface OnLrcMoveToNextListener {
        void moveToNext(int curstarttime);
    }
}
