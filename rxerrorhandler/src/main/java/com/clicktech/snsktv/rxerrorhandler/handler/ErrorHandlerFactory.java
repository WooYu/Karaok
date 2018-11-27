package com.clicktech.snsktv.rxerrorhandler.handler;

import android.content.Context;

import com.clicktech.snsktv.rxerrorhandler.handler.listener.ResponseErroListener;

/**
 * 工厂模式创建错误处理的类
 */
public class ErrorHandlerFactory {
    public final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ResponseErroListener mResponseErroListener;

    public ErrorHandlerFactory(Context mContext, ResponseErroListener mResponseErroListener) {
        this.mResponseErroListener = mResponseErroListener;
        this.mContext = mContext;
    }

    /**
     * 处理错误
     *
     * @param throwable
     */
    public void handleError(Throwable throwable) {
        mResponseErroListener.handleResponseError(mContext, (Exception) throwable);
    }
}
