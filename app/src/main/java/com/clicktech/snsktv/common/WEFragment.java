package com.clicktech.snsktv.common;

import com.clicktech.snsktv.arms.base.BaseFragment;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.squareup.leakcanary.RefWatcher;

public abstract class WEFragment<P extends BasePresenter> extends BaseFragment<P> {
    protected KtvApplication mWeApplication;

    @Override
    protected void ComponentInject() {
        mWeApplication = (KtvApplication) mActivity.getApplication();
        setupFragmentComponent(mWeApplication.getAppComponent());
    }

    //提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
    protected abstract void setupFragmentComponent(AppComponent appComponent);

    @Override
    public void onDestroy() {
        super.onDestroy();
        //使用leakCanary检测fragment的内存泄漏
        RefWatcher watcher = KtvApplication.getRefWatcher(getActivity());
        if (watcher != null) {
            watcher.watch(this);
        }
        this.mWeApplication = null;
    }
}
