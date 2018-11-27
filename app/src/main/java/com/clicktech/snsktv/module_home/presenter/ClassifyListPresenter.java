package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.SongListWithCatecoryResponse;
import com.clicktech.snsktv.entity.SongListWithSingerIDResponse;
import com.clicktech.snsktv.module_home.contract.ClassifyListContract;
import com.clicktech.snsktv.module_home.ui.adapter.ClassifyAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.indexsort.JapaneseEnvironComparator;
import com.clicktech.snsktv.util.indexsort.NonJapaneseEnvComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/1/16.
 */

@ActivityScope
public class ClassifyListPresenter extends BasePresenter<ClassifyListContract.Model, ClassifyListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> mClassifyList = new ArrayList<>();
    private ClassifyAdapter mAdapter;

    @Inject
    public ClassifyListPresenter(ClassifyListContract.Model model, ClassifyListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mAdapter = new ClassifyAdapter(mClassifyList);
        mRootView.setRecyclerView(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {

                switch (view.getId()) {
                    case R.id.tv_singerworks_go_to_k_music:
                        mRootView.turn2KSing(data);
                        break;
                    default:
                        mRootView.turn2Learnsing(data);
                        break;

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mAdapter = null;
        this.mClassifyList = null;
    }


    public void requestData_WithSinger(String singerid) {

        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);
        mModel.getSongListWithSingerId(RequestParams_Maker.getSongList_WithSingerId(singerid, mCurPage, mPageSize))
                .compose(RxUtils.<SongListWithSingerIDResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SongListWithSingerIDResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SongListWithSingerIDResponse singInfo) {
                        mClassifyList = singInfo.getSingerSongList();
                        mAdapter.setmInfos(mClassifyList);
                    }

                });
    }

    public void requestData_WithCategory(String categoryid) {
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getSongListWithCategory(RequestParams_Maker.getSongList_WithCategroy(categoryid, mCurPage, mPageSize))
                .compose(RxUtils.<SongListWithCatecoryResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SongListWithCatecoryResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SongListWithCatecoryResponse singInfo) {
                        mClassifyList = singInfo.getSongList();
                        mAdapter.setmInfos(mClassifyList);
                    }

                });
    }

    public void requestData_WithYearsBetween(String beginYear, String endYear) {
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getSongListWithYearsBetween(RequestParams_Maker.
                getSongList_WithYearsBetween(beginYear, endYear, mCurPage, mPageSize))
                .compose(RxUtils.<SongListWithCatecoryResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SongListWithCatecoryResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SongListWithCatecoryResponse singInfo) {
                        mClassifyList = singInfo.getSongList();
                        operationalData();
                    }

                });
    }

    private void operationalData() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        resetRanging();
                        subscriber.onNext("");
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mAdapter.setmInfos(mClassifyList);
                    }
                });
    }

    //重新排序
    private void resetRanging() {
        if (EmptyUtils.isEmpty(mClassifyList)) {
            return;
        }

        ArrayList<SongInfoBean> jpList = new ArrayList<>();
        ArrayList<SongInfoBean> nonJpList = new ArrayList<>();
        ArrayList<SongInfoBean> symbolList = new ArrayList<>();
        for (SongInfoBean songInfoBean : mClassifyList) {
            String sortIndex = StringHelper.getSortIndex(songInfoBean.getSong_name_jp(),
                    songInfoBean.getSong_name_us(), songInfoBean.getSong_name_cn(),
                    "");
            songInfoBean.setFirst_letter(sortIndex);
            if (sortIndex.matches("[A-Z]")) {
                nonJpList.add(songInfoBean);
            } else if (sortIndex.equals(mApplication.getString(R.string.symbol_other))) {
                symbolList.add(songInfoBean);
            } else {
                jpList.add(songInfoBean);
            }
        }

        Collections.sort(nonJpList, new NonJapaneseEnvComparator());
        Collections.sort(jpList, new JapaneseEnvironComparator());
        mClassifyList.clear();
        String language = KtvApplication.ktvApplication.getLocaleCode();
        if (language.equals(mApplication.getString(R.string.language_japan))) {
            mClassifyList.addAll(jpList);
            mClassifyList.addAll(nonJpList);
        } else {
            mClassifyList.addAll(nonJpList);
            mClassifyList.addAll(jpList);
        }
        mClassifyList.addAll(symbolList);

    }
}