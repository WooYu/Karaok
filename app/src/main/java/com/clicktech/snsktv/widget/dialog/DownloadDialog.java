package com.clicktech.snsktv.widget.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;

/**
 * Created by Administrator on 2017-06-24.
 */

public class DownloadDialog extends DialogFragment {

    private TextView tvProgress;
    private TextView tvTip;

    private DialogDismissListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_download, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != mListener) {
                        mListener.onDismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        tvProgress = (TextView) rootView.findViewById(R.id.progress);
        tvTip = (TextView) rootView.findViewById(R.id.tipcontent);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    public void setTvProgress(int progress) {
        tvProgress.setText(progress + "%");
    }

    //设置提示
    public void setTvTip(String tip) {
        if (EmptyUtils.isNotEmpty(tip))
            tvTip.setText(tip);
        else
            tvTip.setText(getString(R.string.dialog_pleasewait));
    }

    public void setDialogDismissListener(DialogDismissListener listener) {
        this.mListener = listener;
    }

    public interface DialogDismissListener {
        void onDismiss();
    }

}
