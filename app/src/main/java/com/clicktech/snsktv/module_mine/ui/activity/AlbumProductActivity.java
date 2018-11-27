package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.module_mine.contract.AlbumProductContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerAlbumProductComponent;
import com.clicktech.snsktv.module_mine.inject.module.AlbumProductModule;
import com.clicktech.snsktv.module_mine.presenter.AlbumProductPresenter;
import com.clicktech.snsktv.module_mine.ui.adapter.AlbumProductActivityAdapter;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
 * Created by Administrator on 2017/5/19.
 * 制作专辑
 */

public class AlbumProductActivity extends WEActivity<AlbumProductPresenter> implements
        AlbumProductContract.View, HeaderView.OnCustomTileListener {


    private final int RequestCode_SelectPicture = 0x0010;
    private final int RequestCode_SelectSong = 0x0009;
    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.rl_left)
    RelativeLayout rlLeft;
    @BindView(R.id.im_works_add)
    ImageView imWorksAdd;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_album)
    EditText albumName;
    @BindView(R.id.tv_albumname_limit)
    TextView limit;
    @BindView(R.id.edt_detail)
    EditText detail;
    @BindView(R.id.tv_detail_limit)
    TextView detailLimit;
    @BindView(R.id.im_imagview)
    ImageView avatar;
    private List<SongInfoBean> dataList;
    private AlbumProductActivityAdapter adapter;
    private String worksIds;//拼接选择的作品id
    private String mPathOfAlbum;//专辑图片地址
    private NetworkDialog mNetWorkDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAlbumProductComponent
                .builder()
                .appComponent(appComponent)
                .albumProductModule(new AlbumProductModule(this)) //请将AlbumProductModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_albumproduct, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        headerview.setTitleName(getString(R.string.album_make));
        mNetWorkDialog = new NetworkDialog(this);
        initRecyclerview();
        initEditext();
    }

    private void initEditext() {
        albumName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String textNumber = albumName.getText().toString();
                limit.setText(textNumber.length() + "/20");
            }
        });


        detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String textNumber = detail.getText().toString();
                detailLimit.setText(textNumber.length() + "/140");
            }
        });
    }

    private void initRecyclerview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mWeApplication));
        dataList = new ArrayList<>();
        adapter = new AlbumProductActivityAdapter(dataList);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        if (null != mNetWorkDialog) {
            mNetWorkDialog.showNetWorkDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (null != mNetWorkDialog) {
            mNetWorkDialog.dismissNetWorkDialog();
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
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
        onReleaseClicked();
    }

    @OnClick({R.id.im_works_add, R.id.im_imagview})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_works_add:
                Intent songIntent = new Intent(this, SingleMusicForAlbumActivity.class);
                startActivityForResult(songIntent, RequestCode_SelectSong);
                break;
            case R.id.im_imagview:
                Intent imageIntent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(imageIntent, RequestCode_SelectPicture);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择歌曲
        if (resultCode == RESULT_OK && requestCode == RequestCode_SelectSong) {
            Bundle bundle = data.getExtras();
            dataList = bundle.getParcelableArrayList("songlist");
            adapter.setmInfos(dataList);

            if (EmptyUtils.isEmpty(dataList)) {
                return;
            }

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < dataList.size(); i++) {
                if (i == dataList.size() - 1) {
                    stringBuffer.append(dataList.get(i).getId());
                } else {
                    stringBuffer.append(dataList.get(i).getId() + ",");
                }
            }

            worksIds = stringBuffer.toString();
        }

        //更换专辑图片
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == RequestCode_SelectPicture) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (EmptyUtils.isEmpty(images)) {
                return;
            }

            mPathOfAlbum = images.get(0).path;
            Glide.with(this)                             //配置上下文
                    .load(Uri.fromFile(new File(mPathOfAlbum)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.mipmap.def_square_large)           //设置错误图片
                    .placeholder(R.mipmap.def_square_large)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(avatar);

        }

    }

    public void onReleaseClicked() {

        String nameOfAlbum = albumName.getText().toString().trim();
        String descOfAlbum = detail.getText().toString().trim();
        if (TextUtils.isEmpty(nameOfAlbum)) {
            showMessage(getString(R.string.error_input_albumname));
            return;
        }

        if (TextUtils.isEmpty(descOfAlbum)) {
            showMessage(getString(R.string.error_input_albumdesc));
            return;
        }

        if (EmptyUtils.isEmpty(mPathOfAlbum)) {
            showMessage(getString(R.string.error_select_albumpicture));
            return;
        }

        if (TextUtils.isEmpty(mPathOfAlbum)) {
            showMessage(getString(R.string.error_select_albumpicture));
            return;
        }

        if (EmptyUtils.isEmpty(worksIds)) {
            showMessage(getString(R.string.error_singlelimit));
            return;
        }

        MultipartBody.Part part = null;
        File file = new File(mPathOfAlbum);
        if (file.exists()) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            part = MultipartBody.Part.createFormData("photo", "file.jpg", imageBody);
        }

        mPresenter.releasedAlbums(RequestParams_Maker.getCreateAlbum(
                mWeApplication.getUserID(), worksIds, nameOfAlbum, descOfAlbum), part);

    }

    @Override
    public void releasSucces() {
        showMessage(mApplication.getString(R.string.tip_success_albumcreate));
        setResult(RESULT_OK, getIntent());
        killMyself();
    }
}
