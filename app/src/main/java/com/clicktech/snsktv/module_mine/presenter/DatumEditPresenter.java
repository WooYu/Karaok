package com.clicktech.snsktv.module_mine.presenter;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.MineUserInfoResponse;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_mine.contract.DatumEditContract;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.widget.imagepicker.GlideImageLoader;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import okhttp3.MultipartBody;


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
public class DatumEditPresenter extends BasePresenter<DatumEditContract.Model, DatumEditContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private String[] mLanguageArr;
    private int[] mLanguageCodeArr;
    private String[] mLanguageAbbrArr;

    @Inject
    public DatumEditPresenter(DatumEditContract.Model model, DatumEditContract.View rootView
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

    public void requestPermission(final Context context) {
        PermissionUtil.recordmv(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                showPopOfChangeAvatar(context);
            }

            @Override
            public void onRequestPermissionRefuse() {
                mRootView.showMessage(mApplication.getString(R.string.permiss_changephoto));
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

    }

    //初始化ImagePicker
    public void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    //展示更换头像的弹窗
    private void showPopOfChangeAvatar(Context context) {
        String[] options = new String[]{mApplication.getString(R.string.announce_camera),
                mApplication.getString(R.string.announce_photoalbum)};
        final ActionSheetDialog dialog = new ActionSheetDialog(context,
                options, null);
        dialog.title(mApplication.getString(R.string.enter_regist_info_upavater))//
                .layoutAnimation(null)//
                .cancelText(mApplication.getString(R.string.dialog_cancel))
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {//拍照
                    mRootView.takeAPictures();
                } else {//相册
                    mRootView.takePhotoAlbum();
                }
                dialog.dismiss();
            }
        });
    }

    //展示语言切换的弹窗
    public void showPopOfChangeLanguage(Context context) {
        mLanguageArr = new String[]{context.getString(R.string.language_jp), context.getString(R.string.language_cn),
                context.getString(R.string.language_en)};
        mLanguageAbbrArr = new String[]{context.getString(R.string.language_japan),
                context.getString(R.string.language_china), context.getString(R.string.language_english)};
        Resources resources = context.getResources();
        mLanguageCodeArr = new int[]{resources.getInteger(R.integer.languagecode_jp),
                resources.getInteger(R.integer.languagecode_cn), resources.getInteger(R.integer.languagecode_en)};

        final ActionSheetDialog dialog = new ActionSheetDialog(context,
                mLanguageArr, null);
        dialog.title(context.getString(R.string.language_title))//
                .layoutAnimation(null)//
                .cancelText(context.getString(R.string.mine_playlist_close))
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRootView.updateLanguageSetting(mLanguageArr[position], mLanguageCodeArr[position]);
                dialog.dismiss();
            }
        });

    }

    //展示改变性别对话框
    public void showPopOfChangeSex(Context context) {
        final String[] options = new String[]{mApplication.getString(R.string.setting_datum_sex_man),
                mApplication.getString(R.string.setting_datum_sex_female)};
        final ActionSheetDialog dialog = new ActionSheetDialog(context,
                options, null);
        dialog.title(mApplication.getString(R.string.setting_datum_sex))//
                .layoutAnimation(null)//
                .cancelText(mApplication.getString(R.string.dialog_cancel))
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRootView.updateSexSetting(options[position]);
                dialog.dismiss();
            }
        });
    }

    //请求修改用户信息
    public void requestModifyInfo(String nickname, int sex, String birthday, String area,
                                  int language, MultipartBody.Part part) {
        String userid = ((KtvApplication) mApplication).getUserID();
        UserInfoBean userInfoBean = KtvApplication.ktvApplication.getUserInfoBean();
        String curlanguage = userInfoBean.getLanguage();
        final boolean changeLanguage = !String.valueOf(language).equals(curlanguage);
        if (changeLanguage) {
            userInfoBean.setLanguage(String.valueOf(language));
            KtvApplication.ktvApplication.setUserInfoBean(userInfoBean);
        }
        mModel.modifyUserInfo(RequestParams_Maker.modifyUserInfo(
                userid, nickname, sex, birthday, area, language), part)
                .compose(RxUtils.<BaseResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        requestGetUserInfo(changeLanguage);
                    }
                });
    }

    //获取用户信息
    public void requestGetUserInfo(final boolean changeLanguage) {
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.getUserInfo(RequestParams_Maker.getMineUserInfo(userid,
                mApplication.getResources().getInteger(R.integer.ispublish_open)))
                .compose(RxUtils.<MineUserInfoResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<MineUserInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MineUserInfoResponse response) {
                        UserInfoBean userInfoBean = ((KtvApplication) mApplication).getUserInfoBean();
                        userInfoBean.setUser_photo(response.getUser().getUser_photo());
                        userInfoBean.setUser_nickname(response.getUser().getUser_nickname());
                        userInfoBean.setUser_sex(response.getUser().getUser_sex());
                        userInfoBean.setUser_birthday(response.getUser().getUser_birthday());
                        userInfoBean.setUser_district_dtl(response.getUser().getUser_district_dtl());
                        userInfoBean.setUser_district_id(response.getUser().getUser_district_id());
                        userInfoBean.setUser_age(response.getUser().getUser_age());

                        KtvApplication.ktvApplication.setUserInfoBean(userInfoBean);

                        EventBus.getDefault().post(new Message(), EventBusTag.CHANGEAVATAR);
                        if (changeLanguage) {
                            mRootView.turn2Main();
                        } else {
                            mRootView.killMyself();
                        }
                    }

                });
    }
}