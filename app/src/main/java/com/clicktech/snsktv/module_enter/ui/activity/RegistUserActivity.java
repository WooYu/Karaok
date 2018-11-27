package com.clicktech.snsktv.module_enter.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.clicktech.snsktv.module_enter.contract.RegistUserContract;
import com.clicktech.snsktv.module_enter.inject.component.DaggerRegistUserComponent;
import com.clicktech.snsktv.module_enter.inject.module.RegistUserModule;
import com.clicktech.snsktv.module_enter.presenter.RegistUserPresenter;
import com.clicktech.snsktv.util.sharesdklogin.ThirdUserInfo;
import com.clicktech.snsktv.widget.filetcircleimageview.MLImageView;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
 * Created by Administrator on 2017/3/9.
 */

public class RegistUserActivity extends WEActivity<RegistUserPresenter> implements
        RegistUserContract.View, HeaderView.OnCustomTileListener
        , View.OnClickListener {

    private static final int REQUESTCODE_AVATAR = 100;
    @BindView(R.id.headerview)
    HeaderView headerView;
    @BindView(R.id.regist_iv_avater)
    MLImageView regist_iv_avater;
    @BindView(R.id.regist_tv_avater)
    TextView regist_tv_avater;
    @BindView(R.id.nickname_left)
    TextView nicknameLeft;
    @BindView(R.id.m_nickname)
    EditText mNickname;
    @BindView(R.id.m_nickname_enter)
    ImageView mNicknameEnter;
    @BindView(R.id.sex_left)
    TextView sexLeft;
    @BindView(R.id.m_sex)
    TextView mSex;
    @BindView(R.id.m_sex_enter)
    ImageView mSexEnter;
    @BindView(R.id.birthday_left)
    TextView birthdayLeft;
    @BindView(R.id.m_birthday)
    TextView mBirthday;
    @BindView(R.id.m_birthday_enter)
    ImageView mBirthdayEnter;
    @BindView(R.id.address_left)
    TextView addressLeft;
    @BindView(R.id.m_address)
    TextView mAddress;
    @BindView(R.id.m_address_enter)
    ImageView mAddressEnter;
    @BindView(R.id.btn_next)
    TextView btnNext;
    private ThirdUserInfo mThirdUserInfo;//第三方用户信息
    private int sextype;
    private Calendar c = null;
    private OptionsPickerView optionsPickerView;
    private PopupWindow sexPopuWindow;
    private TimePickerView pvCustomTime;

    private List<String> country = new ArrayList<>();
    private List<List<String>> province = new ArrayList<>();
    private ArrayList<ImageItem> mImageList;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerRegistUserComponent
                .builder()
                .appComponent(appComponent)
                .registUserModule(new RegistUserModule(this)) //请将RegistUserModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_regist_user, null, false);
    }

    @Override
    protected void initData() {
        headerView.setTitleClickListener(this);

        mThirdUserInfo = (ThirdUserInfo) getIntent().getSerializableExtra("thirdinfo");
        if (null == mThirdUserInfo) {
            return;
        }

        setUserInfo();
        initEditTextOfNickName();
        initOptionData();
        initOptionView();
        initSexPopuWindow();
        initCustomTimePicker();
    }

    //设置用户信息
    public void setUserInfo() {
        if (EmptyUtils.isNotEmpty(mThirdUserInfo.getUsericon())) {
            mWeApplication.getAppComponent().imageLoader().loadImage(mWeApplication, GlideImageConfig
                    .builder()
                    .errorPic(R.mipmap.def_avatar_round)
                    .placeholder(R.mipmap.def_avatar_round)
                    .cacheStrategy(2)
                    .url(mThirdUserInfo.getUsericon())
                    .imageView(regist_iv_avater)
                    .build());
        }

        mNickname.setText(mThirdUserInfo.getUsername());

        if ("m".equals(mThirdUserInfo.getGender())) {
            sextype = 1;
            mSex.setText(R.string.setting_datum_sex_man);
        } else {
            sextype = 0;
            mSex.setText(R.string.setting_datum_sex_female);
        }


    }

    //初始化昵称输入框（控制光标显示和隐藏）
    private void initEditTextOfNickName(){
        mNickname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    mNickname.setCursorVisible(true);
                }
                return false;
            }
        });
    }

    //初始化性别选择的控件
    private void initSexPopuWindow() {

        View view = LayoutInflater.from(mWeApplication).inflate(R.layout.popwindow_sex_choose, null);
        sexPopuWindow = new PopupWindow(view);
        sexPopuWindow.setFocusable(true);
        sexPopuWindow.setOutsideTouchable(false);
        sexPopuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        sexPopuWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0000000000);
        sexPopuWindow.setBackgroundDrawable(dw);
        TextView male = (TextView) view.findViewById(R.id.male);
        TextView female = (TextView) view.findViewById(R.id.female);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSex.setText(R.string.setting_datum_sex_man);
                sextype = 1;
                sexPopuWindow.dismiss();
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSex.setText(R.string.setting_datum_sex_female);
                sextype = 0;
                sexPopuWindow.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPopuWindow.dismiss();
            }
        });

    }

    //初始化选择地区的控件
    private void initOptionView() {

        optionsPickerView = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String tx = country.get(options1) + province.get(options1).get(options2);
                        mAddress.setText(tx);
                    }
                }).setLayoutRes(R.layout.layout_chooser_address, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView cancle = (TextView) v.findViewById(R.id.tv_cancle);
                TextView determine = (TextView) v.findViewById(R.id.tv_determine);
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsPickerView.dismiss();
                    }
                });
                determine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsPickerView.returnData();
                    }
                });
            }
        }).setDividerColor(getResources().getColor(R.color.div_color))
                .setContentTextSize(22)//设置滚轮文字大小
                .setTextColorOut(getResources().getColor(R.color.div_color_other))
                .isDialog(false)
                .build();
        optionsPickerView.setPicker(country, province);

    }

    //初始化地区数据
    private void initOptionData() {

        country.add(getString(R.string.enter_nation_cn));
        country.add(getString(R.string.enter_nation_jp));

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
        province.add(province1);
        province.add(province2);

    }

    //初始化出生日期的控件
    private void initCustomTimePicker() {

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        int year = selectedDate.get(Calendar.YEAR); // 获取当前年份
        int birthLimit = getResources().getInteger(R.integer.birth_limits);
        startDate.set(year - birthLimit, 1, 1);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mBirthday.setText(getTime(date));
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
                                pvCustomTime.dismiss();
                            }
                        });
                        determine.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
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


    /**
     * 创建日期及时间选择对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case R.id.m_birthday_enter:
                c = Calendar.getInstance();
                dialog = new DatePickerDialog(
                        this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                if (month + 1 < 10) {
                                    if (dayOfMonth < 10) {
                                        mBirthday.setText(year + "-0" + (month + 1) + "-0" + dayOfMonth + "");
                                    } else if (dayOfMonth > 9) {
                                        mBirthday.setText(year + "-0" + (month + 1) + "-" + dayOfMonth + "");
                                    }
                                } else if (month + 1 > 9) {
                                    if (dayOfMonth < 10) {
                                        mBirthday.setText(year + "-" + (month + 1) + "-0" + dayOfMonth + "");
                                    } else if (dayOfMonth > 9) {
                                        mBirthday.setText(year + "-" + (month + 1) + "-" + dayOfMonth + "");
                                    }
                                }
                            }
                        },
                        c.get(Calendar.YEAR), // 传入年份
                        c.get(Calendar.MONTH), // 传入月份
                        c.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                break;
        }
        return dialog;
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {
    }

    @Override
    @OnClick({R.id.btn_next, R.id.regist_tv_avater, R.id.ll_sex, R.id.ll_birthday, R.id.ll_address})
    public void onClick(View v) {
        mNickname.setCursorVisible(false);
        switch (v.getId()) {
            case R.id.btn_next://登录
                showUserInfo();
                break;
            case R.id.regist_tv_avater://头像
                changeAvatar();
                break;
            case R.id.ll_sex:
                sexPopuWindow.showAtLocation(mSex, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_birthday:
                pvCustomTime.show();
                break;
            case R.id.ll_address:
                optionsPickerView.show();
                break;
        }
    }

    //改变头像
    private void changeAvatar() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUESTCODE_AVATAR);
    }

    private void showUserInfo() {
        String nic = mNickname.getText().toString();
        if (TextUtils.isEmpty(nic)) {
            showMessage(getResources().getString(R.string.enter_regist_info_unick_hint));
            return;
        }

        String bir = mBirthday.getText().toString();
        if (TextUtils.isEmpty(bir)) {
            showMessage(getResources().getString(R.string.enter_regist_info_birthday_hint));
            return;
        }

        String adr = mAddress.getText().toString();
        if (TextUtils.isEmpty(adr)) {
            showMessage(getResources().getString(R.string.enter_regist_info_address_hint));
            return;
        }

        if (EmptyUtils.isNotEmpty(mImageList)) {
            String avatarpath = mImageList.get(0).path;
            mThirdUserInfo.setUsericon(avatarpath);
        }

        mThirdUserInfo.setNickname(nic);
        mThirdUserInfo.setGender(String.valueOf(sextype));
        mThirdUserInfo.setBirthday(bir);
        mThirdUserInfo.setArea(adr);

        Intent intent = new Intent(this, ShowUserInfoActivity.class);
        intent.putExtra("thirdinfo", mThirdUserInfo);
        UiUtils.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == REQUESTCODE_AVATAR) {
            if (null == data) {
                return;
            }
            mImageList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (EmptyUtils.isEmpty(mImageList)) {
                return;
            }

            String imgpath = mImageList.get(0).path;
            Glide.with(this)
                    .load(imgpath)
                    .error(R.mipmap.def_avatar_round)
                    .placeholder(R.mipmap.def_avatar_round)
                    .into(regist_iv_avater);

        }
    }
}
