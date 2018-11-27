package com.clicktech.snsktv.widget.quicksearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DeviceUtils;
import com.clicktech.snsktv.common.KtvApplication;

import java.util.Arrays;
import java.util.List;

public class SearchIndexView extends View {
    private static String[] jp_index = {"あ", "か", "さ", "た", "な", "は", "ま", "や", "ら",
            "わ", "ん", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private static String[] nojp_index = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "あ", "か", "さ", "た", "な", "は", "ま", "や", "ら",
            "わ", "ん", "#"};

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int choose = -1;
    private Paint paint = new Paint();
    private List<String> indexList;
    private TextView mTextDialog;

    public SearchIndexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SearchIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchIndexView(Context context) {
        super(context);
        init();
    }

    private void init() {
        KtvApplication application = KtvApplication.ktvApplication;
        String local = application.getLocaleCode();
        if (local.equals(application.getString(R.string.language_japan))) {
            indexList = Arrays.asList(jp_index);
        } else {
            indexList = Arrays.asList(nojp_index);
        }
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / indexList.size();

        for (int i = 0; i < indexList.size(); i++) {
            String letter = indexList.get(i);
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setAntiAlias(true);
            paint.setTextSize(DeviceUtils.dpToPixel(getContext(), 10));

            if (i == choose) {
                paint.setColor(Color.parseColor("#000000"));
                paint.setFakeBoldText(true);
            }

            float xPos = width / 2 - paint.measureText(letter) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letter, xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * indexList.size());

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundResource(R.drawable.shape_cc_sort_sidebarbg);
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundResource(R.drawable.shape_cc_sort_sidebarbg);
                if (oldChoose != c) {
                    if (c >= 0 && c < indexList.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(indexList.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(indexList.get(c));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }


    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}