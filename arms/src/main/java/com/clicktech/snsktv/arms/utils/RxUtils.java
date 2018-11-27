package com.clicktech.snsktv.arms.utils;

import com.clicktech.snsktv.arms.base.BaseActivity;
import com.clicktech.snsktv.arms.base.BaseFragment;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.mvp.BaseView;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxUtils {

    public static <T extends BaseResponse> Observable.Transformer<T, T> applySchedulers(final BaseView view) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {//显示进度条
                                view.showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                view.hideLoading();//隐藏进度条
                            }
                        })
                        .filter(new Func1<T, Boolean>() {
                            @Override
                            public Boolean call(T t) {//错误的处理可以通过拦截器实现
                                if (EmptyUtils.isEmpty(t.getMsg())) {
                                    UiUtils.SnackbarText("server return data is null");
                                    return false;
                                }
                                if (t.getErrCode() != 0) {
                                    UiUtils.SnackbarText(t.getMsg());
                                    return false;
                                }
                                return true;
                            }
                        })
                        .compose(RxUtils.<T>bindToLifecycle(view));
            }
        };
    }


    public static <T> LifecycleTransformer<T> bindToLifecycle(BaseView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).bindToLifecycle();
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }

    }

}
