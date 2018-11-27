package com.clicktech.snsktv.widget.titlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Administrator on 2017/2/6.
 * 公共标题具有的功能：
 * 将左右两边的点击事件暴露出去
 * 有属性设置标题、文字、图标、标题透明度
 * 动态改变标题透明度
 */

public class HeaderView extends AutoRelativeLayout implements View.OnClickListener {

    /*<attr name="titleLeftText" format="string"/>
    <attr name="titleLeftImg" format="reference"/>
    <attr name="titleName" format="string"/>
    <attr name="titleRightText" format="string"/>
    <attr name="titleRightImg" format="reference"/>
    <attr name="titleAlpha" format="float"/>*/


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private String titleLeftText;
    private Drawable titleLeftImg;
    private String titleRightText;
    private Drawable titleRightImg;
    private String titleName;
    private int titleGravity;
    private int titleBgAlpha;
    private RelativeLayout mTitleView;
    private FrameLayout mLeftFLayout;
    private ImageView mLeftImg;
    private TextView mLeftTv;
    private FrameLayout mRightFLayout;
    private ImageView mRightImg;
    private TextView mRightTv;
    private TextView mMiddleTv;
    private OnCustomTileListener mTileListener;//自定义接口，实现监听处理
    private Context mContext;
    private long lastClickTime;


    public HeaderView(Context context) {
        super(context);
        this.mContext = context;
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        //加载视图布局
        LayoutInflater.from(context).inflate(R.layout.layout_titleview, this, true);

        //加载自定义属性
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
            titleLeftText = typedArray.getString(R.styleable.HeaderView_titleLeftText);
            titleLeftImg = typedArray.getDrawable(R.styleable.HeaderView_titleLeftImg);
            titleRightText = typedArray.getString(R.styleable.HeaderView_titleRightText);
            titleRightImg = typedArray.getDrawable(R.styleable.HeaderView_titleRightImg);
            titleName = typedArray.getString(R.styleable.HeaderView_titleName);
            titleBgAlpha = typedArray.getInteger(R.styleable.HeaderView_titleAlpha, -1);
            titleGravity = typedArray.getInt(R.styleable.HeaderView_titleGravity, 0);
        } finally {
            typedArray.recycle();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //获取子控件
        mTitleView = (RelativeLayout) findViewById(R.id.titlebar);
        mLeftFLayout = (FrameLayout) findViewById(R.id.flleft);
        mLeftTv = (TextView) findViewById(R.id.tvleft);
        mLeftImg = (ImageView) findViewById(R.id.ivleft);
        mRightFLayout = (FrameLayout) findViewById(R.id.flright);
        mRightTv = (TextView) findViewById(R.id.tvright);
        mRightImg = (ImageView) findViewById(R.id.ivright);
        mMiddleTv = (TextView) findViewById(R.id.tvmiddle);

        //设置点击事件
        mLeftFLayout.setOnClickListener(this);
        mRightFLayout.setOnClickListener(this);

        //将从资源文件中加载的属性设置给子控件
        setTitleLeftText(titleLeftText);
        setTitleLeftImg(titleLeftImg);
        setTitleRightText(titleRightText);
        setTitleRightImg(titleRightImg);
        setTitleName(titleName);
        setTitleBgAlpha(titleBgAlpha);
        setTitleGravity(titleGravity);
    }

    public void setTitleLeftText(String titleLeftText) {
        if (null == titleLeftText || titleLeftText.isEmpty())
            return;
        mLeftTv.setText(titleLeftText);
        mLeftTv.setVisibility(VISIBLE);
    }

    public void setTitleLeftImg(Drawable titleLeftImg) {
        if (null == titleLeftImg)
            return;
        mLeftImg.setImageDrawable(titleLeftImg);
        mLeftImg.setVisibility(VISIBLE);
    }

    public void setTitleRightText(String titleRightText) {
        if (null == titleRightText || titleRightText.isEmpty())
            return;
        mRightTv.setText(titleRightText);
        mRightTv.setVisibility(VISIBLE);
    }

    public void setTitleRightImg(Drawable titleRightImg) {
        if (null == titleRightImg)
            return;
        mRightImg.setImageDrawable(titleRightImg);
        mRightImg.setVisibility(VISIBLE);
    }

    public void setTitleName(String titleName) {
        if (null == titleName || titleName.isEmpty())
            return;
        mMiddleTv.setText(titleName);
    }

    public float getTitleBgAlpha() {
        return mTitleView.getAlpha();
    }

    public void setTitleBgAlpha(int titleBgAlpha) {

        if (titleBgAlpha >= 255) {
            mTitleView.getBackground().mutate().setAlpha(255);
        } else if (titleBgAlpha >= 0) {
            mTitleView.getBackground().mutate().setAlpha(titleBgAlpha);
        } else {
            mTitleView.getBackground().mutate().setAlpha(255);
        }
    }

    public void setTitleTextViewAlpha(int titleLeftAlpha) {

        if (titleLeftAlpha >= 255) {
            mMiddleTv.setTextColor(Color.argb(255, 255, 255, 255));   //文字透明度
        } else if (titleLeftAlpha >= 0) {
            mMiddleTv.setTextColor(Color.argb(titleLeftAlpha, 255, 255, 255));   //文字透明度
        } else {
            mMiddleTv.setTextColor(Color.argb(255, 255, 255, 255));   //文字透明度
        }
    }

    @Override
    public void onClick(View v) {
        if (null == mTileListener)
            return;
        switch (v.getId()) {
            case R.id.flleft://标题左边
                mTileListener.setTitleLeftClick();
                break;
            case R.id.flright://标题右边
                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    mTileListener.setTitleRightClick();
                }
                break;
        }
    }

    public void setTitleGravity(int titleGravity) {
        switch (titleGravity) {
            case 0:
                mMiddleTv.setGravity(Gravity.CENTER);
                break;
            case 1:
                mMiddleTv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                MarginLayoutParams lp = (MarginLayoutParams) mMiddleTv.getLayoutParams();
                lp.leftMargin = 150;
                mMiddleTv.setLayoutParams(lp);
                break;
        }
    }

    public void setTitleClickListener(OnCustomTileListener tileListener) {
        mTileListener = tileListener;
    }

    /*
     * 自定义接口 实现控件监听
     */
    public interface OnCustomTileListener {
        void setTitleLeftClick();

        void setTitleRightClick();
    }
}
