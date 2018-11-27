package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.entity.SingerListWithCatecoryResponse;
import com.clicktech.snsktv.module_home.contract.SingerListWithTypeContract;
import com.clicktech.snsktv.module_home.ui.activity.ClassifyListActivity;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.indexsort.JapanEnvirSingerComparator;
import com.clicktech.snsktv.util.indexsort.NonJapanEnvirSingerComparator;
import com.clicktech.snsktv.widget.quicksearch.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
 * Created by Administrator on 2017/3/13.
 */

@ActivityScope
public class SingerListWithTypePresenter extends BasePresenter<SingerListWithTypeContract.Model, SingerListWithTypeContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private SortAdapter mAdapter;
    private List<SingerInfoEntity> mList;
    private HashMap<String, Integer> mIndexHashMap;//侧边栏索引集合

    @Inject
    public SingerListWithTypePresenter(SingerListWithTypeContract.Model model, SingerListWithTypeContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mAdapter = new SortAdapter(mList);
        mRootView.setRecyclerView(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SingerInfoEntity>() {
            @Override
            public void onItemClick(View view, SingerInfoEntity data, int position) {
                Intent intent = new Intent(mApplication, ClassifyListActivity.class);
                intent.putExtra("singerinfo", data);
                intent.putExtra("incometype", 0);
                intent.putExtra("title", StringHelper.getLau_With_J_U_C(
                        data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()));
                UiUtils.startActivity(intent);
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
    }

    public void requestData(String categoryId) {

        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getSingerList(RequestParams_Maker.getSingerList_WithType(categoryId, mCurPage, mPageSize))
                .compose(RxUtils.<SingerListWithCatecoryResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingerListWithCatecoryResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SingerListWithCatecoryResponse response) {
                        mList = response.getSingerList();
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
                        mAdapter.setmInfos(mList);
                    }
                });
    }

    //重新排序
    private void resetRanging() {
        if (EmptyUtils.isEmpty(mList)) {
            return;
        }

        ArrayList<SingerInfoEntity> jpList = new ArrayList<>();
        ArrayList<SingerInfoEntity> nonJpList = new ArrayList<>();
        ArrayList<SingerInfoEntity> symbolList = new ArrayList<>();
        for (SingerInfoEntity singerInfo : mList) {
            String sortIndex = StringHelper.getSortIndex(singerInfo.getSinger_name_jp(),
                    singerInfo.getSinger_name_us(), singerInfo.getSinger_name_cn(),
                    singerInfo.getSinger_name_jp_ping());
            singerInfo.setFirst_letter(sortIndex);
            if (sortIndex.matches("[A-Z]")) {
                nonJpList.add(singerInfo);
            } else if (sortIndex.equals(mApplication.getString(R.string.symbol_other))) {
                symbolList.add(singerInfo);
            } else {
                jpList.add(singerInfo);
            }
        }

        Collections.sort(nonJpList, new NonJapanEnvirSingerComparator());
        Collections.sort(jpList, new JapanEnvirSingerComparator());
        mList.clear();
        String language = KtvApplication.ktvApplication.getLocaleCode();
        if (language.equals(mApplication.getString(R.string.language_japan))) {
            mList.addAll(jpList);
            mList.addAll(nonJpList);
        } else {
            mList.addAll(nonJpList);
            mList.addAll(jpList);
        }
        mList.addAll(symbolList);

        mIndexHashMap = new HashMap<>();
        ArrayList<String> indexs = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            SingerInfoEntity songInfoBean = mList.get(i);
            String letter = songInfoBean.getFirst_letter();
            if (!indexs.contains(letter)) {
                indexs.add(letter);
                mIndexHashMap.put(letter, i);
            }
        }
    }

    //获取侧边栏选中字母在集合中的位置
    public int getPositionOfSelectLetter(String letter) {
        if (EmptyUtils.isEmpty(letter) || EmptyUtils.isEmpty(mIndexHashMap)) {
            return -1;
        }

        return EmptyUtils.isEmpty(mIndexHashMap.get(letter)) ? -1 : mIndexHashMap.get(letter);
    }


}
