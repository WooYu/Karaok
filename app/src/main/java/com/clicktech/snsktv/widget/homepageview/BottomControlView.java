package com.clicktech.snsktv.widget.homepageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.clicktech.snsktv.R;

/**
 * Created by Administrator on 2017/3/22.
 */

public class BottomControlView extends RelativeLayout implements View.OnClickListener {

    private FrameLayout flLeft;
    private ImageView ivLeftBg;
    private ImageView ivLeftSrc;
    private FrameLayout flMiddle;
    private ImageView ivMiddleBg;
    private ImageView ivMiddleBg2;
    private ImageView ivMiddleSrc;
    private FrameLayout flRight;
    private ImageView ivRightBg;
    private ImageView ivRightSrc;
    private float leftZoomRadio;
    private float rightZoomRadio;
    private float leftStartX;
    private float leftEndX;
    private float leftStartY;
    private float leftEndY;
    private float rightStartX;
    private float rightEndX;
    private float rightStartY;
    private float rightEndY;

    private int leftWidth;
    private int leftHeight;
    private int targetWidth;
    private int targetHeight;
    private int rightWidth;
    private int rightHeight;
    private boolean isFirstLayout = true;
    private BottomClickListener mClickListener;
    private int lastclickViewId = -1;

    public BottomControlView(Context context) {
        super(context);
        initView(context);

    }

    public BottomControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setBottomClickListener(BottomClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public void onClick(View v) {

        if (null == mClickListener)
            return;

        switch (v.getId()) {
            case R.id.iv_left_src:
                mClickListener.clickLeft();
                break;
            case R.id.iv_right_src:
                mClickListener.clickRight();
                break;
            case R.id.iv_middle_src:
                mClickListener.clickMiddle(lastclickViewId == -1 || lastclickViewId == v.getId());
                break;
        }
        lastclickViewId = v.getId();
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_bottom_switchcontrol, this, true);
        flLeft = (FrameLayout) findViewById(R.id.fl_left);
        ivLeftBg = (ImageView) findViewById(R.id.iv_left_bg);
        ivLeftSrc = (ImageView) findViewById(R.id.iv_left_src);
        flMiddle = (FrameLayout) findViewById(R.id.fl_middle);
        ivMiddleBg = (ImageView) findViewById(R.id.iv_middle_bg);
        ivMiddleBg2 = (ImageView) findViewById(R.id.iv_middle_bg2);
        ivMiddleSrc = (ImageView) findViewById(R.id.iv_middle_src);
        flRight = (FrameLayout) findViewById(R.id.fl_right);
        ivRightBg = (ImageView) findViewById(R.id.iv_right_bg);
        ivRightSrc = (ImageView) findViewById(R.id.iv_right_src);

        ivLeftSrc.setOnClickListener(this);
        ivRightSrc.setOnClickListener(this);
        ivMiddleSrc.setOnClickListener(this);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isFirstLayout)
            return;
        isFirstLayout = false;
        //计算出左边图片的缩放比例
        leftWidth = flLeft.getWidth();
        leftHeight = flLeft.getHeight();
        targetWidth = (ivMiddleBg2.getWidth() - ivMiddleSrc.getWidth()) / 2;
        targetHeight = ivMiddleBg2.getHeight();

        leftZoomRadio = calcuZoomRatio(leftWidth, leftHeight, targetWidth, targetHeight);

        //计算出右边图片的缩放比例
        rightWidth = flRight.getWidth();
        rightHeight = flRight.getHeight();

        rightZoomRadio = calcuZoomRatio(rightWidth, rightHeight, targetWidth, targetHeight);

        targetWidth = (int) (leftWidth * leftZoomRadio);
        targetHeight = (int) (leftHeight * leftZoomRadio);

        leftStartX = flLeft.getX();
        leftEndX = flMiddle.getX();
        leftStartY = flLeft.getY();
        leftEndY = ivMiddleBg2.getY() + flMiddle.getY();

        //右边起始和终止的getRight()和getTop()
        rightStartX = getWidth() - flRight.getRight();
        rightEndX = getWidth() - flMiddle.getRight();
        rightStartY = flRight.getY();
        rightEndY = ivMiddleBg2.getY() + flMiddle.getY();

