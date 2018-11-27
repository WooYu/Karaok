package com.clicktech.snsktv.module_home.ui.fragment;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;

import org.simple.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wy201 on 2018-03-09.
 */

public class CommentFragment extends DialogFragment {
    @BindView(R.id.et_content)
    EditText etContent;

    Unbinder unbinder;
    private CommentInputCallBack mCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //去掉留白的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_comment, container);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);//注册到事件主线
        initArguments();
        forceOpenSoftKeyboard(getActivity());
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
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv_sure)
    void onTvSureClicked() {
        String content = etContent.getText().toString().trim();
        dismiss();
        if (EmptyUtils.isEmpty(content)) {
            UiUtils.SnackbarText(getString(R.string.error_nocomment));
            return;
        }

        if (null != mCallBack) {
            mCallBack.sendComment(content);
        }

    }

    private void initArguments() {
        Bundle bundle = getArguments();
    }

    public void forceOpenSoftKeyboard(final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etContent, 0);
            }
        }, 100);
    }

    public void setCommentInputCallBack(CommentInputCallBack callBack) {
        mCallBack = callBack;
    }

    public interface CommentInputCallBack {
        void sendComment(String content);
    }
}
