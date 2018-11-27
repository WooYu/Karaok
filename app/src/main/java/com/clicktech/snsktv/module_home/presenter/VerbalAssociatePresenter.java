package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SearchBySongNameResponse;
import com.clicktech.snsktv.entity.SearchResponse;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_home.contract.VerbalAssociateContract;
import com.clicktech.snsktv.module_home.ui.activity.ClassifyListActivity;
import com.clicktech.snsktv.module_home.ui.activity.LearnSingActivity;
import com.clicktech.snsktv.module_home.ui.adapter.AssociateAdapter;
import com.clicktech.snsktv.module_home.ui.adapter.ClassifyAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Administrator on 2017/1/11.
 */

@ActivityScope
public class VerbalAssociatePresenter extends BasePresenter<VerbalAssociateContract.Model, VerbalAssociateContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> mResultList = new ArrayList<>();
    private AssociateAdapter mSingerAdapter;
    private ClassifyAdapter mSongAdapter;

    private int mCurPage;
    private int mPagesize;

    @Inject
    public VerbalAssociatePresenter(VerbalAssociateContract.Model model, VerbalAssociateContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        mPagesize = mApplication.getResources().getInteger(R.integer.paging_size);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

    }

    //初始化歌手的Adapter
    private void initSingerAdapter() {
        mSingerAdapter = new AssociateAdapter(mResultList);
        mSingerAdapter.setOnItemClickListener(new DefaultAdapter.
                OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Intent intent = new Intent(mApplication, ClassifyListActivity.class);
                intent.putExtra("singerinfo", convertData(data));
                intent.putExtra("title", StringHelper.getLau_With_J_U_C(
                        data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()));
                intent.putExtra("incometype", 0);
                UiUtils.startActivity(intent);
            }
        });
        mRootView.setSingerAdapter(mSingerAdapter);
    }

    //初始化歌曲的Adapter
    private void initSongAdapter() {
        mSongAdapter = new ClassifyAdapter(mResultList);
        mSongAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SongInfoBean>() {
            @Override
            public void onItemClick(View view, SongInfoBean data, int position) {
                Intent intent = new Intent(mApplication, LearnSingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("songid", data.getSong_id());
                bundle.putParcelable("song", data);
                intent.putExtras(bundle);
                UiUtils.startActivity(intent);
            }
        });
        mRootView.setSongAdapter(mSongAdapter);

    }

    //请求数据依据歌曲名
    public void showSongResult(String key) {
        initSongAdapter();
        mModel.searchBySongResult(RequestParams_Maker.
                getSongListBySongName("", key, mCurPage, mPagesize))
                .compose(RxUtils.<SearchBySongNameResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SearchBySongNameResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SearchBySongNameResponse singInfo) {
                        mResultList = singInfo.getSongList();
                        mSongAdapter.setmInfos(mResultList);
                        mRootView.showEmptyLayout(mResultList.isEmpty());
                    }
                });
    }

    //请求数据依据歌手名
    public void showSingerResult(String key) {
        initSingerAdapter();
        mModel.searchBySingerResult(RequestParams_Maker.
                getSongListBySingerName("", key, mCurPage, mPagesize))
                .compose(RxUtils.<SearchBySongNameResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SearchBySongNameResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SearchBySongNameResponse singInfo) {
                        mResultList = singInfo.getSongList();
                        mSingerAdapter.setmInfos(mResultList);
                        mRootView.showEmptyLayout(mResultList.isEmpty());
                    }

                });

    }

    //展示模糊搜索的结果(暂时没有使用)
    public void showFuzzySearchResult(String key) {
        mModel.searchResult(RequestParams_Maker.getQuicklySearch(key, mCurPage, mPagesize))
                .compose(RxUtils.<SearchResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SearchResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SearchResponse response) {

                    }

                });
    }

    //将Songinfobean转换成SingerInfoEntity
    private SingerInfoEntity convertData(SongInfoBean bean) {
        SingerInfoEntity singerInfoEntity = new SingerInfoEntity();
        singerInfoEntity.setSinger_id(bean.getSinger_id());
        singerInfoEntity.setSinger_name_jp(bean.getSinger_name_jp());
        singerInfoEntity.setSinger_name_cn(bean.getSinger_name_cn());
        singerInfoEntity.setSinger_name_us(bean.getSinger_name_us());
        singerInfoEntity.setSinger_name_jp_ping(bean.getSinger_name_jp_ping());
        singerInfoEntity.setAdd_time(bean.getAdd_time());
        singerInfoEntity.setFirst_letter(bean.getFirst_letter());
        singerInfoEntity.setSinger_photo(bean.getSinger_photo());
        return singerInfoEntity;
    }

}