        ivMiddleBg2.setAlpha(0f);
    }

    /**
     * 计算出缩放比例
     */
    private float calcuZoomRatio(int sWidth, int sHeight, int eWidth, int eHeight) {
        float widthRadio = (float) eWidth / sWidth;
        float heightRadio = (float) eHeight / sHeight;
        return Math.min(widthRadio, heightRadio);
    }

    //**********************跟随手指的移动来变化*********************

    /**
     * 大小改变：通过设置LayutParam
     * 透明度：通过设置Alpha
     * 位置改变：通过layoutParam
     */

    public void changeViewShrink(float radio) {
        Log.d("BottomControlView", "radio = " + radio + "-----------------");
        LayoutParams flLeftParams = (LayoutParams) flLeft.getLayoutParams();
        flLeftParams.width = (int) (leftWidth - (leftWidth - targetWidth) * radio);
        flLeftParams.height = (int) (leftHeight - (leftHeight - targetHeight) * radio);
//        flLeftParams.width = flLeftParams.height;
        flLeftParams.leftMargin = (int) ((leftEndX - leftStartX) * radio);
        flLeftParams.topMargin = (int) ((leftEndY - leftStartY) * radio);
//        Log.d("BottomControlView","flLeftParams.width = " + flLeftParams.width +",flLeftParams.height = " + flLeftParams.height);
        Log.d("BottomControlView", "flLeftParams.topMargin =" + flLeftParams.topMargin);
        flLeft.setLayoutParams(flLeftParams);

        LayoutParams flRightParams = (LayoutParams) flRight.getLayoutParams();
        flRightParams.width = (int) (rightWidth - (rightWidth - targetWidth) * radio);
        flRightParams.height = (int) (rightHeight - (rightHeight - targetHeight) * radio);
//        flRightParams.width = flRightParams.height;
        flRightParams.rightMargin = (int) ((rightEndX - rightStartX) * radio);
        flRightParams.topMargin = (int) ((rightEndY - rightStartY) * radio);
//        Log.d("BottomControlView","flRightParams.width = " + flRightParams.width+",flRightParams.height = " + flRightParams.height);
        Log.d("BottomControlView", "flRightParams.topMargin = " + flRightParams.topMargin);
        flRight.setLayoutParams(flRightParams);

        ivMiddleBg2.setScaleX(radio);
        ivMiddleBg2.setScaleY(radio);
        ivMiddleBg2.setAlpha(radio);

        ivMiddleBg.setAlpha(1 - radio);
        ivMiddleBg.setScaleX(1 - radio);
        ivMiddleBg.setScaleY(1 - radio);

        ivLeftBg.setAlpha(1 - radio);
//        ivLeftBg.setScaleX(1-radio);
//        ivLeftBg.setScaleY(1-radio);

        ivRightBg.setAlpha(1 - radio);
//        ivRightBg.setScaleX(1-radio);
//        ivRightBg.setScaleY(1-radio);
        invalidate();
    }

    public void changeView_left2MidSlide(float offset) {
        //两边变暗，中变亮
//        ivLeftSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_left_src), 1 - offset));
//        ivRightSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_right_src), 1 - offset));
//
//        ivMiddleSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_middle_src), offset));
    }

    public void changeView_mid2LeftSlide(float offset) {
        //中右变暗，左变亮
//        ivRightSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_right_src), offset));
//        ivMiddleSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_middle_src), offset));
//
//        ivLeftSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_left_src), 1 - offset));
    }

    public void changeView_mid2RightSlide(float offset) {
        //左中变暗，右变亮
//        ivLeftSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_left_src), 1 - offset));
//        ivMiddleSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_middle_src), 1 - offset));
//
//        ivRightSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_right_src), offset));
    }

    public void changeView_right2MidSlide(float offset) {
        //两边变暗，中变亮
//        ivLeftSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_left_src), offset));
//        ivRightSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_right_src), offset));
//
//        ivMiddleSrc.setImageBitmap(handleImageEffect(getBitmap(R.mipmap.main_middle_src), 1 - offset));
    }

    //设置亮度变化
    private Bitmap handleImageEffect(Bitmap bm, float lum) {
        lum = lum < 0.6f ? 0.6f : lum;

        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);

        return bmp;
    }

    //直接使用BitmapFactory进行转换
    public Bitmap getBitmap(int resid) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid);
        return bitmap;
    }

}



