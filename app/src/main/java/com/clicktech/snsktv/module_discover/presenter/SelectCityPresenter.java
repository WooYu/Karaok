package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.CityEntity;
import com.clicktech.snsktv.module_discover.contract.SelectCityContract;
import com.clicktech.snsktv.module_discover.ui.adapter.SelectCityAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@ActivityScope
public class SelectCityPresenter extends BasePresenter<SelectCityContract.Model,
        SelectCityContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private SelectCityAdapter mCityAdapter;
    private List<CityEntity> mCityList;

    @Inject
    public SelectCityPresenter(SelectCityContract.Model model, SelectCityContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mCityAdapter = new SelectCityAdapter(mCityList);
        mCityAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<CityEntity>() {
            @Override
            public void onItemClick(View view, CityEntity data, int position) {
                mRootView.returnSelectCit(data.getName());
            }
        });
        mRootView.setRecyclerView(mCityAdapter);
        getCityList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    private void getCityList() {
        String language = KtvApplication.ktvApplication.getLocaleCode();
        if (language.equals(mApplication.getString(R.string.language_japan))) {
            getJapanCities();
        } else {
            getUnJapanCities();
        }
    }

    public void getJapanCities() {
        mCityList = mModel.getJapanCities();
        mCityAdapter.setmInfos(mCityList);
    }

    public void getUnJapanCities() {
        mCityList = mModel.getUnJapanCities();
        mCityAdapter.setmInfos(mCityList);
    }

    public void clearSearch() {
        mCityAdapter.setmInfos(mCityList);
    }

    public void searchCity(String key) {
        if (EmptyUtils.isEmpty(key)) {
            return;
        }

        List<CityEntity> templist = new ArrayList<>();
        for (CityEntity cityEntity : mCityList) {
            if (cityEntity.getName().contains(key)) {
                templist.add(cityEntity);
            }
        }
        mCityAdapter.setmInfos(templist);
    }
}
