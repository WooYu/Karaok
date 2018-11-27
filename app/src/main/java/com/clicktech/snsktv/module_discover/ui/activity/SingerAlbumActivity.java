package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_discover.contract.SingerAlbumContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerSingerAlbumComponent;
import com.clicktech.snsktv.module_discover.inject.module.SingerAlbumModule;
import com.clicktech.snsktv.module_discover.presenter.SingerAlbumPresenter;
import com.clicktech.snsktv.module_discover.ui.adapter.PhotoAlbumAdapter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.clicktech.snsktv.arms.utils.Preconditions.checkNotNull;

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
 * 我的相册
 */

public class SingerAlbumActivity extends WEActivity<SingerAlbumPresenter> implements
        SingerAlbumContract.View, HeaderView.OnCustomTileListener {

    private static final int REQUEST_CODE_ALBUM = 0x0007;
    public boolean isEditAlbum;//是否编辑相册
    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    private RxPermissions mRxPermissions;
    private int mIndexOfStartPage;//开始页面索引
    private int mCurrentPage;//当前请求页码

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerSingerAlbumComponent
                .builder()
                .appComponent(appComponent)
                .singerAlbumModule(new SingerAlbumModule(this)) //请将SingerAlbumModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_singeralbum, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mIndexOfStartPage = getResources().getInteger(R.integer.paging_startindex);
        mPresenter.initImagePicker();
        mPresenter.requestAlbumList(mIndexOfStartPage);

        RxView.clicks(ivDelete)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.deleteTheSelectedImage();
                    }
                });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        if (isEditAlbum) {
            isEditAlbum = false;
            headerView.setTitleRightText(getString(R.string.singerintro_edit));
            headerView.setTitleName(getString(R.string.singerintro_album));
            ivDelete.setVisibility(View.GONE);
            mPresenter.endEditing();
        } else {
            isEditAlbum = true;
            headerView.setTitleRightText(getString(R.string.dialog_cancel));
            headerView.setTitleName(String.format(getString(R.string.format_numofimagesselected), 0));
            ivDelete.setVisibility(View.VISIBLE);
            mPresenter.startEditing();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_ALBUM) {
                List<ImageItem> mAvatarList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (null != mAvatarList) {
                    mPresenter.requestPublishPicture(mAvatarList.get(0).path);
                }
            }
        }
    }

    @Override
    public void setRecyclerAdapter(PhotoAlbumAdapter adapter) {
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public boolean getIsEditing() {
        return isEditAlbum;
    }

    @Override
    public void updateTitle(String title) {
        headerView.setTitleName(title);
    }

    @Override
    public void showPopOfSelectPhoto() {
        String[] options = new String[]{mApplication.getString(R.string.announce_camera),
                mApplication.getString(R.string.announce_photoalbum)};
        final ActionSheetDialog dialog = new ActionSheetDialog(this,
                options, null);
        dialog.title(getString(R.string.dialog_album_title))//
                .layoutAnimation(null)//
                .cancelText(mApplication.getString(R.string.mine_playlist_close))
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {//拍照
                    takeAPictures();
                } else {//相册
                    takePhotoAlbum();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void deleteAlbumSuccess() {
        setTitleRightClick();
        EventBus.getDefault().post(new Message(), EventBusTag.PHOTOALBUMCHANGE);
    }

    private void takeAPictures() {
        Intent intent = new Intent(mWeApplication, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    private void takePhotoAlbum() {
        Intent intent = new Intent(mWeApplication, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

}
