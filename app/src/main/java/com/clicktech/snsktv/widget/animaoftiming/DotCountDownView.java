package com.clicktech.snsktv.widget.animaoftiming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DeviceUtils;

/**
 * Created by wy201 on 2018-03-27.
 */

public class DotCountDownView extends View {

    private int mNumOfDot = 5;// 点的数量
    private Paint mPaint;
    private float mDotSize;//点的大小
    private TimingHandler timingHandler;
    private TimingListener mListener;

    public DotCountDownView(Context context) {
        super(context);
        init();
    }

    public DotCountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDotSize = DeviceUtils.dpToPixel(getContext(), 6f);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getContext().getResources().getColor(R.color.white));
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startX = (getMeasuredWidth() - mDotSize * 9) / 2;
        float startY = getMeasuredHeight() / 2;
        for (int i = 0; i < mNumOfDot; i++) {
            canvas.drawCircle(
                    startX + mDotSize / 2 + (mDotSize * 2) * i,
                    startY,
                    mDotSize / 2,
                    mPaint);
        }
    }

    public void setTimingListener(TimingListener listener) {
        this.mListener = listener;
    }

    public void startCountDown() {
        timingHandler = new TimingHandler();
        timingHandler.sendMessageDelayed(timingHandler.obtainMessage(), 1000);
    }

    public void endCountDown() {
        if (null != timingHandler) {
            timingHandler.removeCallbacksAndMessages(null);
        }
    }

    public interface TimingListener {
        void endingTheTimer();
    }

    class TimingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mNumOfDot == 0 && null != mListener) {
                mListener.endingTheTimer();
            }

            if (mNumOfDot > 0) {
                --mNumOfDot;
                invalidate();
                sendMessageDelayed(new Message(), 1000);
            }
        }
    }
}
