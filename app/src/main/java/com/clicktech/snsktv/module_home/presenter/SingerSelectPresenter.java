package com.clicktech.snsktv.module_home.presenter;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.DefaultAdapter;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SingSingnerTypeBeanEntity;
import com.clicktech.snsktv.entity.SingSingnerTypeResponse;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.entity.SingerListWithCatecoryResponse;
import com.clicktech.snsktv.module_home.contract.SingerSelectContract;
import com.clicktech.snsktv.module_home.ui.activity.ClassifyListActivity;
import com.clicktech.snsktv.module_home.ui.activity.SingerListWithTypeActivity;
import com.clicktech.snsktv.module_home.ui.adapter.SingerCategoryAdapter;
import com.clicktech.snsktv.module_home.ui.adapter.SingerSelectAdapter;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.util.HistoryUtils;
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
 * Created by Administrator on 2017/1/13.
 */

@ActivityScope
public class SingerSelectPresenter extends BasePresenter<SingerSelectContract.Model, SingerSelectContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SingerInfoEntity> mHistoryList = new ArrayList<>();
    private List<SingSingnerTypeBeanEntity> mSingers = new ArrayList<>();
    private SingerSelectAdapter mAdapter;
    private SingerCategoryAdapter mCategoryAdapter;


    @Inject
    public SingerSelectPresenter(SingerSelectContract.Model model, SingerSelectContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        if (HistoryUtils.getNormalSingerHistory(mApplication) != null) {
            mHistoryList = HistoryUtils.getSingerHistoryReverse(mApplication).getSingerList();
        }

        mAdapter = new SingerSelectAdapter(mHistoryList);
        mCategoryAdapter = new SingerCategoryAdapter(mSingers);
        mRootView.setRecyclerView(mAdapter, mCategoryAdapter);

        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SingerInfoEntity>() {
            @Override
            public void onItemClick(View view, SingerInfoEntity data, int position) {
                Intent intent = new Intent(view.getContext(), ClassifyListActivity.class);
                intent.putExtra("singerinfo", data);
                intent.putExtra("incometype", 0);
                intent.putExtra("title", StringHelper.getLau_With_J_U_C(
                        data.getSinger_name_jp(), data.getSinger_name_us(), data.getSinger_name_cn()));
                UiUtils.startActivity(intent);
            }
        });

        mCategoryAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SingSingnerTypeBeanEntity>() {
            @Override
            public void onItemClick(View view, SingSingnerTypeBeanEntity data, int position) {
                Intent intent = new Intent(view.getContext(), SingerListWithTypeActivity.class);
                intent.putExtra("categoryid", data.getCategory_Type());
                intent.putExtra("categoryname", data.getCategory_name());
                UiUtils.startActivity(intent);
            }
        });
    }

    //获取已点歌手
    public void requestHistoryOfSinger() {
        SingerListWithCatecoryResponse response = HistoryUtils.getSingerHistoryReverse(mApplication);
        if (null == response) {
            return;
        }

        mHistoryList = response.getSingerList();
        mAdapter.setmInfos(mHistoryList);
    }

    //获取歌手分类列表
    public void requestSingerType() {

        mModel.getSingerList(RequestParams_Maker.getSingerCategory())
                .compose(RxUtils.<SingSingnerTypeResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingSingnerTypeResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SingSingnerTypeResponse singInfo) {
                        mSingers = singInfo.getSingerCategoryList();
                        mCategoryAdapter.setmInfos(mSingers);
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

}