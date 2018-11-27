package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.entity.SoundHistoryEntity;
import com.clicktech.snsktv.entity.SoundHistoryListResponse;
import com.clicktech.snsktv.module_mine.contract.Mine_MyHistorySound_FRContract;
import com.clicktech.snsktv.module_mine.ui.adapter.AccompanimentAdapter;
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
 * Created by Administrator on 2017/3/24.
 */

@ActivityScope
public class Mine_MyHistorySound_FRPresenter extends BasePresenter<Mine_MyHistorySound_FRContract.Model, Mine_MyHistorySound_FRContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SoundHistoryEntity> hotSingers = new ArrayList<>();
    private AccompanimentAdapter mRichAdapter;

    @Inject
    public Mine_MyHistorySound_FRPresenter(Mine_MyHistorySound_FRContract.Model model, Mine_MyHistorySound_FRContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mRichAdapter = new AccompanimentAdapter(hotSingers, mApplication);
        mRootView.setRichListRecyclerView(mRichAdapter, hotSingers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;

        this.hotSingers = null;
        this.mRichAdapter = null;
    }

    public void requestData_history(Map<String, String> info, final int index) {
        mModel.getMine_MySoundHistoryList(info)
                .compose(RxUtils.<SoundHistoryListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SoundHistoryListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SoundHistoryListResponse singInfo) {

                        if (index == 1) {
                            hotSingers.clear();
                        }

                        if (singInfo.getList() != null) {
                            hotSingers.addAll(singInfo.getList());
                        }

                        mRichAdapter.notifyDataSetChanged();
                    }

                });
    }

}
