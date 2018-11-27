package com.clicktech.snsktv.module_enter.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_enter.contract.ShowUserInfoContract;
import com.clicktech.snsktv.module_enter.inject.component.DaggerShowUserInfoComponent;
import com.clicktech.snsktv.module_enter.inject.module.ShowUserInfoModule;
import com.clicktech.snsktv.module_enter.presenter.ShowUserInfoPresenter;
import com.clicktech.snsktv.util.sharesdklogin.ThirdUserInfo;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

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
 * Created by Administrator on 2017/5/11.
 * 注册信息确认页面
 */

public class ShowUserInfoActivity extends WEActivity<ShowUserInfoPresenter> implements
        ShowUserInfoContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.avatar)
    MLImageView avatar;
    @BindView(R.id.album)
    RelativeLayout album;
    @BindView(R.id.tv_nickname)
    TextView nickName;
    @BindView(R.id.tv_birthday)
    TextView birthday;
    @BindView(R.id.tv_address)
    TextView address;
    @BindView(R.id.tv_language)
    TextView language;
    @BindView(R.id.tv_accountnumber)
    TextView ID;
    @BindView(R.id.m_sex)
    TextView sex;
    @BindView(R.id.textView3)
    TextView loginMode;

    private ThirdUserInfo mThirdUserInfo;
    private int languagetype = -1;//语言类型
    private NetworkDialog mNetDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerShowUserInfoComponent
                .builder()
                .appComponent(appComponent)
                .showUserInfoModule(new ShowUserInfoModule(this)) //请将ShowUserInfoModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_showuseinfo, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);
        mNetDialog = new NetworkDialog(this);

        mThirdUserInfo = (ThirdUserInfo) getIntent().getSerializableExtra("thirdinfo");
        if (null == mThirdUserInfo) {
            return;
        }

        setUserInfo();
        String mLanguage = mWeApplication.getLocaleCode();
        if (mLanguage.equals(getString(R.string.language_china))) {
            languagetype = getResources().getInteger(R.integer.languagecode_cn);
            language.setText(R.string.language_cn);
        } else if (mLanguage.equals(getString(R.string.language_english))) {
            languagetype = getResources().getInteger(R.integer.languagecode_en);
            language.setText(R.string.language_en);
        } else if (mLanguage.equals(getString(R.string.language_japan))) {
            languagetype = getResources().getInteger(R.integer.languagecode_jp);
            language.setText(R.string.language_jp);
        }
    }

    //设置用户信息
    private void setUserInfo() {
        if (Patterns.WEB_URL.matcher(mThirdUserInfo.getUsericon()).matches()) {
            mWeApplication.getAppComponent().imageLoader().loadImage(mWeApplication, GlideImageConfig
                    .builder()
                    .errorPic(R.mipmap.def_avatar_round)
                    .placeholder(R.mipmap.def_avatar_round)
                    .url(mThirdUserInfo.getUsericon())
                    .imageView(avatar)
                    .build());
        } else {
            //不符合标准
            avatar.setImageBitmap(BitmapFactory.decodeFile(mThirdUserInfo.getUsericon()));
        }

        nickName.setText(mThirdUserInfo.getNickname());
        birthday.setText(mThirdUserInfo.getBirthday());
        address.setText(mThirdUserInfo.getArea());
        if ("1".equals(mThirdUserInfo.getGender())) {
            sex.setText(R.string.setting_datum_sex);
        } else {
            sex.setText(R.string.setting_datum_sex_female);
        }
        loginMode.setText(mThirdUserInfo.getLoginmode());
    }

    @Override
    public void showLoading() {
        if (null != mNetDialog) {
            mNetDialog.showNetWorkDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (null != mNetDialog) {
            mNetDialog.dismissNetWorkDialog();
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
    public void turn2Main(UserInfoBean infoEntity) {
        Intent intent = new Intent(mWeApplication, MainActivity.class);
        intent.putExtra("islogin", true);
        launchActivity(intent);
        killMyself();
    }

    @OnClick({R.id.btn_ok, R.id.tv_language})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                submitData();
                break;
            case R.id.tv_language:
                chooseLanguage();
                break;
        }
    }

    private void chooseLanguage() {
        View chooseLanguageView = LayoutInflater.from(this).inflate(R.layout.layout_chooselanguage, null);
        TextView zh = (TextView) chooseLanguageView.findViewById(R.id.tv_language_zh);
        TextView en = (TextView) chooseLanguageView.findViewById(R.id.tv_language_en);
        TextView jp = (TextView) chooseLanguageView.findViewById(R.id.tv_language_jp);

        final AlertDialog chooseLanguageDialog = new AlertDialog.Builder(this).create();
        chooseLanguageDialog.setView(chooseLanguageView);
        chooseLanguageDialog.setTitle("");
        chooseLanguageDialog.show();
        zh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.setText(R.string.language_cn);
                languagetype = getResources().getInteger(R.integer.languagecode_cn);
                chooseLanguageDialog.dismiss();
            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.setText(R.string.language_en);
                languagetype = getResources().getInteger(R.integer.languagecode_en);
                chooseLanguageDialog.dismiss();
            }
        });

        jp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.setText(R.string.language_jp);
                languagetype = getResources().getInteger(R.integer.languagecode_jp);
                chooseLanguageDialog.dismiss();
            }
        });
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
    }

    //确认提交
    private void submitData() {
        if (null == mThirdUserInfo) {
            return;
        }

        if (-1 == languagetype) {
            showMessage(getString(R.string.error_not_languagenvir));
            return;
        }

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), mThirdUserInfo.getUsericon());
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", "file.jpg", imageBody);
        mPresenter.getRegistUser(RequestParams_Maker.
                getRegistUserRequest(mThirdUserInfo.getThirdtype(), mThirdUserInfo.getUserid(),
                        mThirdUserInfo.getNickname(), mThirdUserInfo.getGender(),
                        mThirdUserInfo.getBirthday(), mThirdUserInfo.getArea(), languagetype), part);
    }
}
