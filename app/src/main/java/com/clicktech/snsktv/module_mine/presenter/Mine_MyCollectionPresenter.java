package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.MineCollectionResponse;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_discover.ui.activity.SingerIntroActivity;
import com.clicktech.snsktv.module_discover.ui.activity.SongDetailsActivity;
import com.clicktech.snsktv.module_mine.contract.Mine_MyCollectionContract;
import com.clicktech.snsktv.module_mine.ui.activity.Mine_MyCollectionActivity;
import com.clicktech.snsktv.module_mine.ui.adapter.SwipCollectionListAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * Created by Administrator on 2017/3/24.
 */

@ActivityScope
public class Mine_MyCollectionPresenter extends BasePresenter<Mine_MyCollectionContract.Model, Mine_MyCollectionContract.View> {
    public SwipCollectionListAdapter.OnItemClickListener onItemClickListener;
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SongInfoBean> hotSingers = new ArrayList<>();
    private SwipCollectionListAdapter swipAdapter;

    @Inject
    public Mine_MyCollectionPresenter(Mine_MyCollectionContract.Model model, Mine_MyCollectionContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        swipAdapter = new SwipCollectionListAdapter(hotSingers, mApplication);

        OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();// 关闭被点击的菜单。
                cancelCollect(adapterPosition);
            }
        };

        swipAdapter.setOnItemClickListener(new SwipCollectionListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {
                SongInfoBean songInfoBean = hotSingers.get(position);
                int store_type = Integer.parseInt(songInfoBean.getStore_type());
                if (store_type == mApplication.getResources().getInteger(R.integer.collectworkstype_sing)) {
                    Intent intent = new Intent(mApplication, SongDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("songinfo", songInfoBean);
                    intent.putExtras(bundle);
                    UiUtils.startActivity(intent);
                } else if (store_type == mApplication.getResources().getInteger(R.integer.collectworkstype_album)) {
                    Intent intent = new Intent(mApplication, SingerIntroActivity.class);
                    intent.putExtra("userid", songInfoBean.getUser_id());
                    UiUtils.startActivity(intent);
                }
            }
        });

        mRootView.setSwipRecyclerview(swipAdapter, hotSingers, menuItemClickListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        this.hotSingers = null;
    }

    public void requestData() {
        int mPageSize = mApplication.getResources().getInteger(R.integer.paging_size);
        int mCurPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        String userid = KtvApplication.ktvApplication.getUserID();

        mModel.getMine_MyCollectionList(RequestParams_Maker.getCollectList(userid, mCurPage, mPageSize))
                .compose(RxUtils.<MineCollectionResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<MineCollectionResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MineCollectionResponse response) {
                        mRootView.showNoCollection(EmptyUtils.isEmpty(response.getMyStore()));

                        hotSingers = response.getMyStore();
                        swipAdapter.updateData(hotSingers);
                    }

                });
    }

    //取消收藏
    public void cancelCollect(final int position) {
        String userid = ((KtvApplication) mApplication).getUserID();
        int type = Integer.parseInt(hotSingers.get(position).getStore_type());

        mModel.cancelWorksStore(RequestParams_Maker.getAddAndCancleCollection(
                userid, hotSingers.get(position).getId(), type))
                .compose(RxUtils.<ResponseAttentonCollectResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<ResponseAttentonCollectResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ResponseAttentonCollectResponse response) {

                        hotSingers.remove(position);
                        swipAdapter.notifyItemRemoved(position);
                        mRootView.showMessage(mApplication.getString(R.string.tip_collect_cancel));
                    }
                });
    }

}
