package com.clicktech.snsktv.module_enter.presenter;

import android.app.Application;
import android.os.Message;

import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.UserInfoResponse;
import com.clicktech.snsktv.module_enter.contract.ShowUserInfoContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

import org.simple.eventbus.EventBus;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
 * Created by Administrator on 2017/5/11.
 */

@ActivityScope
public class ShowUserInfoPresenter extends BasePresenter<ShowUserInfoContract.Model, ShowUserInfoContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public ShowUserInfoPresenter(ShowUserInfoContract.Model model, ShowUserInfoContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 登录操作
     */
    public void getRegistUser(Map<String, RequestBody> info, MultipartBody.Part part) {

        mModel.getRegistUser(info, part)
                .compose(RxUtils.<UserInfoResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<UserInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(UserInfoResponse userInfo) {

                        JPushInterface.setAlias(mApplication, userInfo.getToken(), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                if (i == 0) {
                                    Timber.d("设置别名成功");
                                } else {
                                    Timber.e("设置别名失败");
                                }
                            }
                        });

                        EventBus.getDefault().post(new Message(), EventBusTag.LOGIN_SUCCESS);
                        DataHelper.setStringSF(mApplication, ConstantConfig.TOKEN, userInfo.getToken());
                        ((KtvApplication) mApplication).setUserInfoBean(userInfo.getThirdUser());
                        mRootView.turn2Main(userInfo.getThirdUser());
                    }

                });
    }
}
