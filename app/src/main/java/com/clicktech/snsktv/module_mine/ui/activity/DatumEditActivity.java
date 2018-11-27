package com.clicktech.snsktv.module_mine.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bumptech.glide.Glide;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.arms.widget.imageloader.glide.GlideImageConfig;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.clicktech.snsktv.module_enter.ui.activity.MainActivity;
import com.clicktech.snsktv.module_mine.contract.DatumEditContract;
import com.clicktech.snsktv.module_mine.inject.component.DaggerDatumEditComponent;
import com.clicktech.snsktv.module_mine.inject.module.DatumEditModule;
import com.clicktech.snsktv.module_mine.presenter.DatumEditPresenter;
import com.clicktech.snsktv.util.StringHelper;
import com.clicktech.snsktv.util.transformations.CircleWithBorderTransformation;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * Created by Administrator on 2017/2/4.
 * 编辑资料
 */

public class DatumEditActivity extends WEActivity<DatumEditPresenter> implements
        DatumEditContract.View, HeaderView.OnCustomTileListener {
    private static final int REQUEST_CODE_SELECT = 0x0001;
    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.iv_avatar)
    MLImageView ivAvatar;
    @BindView(R.id.ll_avatar)
    LinearLayout llAvatar;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_loginmethod)
    TextView tvLoginmethod;
    @BindView(R.id.tv_accountnumber)
    TextView tvAccountnumber;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.btn_ok)
    TextView btnOk;

    ArrayList<ImageItem> mAvatarList;
    private RxPermissions mRxPermissions;
    private int mSexCode;
    private int mLanguageCode = -1;
    private TimePickerView mSelectorOfBirthday;
    private OptionsPickerView mSelectorOfAddr;
    private ArrayList<String> mCountryList;
    private ArrayList<List<String>> mProvinceList;

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
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerDatumEditComponent
                .builder()
                .appComponent(appComponent)
                .datumEditModule(new DatumEditModule(this)) //请将DatumEditModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_datumedit, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        setInterface();
        mPresenter.initImagePicker();
        initSelectorOfBirthday();
        initSelectorOfAddress();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void takeAPictures() {
        Intent intent = new Intent(mWeApplication, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void takePhotoAlbum() {
        Intent intent = new Intent(mWeApplication, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void updateSexSetting(String sex) {
        if (sex.equals(getString(R.string.setting_datum_sex_man))) {
            mSexCode = mWeApplication.getResources().getInteger(R.integer.sex_man);
        } else {
            mSexCode = mWeApplication.getResources().getInteger(R.integer.sex_female);
        }
        tvSex.setText(sex);
    }

    @Override
    public void updateLanguageSetting(String language, int languagecode) {
        tvLanguage.setText(language);
        mLanguageCode = languagecode;
    }

    @Override
    public void turn2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchActivity(intent);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {

    }

    @OnClick(R.id.ll_avatar)
    public void onLlAvatarClicked() {
        mPresenter.requestPermission(this);
    }

    @OnClick(R.id.tv_sex)
    public void onTvSexClicked() {
        mPresenter.showPopOfChangeSex(this);
    }

    @OnClick(R.id.tv_birthday)
    public void onTvBirthdayClicked() {
        mSelectorOfBirthday.show();
    }

    @OnClick(R.id.tv_address)
    public void onTvAddressClicked() {
        mSelectorOfAddr.show();
    }

    @OnClick(R.id.tv_language)
    public void onTvLanguageClicked() {
        mPresenter.showPopOfChangeLanguage(this);
    }

    @OnClick(R.id.btn_ok)
    public void onBtnOkClicked() {
        doChangeInformation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == REQUEST_CODE_SELECT) {
            if (null == data) {
                return;
            }
            mAvatarList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (EmptyUtils.isEmpty(mAvatarList)) {
                return;
            }

            String path = mAvatarList.get(0).path;
            Glide.with(this)
                    .load(path)
                    .placeholder(R.mipmap.def_avatar_round)
                    .error(R.mipmap.def_avatar_round)
                    .into(ivAvatar);
        }
    }

    //初始化界面
    private void setInterface() {
        UserInfoBean userInfoBean = mWeApplication.getUserInfoBean();
        if (null == userInfoBean) {
            return;
        }

        //头像
        mWeApplication.getAppComponent().imageLoader().loadImage(this, GlideImageConfig.builder()
                .url(userInfoBean.getUser_photo())
                .transformation(new CircleWithBorderTransformation(this))
                .errorPic(R.mipmap.def_avatar_round)
                .imageView(ivAvatar)
                .build());
        //昵称
        etNickname.setText(userInfoBean.getUser_nickname());
        //性别
        mSexCode = userInfoBean.getUser_sex();
        if (mSexCode == getResources().getInteger(R.integer.sex_female)) {
            tvSex.setText(R.string.setting_datum_sex_female);
        } else {
            tvSex.setText(R.string.setting_datum_sex_man);
        }
        //生日
        tvBirthday.setText(userInfoBean.getUser_birthday());
        //地区
        tvAddress.setText(userInfoBean.getUser_district_dtl());
        //登录方式
        tvLoginmethod.setText(StringHelper.getLoginMode(userInfoBean));
        //id
        tvAccountnumber.setText(userInfoBean.getUser_no());
        //语言
        String mLanguage = mWeApplication.getLocaleCode();
        if (mLanguage.equals(getString(R.string.language_china))) {
            tvLanguage.setText(R.string.language_cn);
            mLanguageCode = getResources().getInteger(R.integer.languagecode_cn);
        } else if (mLanguage.equals(getString(R.string.language_english))) {
            tvLanguage.setText(R.string.language_en);
            mLanguageCode = getResources().getInteger(R.integer.languagecode_en);
        } else {
            tvLanguage.setText(R.string.language_jp);
            mLanguageCode = getResources().getInteger(R.integer.languagecode_jp);
        }
    }


    //初始化生日选择器
    private void initSelectorOfBirthday() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        int year = selectedDate.get(Calendar.YEAR); // 获取当前年份
        int birthLimit = getResources().getInteger(R.integer.birth_limits);
        startDate.set(year - birthLimit, 1, 1);
        //时间选择器 ，自定义布局
        mSelectorOfBirthday = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                tvBirthday.setText(format.format(date));
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
                /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setLayoutRes(R.layout.layout_chooser_birthday, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        TextView cancle = (TextView) v.findViewById(R.id.tv_cancle);
                        TextView determine = (TextView) v.findViewById(R.id.tv_determine);
                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSelectorOfBirthday.dismiss();
                            }
                        });
                        determine.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSelectorOfBirthday.returnData();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(getResources().getColor(R.color.div_color))
//                .setContentSize(getResources().getDimensionPixelSize(R.dimen.address_select))//设置滚轮文字大小
//                .setTextColorOut(getResources().getColor(R.color.div_color_other))
                .build();
    }

    //初始化地址选择器
    private void initSelectorOfAddress() {
        mCountryList = new ArrayList<>();
        mProvinceList = new ArrayList<>();

        mCountryList.add(getString(R.string.enter_nation_cn));
        mCountryList.add(getString(R.string.enter_nation_jp));

        List<String> province1 = new ArrayList<>();
        String[] str = getResources().getStringArray(R.array.province);
        for (int i = 0; i < str.length; i++) {
            province1.add(str[i]);
        }
        List<String> province2 = new ArrayList<>();
        String[] str2 = getResources().getStringArray(R.array.japanprovince);
        for (int i = 0; i < str2.length; i++) {
            province2.add(str2[i]);
        }
        mProvinceList.add(province1);
        mProvinceList.add(province2);

        mSelectorOfAddr = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = mCountryList.get(options1) + mProvinceList.get(options1).get(options2);
                tvAddress.setText(tx);
            }
        })
                .setLayoutRes(R.layout.layout_chooser_address, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView cancle = (TextView) v.findViewById(R.id.tv_cancle);
                        TextView determine = (TextView) v.findViewById(R.id.tv_determine);
                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSelectorOfAddr.dismiss();
                            }
                        });
                        determine.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSelectorOfAddr.returnData();
                            }
                        });


                    }
                })
                .setDividerColor(getResources().getColor(R.color.div_color))
                .setContentTextSize(14)//设置滚轮文字大小
                .setTextColorOut(getResources().getColor(R.color.div_color_other))
                .isDialog(false)
                .build();

        mSelectorOfAddr.setPicker(mCountryList, mProvinceList);

    }

    //提交数据
    private void doChangeInformation() {
        String nickname = etNickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            showMessage(getResources().getString(R.string.enter_regist_info_unick_hint));
            return;
        }

        String birthday = tvBirthday.getText().toString();
        if (TextUtils.isEmpty(birthday)) {
            showMessage(getResources().getString(R.string.enter_regist_info_birthday_hint));
            return;
        }

        String address = tvAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            showMessage(getResources().getString(R.string.enter_regist_info_address_hint));
            return;
        }

        if (-1 == mLanguageCode) {
            showMessage(getString(R.string.error_not_languagenvir));
            return;
        }

        if (EmptyUtils.isEmpty(mAvatarList)) {
            mPresenter.requestModifyInfo(nickname, mSexCode, birthday, address, mLanguageCode, null);
        } else {
            File file = new File(mAvatarList.get(0).path);
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    "photo", file.getName(), imageBody);
            mPresenter.requestModifyInfo(nickname, mSexCode, birthday, address, mLanguageCode, part);
        }
    }


}