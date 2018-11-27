package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.entity.ListOfWorksResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_home.contract.PopularListContract;
import com.clicktech.snsktv.module_home.ui.adapter.PopularListAdapter;
import com.clicktech.snsktv.module_home.ui.holder.PopularListHolder;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/1/19.
 */

@ActivityScope
public class PopularListPresenter extends BasePresenter<PopularListContract.Model, PopularListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> mPopularList = new ArrayList<>();
    private PopularListAdapter mPopularAdapter;

    @Inject
    public PopularListPresenter(PopularListContract.Model model, PopularListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mPopularAdapter = new PopularListAdapter(mPopularList, new PopularListHolder.onItemClickListener() {
            @Override
            public void itemClick(int position) {
                SongInfoBean songInfoBean = mPopularList.get(position);
                Intent intent = new Intent(mApplication, SongDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("songinfo", songInfoBean);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);
            }
        });
        mRootView.setPopularRecyclerView(mPopularAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mPopularList = null;
        this.mPopularAdapter = null;
    }

    public void requestData(Map<String, String> info, final int index) {

        mModel.getSingerList(info)
                .compose(RxUtils.<ListOfWorksResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ListOfWorksResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ListOfWorksResponse singInfo) {

                        if (singInfo == null)
                            return;

                        if (singInfo.getSongWorksList().size() == 0) {
                            mRootView.canLoadmore(false, index);
                        } else {
                            mRootView.canLoadmore(true, index);
                        }

                        if (index == 1) {
                            mPopularList.clear();
                        }

                        if (singInfo.getSongWorksList() != null) {
                            mPopularList.addAll(singInfo.getSongWorksList());
                        }

                        mPopularAdapter.notifyDataSetChanged();

                    }

                });
    }
}