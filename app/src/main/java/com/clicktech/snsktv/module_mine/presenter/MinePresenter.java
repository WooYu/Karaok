package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.MineUserInfoResponse;
import com.clicktech.snsktv.entity.SingerAlbumEntity;
import com.clicktech.snsktv.entity.SingerAlbumResponse;
import com.clicktech.snsktv.module_mine.contract.MineContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;

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
 * Created by Administrator on 2017/2/4.
 */

@ActivityScope
public class MinePresenter extends BasePresenter<MineContract.Model, MineContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MinePresenter(MineContract.Model model, MineContract.View rootView
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

    //获取相册列表
    public void requestPhotoAlbum() {
        String userid = ((KtvApplication) mApplication).getUserID();
        int curPage = mApplication.getResources().getInteger(R.integer.paging_startindex);
        int pageSize = mApplication.getResources().getInteger(R.integer.paging_size);
        mModel.getSingerAlbum(RequestParams_Maker.getAlbumList(userid, curPage, pageSize))
                .compose(RxUtils.<SingerAlbumResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingerAlbumResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SingerAlbumResponse response) {
                        List<SingerAlbumEntity> albumsPhotos = response.getWorksPhotosList();
                        if (null == albumsPhotos) {
                            return;
                        }

                        List<String> photoUrls = new ArrayList<>();
                        for (SingerAlbumEntity bean : albumsPhotos) {
                            photoUrls.add(bean.getPhoto());
                        }

                        mRootView.updateAlbumOfUser(photoUrls);
                    }

                });
    }

    //获取用户信息
    public void requestUserInfo() {
        String userid = KtvApplication.ktvApplication.getUserID();
        mModel.getUserInfo(RequestParams_Maker.getMineUserInfo(userid, mApplication.
                getResources().getInteger(R.integer.ispublish_open)))
                .compose(RxUtils.<MineUserInfoResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<MineUserInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MineUserInfoResponse singInfo) {
                        mRootView.updateUserInfoView(singInfo);
                    }

                });
    }

    //请求礼物列表
    public void requestGiftList() {
        String userid = KtvApplication.ktvApplication.getUserID();
        mModel.getGiftList(RequestParams_Maker.getGistList(userid))
                .compose(RxUtils.<GiftListResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<GiftListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GiftListResponse response) {
                        mRootView.setGiftListForChildFragment(response);
                    }

                });
    }

    /**
     * 转换等级到图片
     *
     * @param grade
     */
    public int convertGradeToImage(int grade) {
        if (1 <= grade && grade <= 3) {
            return R.mipmap.grade_a;
        } else if (4 <= grade && grade <= 6) {
            return R.mipmap.grade_b;
        } else if (7 <= grade && grade <= 9) {
            return R.mipmap.grade_c;
        } else if (10 <= grade && grade <= 12) {
            return R.mipmap.grade_d;
        } else if (13 <= grade && grade <= 15) {
            return R.mipmap.grade_e;
        } else if (16 <= grade && grade <= 18) {
            return R.mipmap.grade_f;
        } else if (19 <= grade && grade <= 21) {
            return R.mipmap.grade_g;
        } else {
            return R.mipmap.grade_a;
        }
    }
}