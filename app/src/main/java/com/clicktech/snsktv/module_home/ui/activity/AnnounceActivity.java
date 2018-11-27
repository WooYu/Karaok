package com.clicktech.snsktv.module_home.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageLoader;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.EventBusTag;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.PublishInforEntity;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.clicktech.snsktv.module_discover.ui.activity.OtherAlbumActivity;
import com.clicktech.snsktv.module_enter.ui.activity.MainActivity;
import com.clicktech.snsktv.module_home.contract.AnnounceContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerAnnounceComponent;
import com.clicktech.snsktv.module_home.inject.module.AnnounceModule;
import com.clicktech.snsktv.module_home.presenter.AnnouncePresenter;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.BlurTransformation;
import com.clicktech.snsktv.widget.dialog.DownloadDialog;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.imagepicker.GlideImageLoader;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

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
 * Created by Administrator on 2017/4/18.
 * 发布界面
 */

public class AnnounceActivity extends WEActivity<AnnouncePresenter> implements AnnounceContract.View
        , HeaderView.OnCustomTileListener {

    public static final int REQUEST_CODE_SELECTALBUM = 0x0009;
    private static final int REQUEST_CODE_SELECT = 100;
    @BindView(R.id.headerview)
    HeaderView mHeaderView;
    @BindView(R.id.iv_songpicture)
    ImageView ivSongpicture;
    @BindView(R.id.rl_change)
    RelativeLayout rlChange;
    @BindView(R.id.iv_blurrysongpicture)
    ImageView ivBlurrysongpicture;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.tv_wordsnum)
    TextView tvWordsnum;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.switch_public)
    Switch switchPublic;
    @BindView(R.id.rl_special)
    RelativeLayout rlSpecial;
    @BindView(R.id.et_songname)
    EditText etSongName;

    private PublishInforEntity mPublishInfo;//待发布的信息
    private ScoreResultEntity mScoreResult;//打分结果
    private ArrayList<ImageItem> images = null;//接受选择的歌曲图片
    private NetworkDialog mNetworkDialog;
    private DownloadDialog mDownloadDialog;//下载进度框
    private String mAlbumID;//专辑ID

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAnnounceComponent
                .builder()
                .appComponent(appComponent)
                .announceModule(new AnnounceModule(this)) //请将AnnounceModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_announce, null, false);
    }

    @Override
    protected void initData() {
        //创建对话框
        mNetworkDialog = new NetworkDialog(this);
        mHeaderView.setTitleClickListener(this);
        initImagePicker();

        Bundle bundle = getIntent().getExtras();
        if (null == bundle)
            return;

        mPublishInfo = bundle.getParcelable("publishinfo");
        mScoreResult = bundle.getParcelable("scoreresult");
        if (null == mPublishInfo)
            return;

        //清唱需要显示歌曲名字编辑
        if (mPublishInfo.getWorks_type() == getResources().getInteger(R.integer.workstype_cantata)) {
            etSongName.setVisibility(View.VISIBLE);
        }
        mPresenter.setScoreResult(mScoreResult);
        etComment.addTextChangedListener(new CommentInputListener());
        loadSongPicture();
    }

    @Override
    public void showLoading() {
        if (null != mNetworkDialog) {
            mNetworkDialog.showNetWorkDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (null != mNetworkDialog) {
            mNetworkDialog.dismissNetWorkDialog();
        }
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
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        dismissNumberDialog();
        super.onDestroy();
    }

    @Override
    public void setTitleLeftClick() {
        mPresenter.showTipDialog(this, getString(R.string.announce_worksnorelease));
    }

    @Override
    public void setTitleRightClick() {//发布
        onUploadClicked();
    }

    @OnClick({R.id.rl_change, R.id.rl_special})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_change://更好头像
                clickChangeSongPicture();
                break;
            case R.id.rl_special://专辑
                onSpecialClicked();
                break;
        }
    }

    //点击专辑
    private void onSpecialClicked() {
        Intent albumIntent = new Intent(this, OtherAlbumActivity.class);
        albumIntent.putExtra("userid", mWeApplication.getUserID());
        albumIntent.putExtra("need", true);
        startActivityForResult(albumIntent, REQUEST_CODE_SELECTALBUM);
    }

    /**
     * 初始化imagepicker
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(400);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(400);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(500);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(500);                         //保存文件的高度。单位像素
    }

    /**
     * 初始化背景
     */
    private void loadSongPicture() {
        if (null == mPublishInfo)
            return;

        //专辑图片
        ImageLoader imageLoader = mWeApplication.getAppComponent().imageLoader();
        imageLoader.loadImage(mWeApplication, GlideImageConfig
                .builder()
                .errorPic(R.mipmap.def_square)
                .placeholder(R.mipmap.def_square)
                .url(mPublishInfo.getWorks_photo())
                .imageView(ivSongpicture)
                .build());

        //背景图片
        Glide.with(mApplication)
                .load(StringHelper.getImageUrl(mPublishInfo.getWorks_photo()))
                .asBitmap()
                .transform(new BlurTransformation(this, 8f))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (null != ivBlurrysongpicture) {
                            ivBlurrysongpicture.setImageBitmap(resource);
                        }
                    }
                });
    }

    /**
     * 点击切换歌曲图片
     */
    private void clickChangeSongPicture() {
        String[] options = new String[]{mApplication.getString(R.string.announce_camera),
                mApplication.getString(R.string.announce_photoalbum)};
        final ActionSheetDialog dialog = new ActionSheetDialog(this,
                options, null);
        dialog.title(getString(R.string.dialog_album_title))//
                .layoutAnimation(null)//
                .cancelText(mApplication.getString(R.string.dialog_cancel))
                .show();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {//拍照
                    //打开选择,本次允许选择的数量
                    Intent intent = new Intent(AnnounceActivity.this, ImageGridActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                    startActivityForResult(intent, REQUEST_CODE_SELECT);
                } else {//相册
                    //打开选择,本次允许选择的数量
                    Intent intent1 = new Intent(AnnounceActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent1, REQUEST_CODE_SELECT);
                }
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == REQUEST_CODE_SELECT) {
            if (null == data) {
                return;
            }
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (EmptyUtils.isNotEmpty(images)) {
                updateSongPicture(images.get(0).path);
            }
        }


        if (requestCode == REQUEST_CODE_SELECTALBUM && resultCode == Activity.RESULT_OK) {
            mAlbumID = data.getStringExtra("albumid");
        }

    }

    //更换图片
    private void updateSongPicture(String imagepath) {
        mPublishInfo.setPhoto(imagepath);
        ImagePicker.getInstance().getImageLoader().displayImage(this, imagepath, ivSongpicture, 0, 0);
        Glide.with(mApplication)
                .load(imagepath)
                .asBitmap()
                .transform(new BlurTransformation(this, 8f))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (null != ivBlurrysongpicture) {
                            ivBlurrysongpicture.setImageBitmap(resource);
                        }
                    }
                });
    }

    //点击开始上传
    private void onUploadClicked() {
        if (null == mPublishInfo)
            return;

        //清唱需要设置歌曲名字
        if (mPublishInfo.getWorks_type() == getResources().getInteger(R.integer.workstype_cantata)) {
            String songName = etSongName.getText().toString().trim();
            if (EmptyUtils.isEmpty(songName)) {
                showMessage(getString(R.string.tip_cantata_songname));
                return;
            }

            mPublishInfo.setWorks_name(songName);
        }

        String commentStr = etComment.getText().toString().trim();
        if (EmptyUtils.isEmpty(commentStr)) {
            commentStr = getString(R.string.announce_leaveaword_hint);
        }

        mPublishInfo.setWorks_desc(commentStr);
        mPublishInfo.setIs_publish(switchPublic.isChecked() ?
                getResources().getInteger(R.integer.ispublish_secret) : getResources().getInteger(R.integer.ispublish_open));
        mPublishInfo.setAlbum_id(EmptyUtils.isEmpty(mAlbumID) ? "0" : mAlbumID);

        mPresenter.upload_amazonServers(mPublishInfo);
    }

    @Override
    public void turn2MainActivity() {
        EventBus.getDefault().post(new Message(), EventBusTag.ANNOUNCESUCCESS);

        Intent mainIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("announcefinish", true);
        mainIntent.putExtras(bundle);
        launchActivity(mainIntent);
        killMyself();
    }

    @Override
    public void showNumberDialog() {
        if (null == mDownloadDialog) {
            mDownloadDialog = new DownloadDialog();
            mDownloadDialog.show(getFragmentManager(), "com.clicktech.snsktv.widget.dialog.DownloadDialog");
        }
    }

    @Override
    public void dismissNumberDialog() {
        if (null != mDownloadDialog) {
            mDownloadDialog.dismiss();
            mDownloadDialog = null;
        }
    }

    @Override
    public void updateNumberDialog(int progress) {
        if (null != mDownloadDialog && progress <= 100) {
            mDownloadDialog.setTvProgress(progress);
        }
    }

    // 评论字数变化监听
    class CommentInputListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String comment = etComment.getText().toString().trim();
            tvWordsnum.setText(comment.length() + "/" + 140);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
