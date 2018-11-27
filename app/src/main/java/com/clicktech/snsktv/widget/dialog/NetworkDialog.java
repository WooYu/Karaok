package com.clicktech.snsktv.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.clicktech.snsktv.R;


/**
 * Created by Administrator on 2017-06-24.
 */

public class NetworkDialog extends Dialog {

    private Context mContext;
    private Dialog mDialog;

    public NetworkDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NetworkDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected NetworkDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    public void showNetWorkDialog() {
        if (null == mDialog) {
            mDialog = new Dialog(mContext, R.style.NetworkDialog);
            mDialog.setContentView(R.layout.dialog_materialish);
            mDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mDialog = null;
                }
            });
//            //设置点击外部或者放返回键是否取消
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    public void dismissNetWorkDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
