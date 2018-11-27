package com.clicktech.snsktv.rxerrorhandler.handler;

import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;

import rx.Subscriber;

/**
 * 订阅者：错误产生时，回调onError()
 *
 * @param <T>
 */
public abstract class ErrorHandleSubscriber<T> extends Subscriber<T> {
    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler) {
        this.mHandlerFactory = rxErrorHandler.getmHandlerFactory();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }

}

