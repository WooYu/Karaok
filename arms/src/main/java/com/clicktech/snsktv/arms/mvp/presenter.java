package com.clicktech.snsktv.arms.mvp;

import rx.Subscription;

public interface presenter {
    void onStart();

    void onDestroy();

    void unSubscribe(Subscription subscription);
}
