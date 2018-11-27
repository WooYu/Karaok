package com.clicktech.snsktv.module_discover.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.clicktech.snsktv.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/25.
 */

public class SelectNationFragment extends DialogFragment {
    Unbinder unbinder;
    private SwitchNationCallBack mCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去掉留白的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_select_nation, container);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置dialog宽度
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_cancel)
    public void onTvCancelClicked() {
        dismiss();
    }

    @OnClick(R.id.tv_japan)
    public void onTvJapanClicked() {
        if (null != mCallBack) {
            mCallBack.switch2Japan();
        }
        dismiss();
    }

    @OnClick(R.id.tv_china)
    public void onTvChinaClicked() {
        if (null != mCallBack) {
            mCallBack.switch2UnJapan();
        }
        dismiss();
    }

    public void setmCallBack(SwitchNationCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface SwitchNationCallBack {
        void switch2Japan();

        void switch2UnJapan();
    }
}
