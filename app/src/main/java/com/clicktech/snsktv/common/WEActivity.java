package com.clicktech.snsktv.common;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.BaseActivity;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.common.inject.component.AppComponent;

import java.util.Locale;

public abstract class WEActivity<P extends BasePresenter> extends BaseActivity<P> {
    protected KtvApplication mWeApplication;

    @Override
    protected void ComponentInject() {
        mWeApplication = (KtvApplication) getApplication();
        setupActivityComponent(mWeApplication.getAppComponent());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    //提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
    protected abstract void setupActivityComponent(AppComponent appComponent);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mWeApplication = null;
    }

    /**
     * 替代findviewById方法
     */
    public <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

    //切换语言
    protected void switchLanguage() {
        String language = KtvApplication.ktvApplication.getLocaleCode();
        //获取当前系统语言
        Locale locale = getResources().getConfiguration().locale;
        String curlanguage = locale.getLanguage();
        if (EmptyUtils.isEmpty(language) || curlanguage.equals(language)) {
            return;
        }

        // 设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals(getString(R.string.language_japan))) {
            config.locale = Locale.JAPANESE;
        } else if (language.equals(getString(R.string.language_china))) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals(getString(R.string.language_english))) {
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
    }

}
