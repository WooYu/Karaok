package com.clicktech.snsktv.widget.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.clicktech.snsktv.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 评论弹框
 * Created by only on 2017/6/27.
 */

public class CommentPopupWindow extends PopupWindow {
    private String TAG = "CommentPopupWindow";
    private Context context;
    private CommentListener mListener;

    private View contentView;
    private EditText popupCommentEdt;
    private TextView popupCommentSendTv;

    public CommentPopupWindow(Context context, CommentListener listener) {
        super(context);
        this.context = context;
        this.mListener = listener;
        foundPopup();
    }

    private void foundPopup() {
        contentView = View.inflate(context, R.layout.popwindow_coment, null);
        popupCommentEdt = (EditText) contentView.findViewById(R.id.input);
        popupCommentSendTv = (TextView) contentView.findViewById(R.id.tv_detemine);

        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        setFocusable(true);
        //设置允许在外点击消失
        setOutsideTouchable(true);
        //这个是为了点击“Back”也能使其消失，不会影响背景
        setBackgroundDrawable(new BitmapDrawable());
        //显示在键盘上方
//        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        contentView.setFocusable(true);

        popupCommentSendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = popupCommentEdt.getText().toString().trim();
                if (result.length() <= 0) {
                    Toast.makeText(context, context.getString(R.string.songdetail_inputcomment_hint), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (null != mListener) {
                    mListener.sendComment(result);
                }
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                popupCommentEdt.setText("");
            }
        });

        popupCommentEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                if (arg1 == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
    }

    /**
     * 外部调用显示
     */
    public void showReveal() {
        if (null == contentView) {
            return;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                showSoftInputWindow();
            }
        }, 200);

        //显示并设置位置
        showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

    }

    private void showSoftInputWindow() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(popupCommentEdt, InputMethodManager.SHOW_FORCED);
    }
}
