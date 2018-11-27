package com.clicktech.snsktv.module_discover.presenter;

import android.app.Application;
import android.os.Message;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.base.AppManager;
import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.arms.mvp.BasePresenter;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.PermissionUtil;
import com.clicktech.snsktv.arms.utils.RxUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.common.net.CommonService;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.common.net.okgo.JsonCallback;
import com.clicktech.snsktv.entity.SingerAlbumEntity;
import com.clicktech.snsktv.entity.SingerAlbumResponse;
import com.clicktech.snsktv.module_discover.contract.SingerAlbumContract;
import com.clicktech.snsktv.module_discover.ui.adapter.PhotoAlbumAdapter;
import com.clicktech.snsktv.module_discover.ui.holder.PhotoAlbumHolder;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.clicktech.snsktv.widget.imagepicker.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.simple.eventbus.EventBus;

import java.io.File;
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
 * Created by Administrator on 2017/1/30.
 */

@ActivityScope
public class SingerAlbumPresenter extends BasePresenter<SingerAlbumContract.Model, SingerAlbumContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private List<SingerAlbumEntity> mAlbumList;
    private PhotoAlbumAdapter mAlbumAdapter;
    private int mNumOfSelection;//当前选择的张数

    @Inject
    public SingerAlbumPresenter(SingerAlbumContract.Model model, SingerAlbumContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;

        mAlbumList = new ArrayList<>();
        mAlbumList.add(new SingerAlbumEntity());
        mAlbumAdapter = new PhotoAlbumAdapter(mAlbumList, new PhotoAlbumHolder.PhotoAlbumListener() {
            @Override
            public void itemClick(int position) {
                if (!mRootView.getIsEditing()) {
                    return;
                }

                SingerAlbumEntity entity = mAlbumList.get(position);
                entity.setSelect(!entity.isSelect());
                mAlbumAdapter.notifyItemChanged(position);

                if (entity.isSelect()) {
                    ++mNumOfSelection;
                } else {
                    --mNumOfSelection;
                }
                updatePhotoNum();
            }

            @Override
            public void addAlbum() {
                if (mRootView.getIsEditing()) {
                    return;
                }

                requestPermission();
            }
        });
        mRootView.setRecyclerAdapter(mAlbumAdapter);

    }

    //获取相册列表
    public void requestAlbumList(final int curpage) {
        String userid = ((KtvApplication) mApplication).getUserID();
        int pageSize = mApplication.getResources().getInteger(R.integer.paging_size);

        mModel.getSingerAlbum(userid, curpage, pageSize)
                .compose(RxUtils.<SingerAlbumResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<SingerAlbumResponse>(mErrorHandler) {
                    @Override
                    public void onNext(SingerAlbumResponse entity) {
                        if (EmptyUtils.isEmpty(entity.getWorksPhotosList())) {
                            return;
                        }

                        if (curpage == mApplication.getResources().getInteger(R.integer.paging_startindex)) {
                            mAlbumList = entity.getWorksPhotosList();
                            mAlbumList.add(0, new SingerAlbumEntity());
                        } else {
                            mAlbumList.addAll(entity.getWorksPhotosList());
                        }
                        mAlbumAdapter.setmInfos(mAlbumList);
                    }

                });
    }

    //请求照相权限
    public void requestPermission() {
        PermissionUtil.recordmv(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                mRootView.showPopOfSelectPhoto();
            }

            @Override
            public void onRequestPermissionRefuse() {
                mRootView.showMessage(mApplication.getString(R.string.permiss_changephoto));
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

    }

    //请求发布图片
    public void requestPublishPicture(String path) {
        String auth = RequestParams_Maker.getAuthString();
        String sign = RequestParams_Maker.getSignString(auth);
        final KtvApplication app = (KtvApplication) mApplication;
        String userid = app.getUserID();
        String info = "{\"userId\": " + userid + "}";

        PostRequest<BaseResponse> postRequest = OkGo.<BaseResponse>post(BuildConfig.APP_DOMAIN + CommonService.INSERTWORKSPHOTOSINFO)
                .tag(this)
                .params("auth", auth)
                .params("sign", sign)
                .params("info", info);

        if (EmptyUtils.isNotEmpty(path)) {
            postRequest.params("photo", new File(path));
        }

        postRequest.execute(new JsonCallback<BaseResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse.getErrCode() == 0) {
                    EventBus.getDefault().post(new Message(), EventBusTag.PHOTOALBUMCHANGE);
                    mRootView.showMessage(app.getString(R.string.tip_photo_uploadsuccess));
                    requestAlbumList(app.getResources().getInteger(R.integer.paging_startindex));
                } else {
                    UiUtils.SnackbarText(baseResponse.getMsg());
                }
            }
        });
    }

    //开始编辑
    public void startEditing() {
        mNumOfSelection = 0;
        if (EmptyUtils.isEmpty(mAlbumList)) {
            return;
        }

        mAlbumList.remove(0);
        mAlbumAdapter.notifyItemRemoved(0);
    }

    //结束编辑
    public void endEditing() {
        mNumOfSelection = 0;
        if (null == mAlbumList) {
            return;
        }

        mAlbumList.add(0, new SingerAlbumEntity());
        for (int i = 0; i < mAlbumList.size(); i++) {
            SingerAlbumEntity entity = mAlbumList.get(i);
            entity.setSelect(false);
            mAlbumList.set(i, entity);
        }

        mAlbumAdapter.setmInfos(mAlbumList);
    }

    //更新标题
    private void updatePhotoNum() {
        if (mNumOfSelection < 0) {
            mNumOfSelection = 0;
        }

        mRootView.updateTitle(String.format(mApplication.getString(
                R.string.format_numofimagesselected), mNumOfSelection));
    }

    //删除选中的图片
    public void deleteTheSelectedImage() {
        if (EmptyUtils.isEmpty(mAlbumList)) {
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mAlbumList.size(); i++) {
            SingerAlbumEntity entity = mAlbumList.get(i);
            if (entity.isSelect()) {
                stringBuffer.append(entity.getId());
                stringBuffer.append(",");
            }
        }

        int index = stringBuffer.lastIndexOf(",");
        stringBuffer.deleteCharAt(index);
        String photoids = stringBuffer.toString();
        String userid = ((KtvApplication) mApplication).getUserID();
        mModel.getDeleteAlbum(RequestParams_Maker.getDeleteAlbum(userid, photoids))
                .compose(RxUtils.<BaseResponse>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse entity) {
                        mRootView.showMessage(mApplication.getString(R.string.tip_photo_deletsuccess));
                        for (int i = 0; i < mAlbumList.size(); i++) {
                            SingerAlbumEntity albumEntity = mAlbumList.get(i);
                            if (albumEntity.isSelect()) {
                                mAlbumList.remove(i);
                                --i;
                            }
                        }

                        mRootView.deleteAlbumSuccess();
                    }

                });
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
        imagePicker.setFocusWidth(500);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(500);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);                         //保存文件的高度。单位像素
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        OkGo.getInstance().cancelAll();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}