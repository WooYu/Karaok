package com.clicktech.snsktv.rxerrorhandler.handler.listener;

import android.content.Context;

public interface ResponseErroListener {
    ResponseErroListener EMPTY = new ResponseErroListener() {
        @Override
        public void handleResponseError(Context context, Exception e) {

        }
    };

    void handleResponseError(Context context, Exception e);
}
