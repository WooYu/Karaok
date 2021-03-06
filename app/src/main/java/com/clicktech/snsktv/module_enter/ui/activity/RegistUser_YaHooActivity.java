package com.clicktech.snsktv.module_enter.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
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
import com.clicktech.snsktv.arms.base.BaseActivity;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.util.sharesdklogin.ThirdUserInfo;
import com.clicktech.snsktv.util.yahoologin.YConnectImplicitAsyncTask;
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
import jp.co.yahoo.yconnect.YConnectImplicit;
import jp.co.yahoo.yconnect.core.oauth2.AuthorizationException;
import jp.co.yahoo.yconnect.core.oidc.OIDCDisplay;
import jp.co.yahoo.yconnect.core.oidc.OIDCPrompt;
import jp.co.yahoo.yconnect.core.oidc.OIDCScope;
import jp.co.yahoo.yconnect.core.oidc.UserInfoObject;


public class RegistUser_YaHooActivity extends BaseActivity implements HeaderView.OnCustomTileListener
        , View.OnClickListener {

    // Client ID
    public final static String clientId = "dj00aiZpPUM3dDM2aWVwTlpIRCZzPWNvbnN1bWVyc2VjcmV0Jng9Nzc-";
    //1を指定した場合、同意キャンセル時にredirect_uri設定先へ遷移する
    public final static String BAIL = "1";
    //最大認証経過時間
    public final static String MAX_AGE = "3600";
    // カスタムURIスキーム
    public final static String customUriScheme = "yj-nokaraok://cb";
    private static final int REQUESTCODE_AVATAR = 100;
    private final static String TAG = RegistUser_YaHooActivity.class.getSimpleName();
    @BindView(R.id.regist_iv_avater)
    MLImageView registIvAvater;
    @BindView(R.id.regist_tv_avater)
    TextView registTvAvater;
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
    @BindView(R.id.headerview)
    HeaderView headerview;
    private UserInfoObject mUserInfoObject;//第三方用户信息
    private int sextype;
    private Calendar c = null;
    private OptionsPickerView optionsPickerView;
    private PopupWindow sexPopuWindow;
    private TimePickerView pvCustomTime;
    private List<String> country = new ArrayList<>();
    private List<List<String>> province = new ArrayList<>();
    private ArrayList<ImageItem> mImageList;

    @Override
    protected void ComponentInject() {
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

    @Override
    public void setTitleLeftClick() {
        finish();
    }

    @Override
    public void setTitleRightClick() {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_regist_user, null, false);
    }

    @Override
    protected void initData() {

        headerview.setTitleClickListener(this);
        linkToTheYahoo();
        initEditTextOfNickName();
        initOptionData();
        initOptionView();
        initSexPopuWindow();
        initCustomTimePicker();
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

    //链接到雅虎
    private void linkToTheYahoo() {
        // YConnectインスタンス取得
        YConnectImplicit yconnect = YConnectImplicit.getInstance();

        // ログレベル設定（必要に応じてレベルを設定してください）
        //YConnectLogger.setLogLevel(YConnectLogger.DEBUG);

        Intent intent = getIntent();

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            /*************************************************
             Parse the Response Url and Save the Access Token.
             *************************************************/

            try {

                Log.i(TAG, "Get Response Url and parse it.");

                // stateの読み込み
                String state = DataHelper.getStringSF(this, "yahoo_state");

                // response Url(Authorizationエンドポイントより受け取ったコールバックUrl)から各パラメータを抽出
                Uri uri = intent.getData();
                yconnect.parseAuthorizationResponse(uri, customUriScheme, state);
                // Access Token、ID Tokenを取得
                String accessTokenString = yconnect.getAccessToken();
                long expiration = yconnect.getAccessTokenExpiration();
                String idTokenString = yconnect.getIdToken();

                // Access Tokenを保存
                DataHelper.setStringSF(this, "yahoo_access_token", accessTokenString);

                // 別スレッド(AsynckTask)でID Tokenの検証、UserInfoエンドポイントにリクエスト
                YConnectImplicitAsyncTask asyncTask = new YConnectImplicitAsyncTask(this, idTokenString);
                asyncTask.execute("Verify ID Token and Request UserInfo.");

            } catch (AuthorizationException e) {
                Log.e(TAG, "error=" + e.getError() + ", error_description=" + e.getErrorDescription());
            } catch (Exception e) {
                Log.e(TAG, "error=" + e.getMessage());
            }

        } else {

            /********************************************************
             Request Authorization Endpoint for getting Access Token.
             ********************************************************/

            Log.i(TAG, "Request authorization.");

            // 各パラメーター初期化
            // リクエストとコールバック間の検証用のランダムな文字列を指定してください
            String state = "44GC44Ga44GrWeOCk+ODmuODreODmuODrShez4leKQ==";
            // リプレイアタック対策のランダムな文字列を指定してください
            String nonce = "KOOAjeODu8+J44O7KeOAjVlhaG9vISAo77yP44O7z4njg7sp77yPSkFQQU4=";
            String display = OIDCDisplay.TOUCH;
            String[] prompt = {OIDCPrompt.DEFAULT};
            String[] scope = {OIDCScope.OPENID, OIDCScope.PROFILE, OIDCScope.EMAIL, OIDCScope.ADDRESS};

            try {
                // state、nonceを保存
                DataHelper.setStringSF(this, "yahoo_state", state);
                DataHelper.setStringSF(this, "yahoo_nonce", nonce);

            } catch (Exception e) {
                Log.e(TAG, "error=" + e.getMessage());
            }

            // 各パラメーターを設定
            yconnect.init(clientId, customUriScheme, state, display, prompt, scope, nonce, BAIL, MAX_AGE);
            // Authorizationエンドポイントにリクエスト
            // (ブラウザーを起動して同意画面を表示)
            yconnect.requestAuthorization(this);

        }
    }

    //请求到用户数据，更新界面
    public void updateView(UserInfoObject userinfo) {
        mUserInfoObject = userinfo;

        if (EmptyUtils.isNotEmpty(userinfo.getPicture())) {

            Glide.with(this)
                    .load(userinfo.getPicture())
                    .asBitmap()
                    .placeholder(R.mipmap.def_avatar_round)
                    .error(R.mipmap.def_avatar_round)
                    .into(registIvAvater);
        }

        mNickname.setText(userinfo.getNickname());

        if ("male".equals(userinfo.getGender())) {
            sextype = 1;
            mSex.setText(R.string.setting_datum_sex_man);
        } else {
            sextype = 0;
            mSex.setText(R.string.setting_datum_sex_female);
        }

        mAddress.setText(userinfo.getAddressRegion() + userinfo.getAddressLocality());
    }

    //初始化性别选择的控件
    private void initSexPopuWindow() {

        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_sex_choose, null);
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

    //改变头像
    private void changeAvatar() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUESTCODE_AVATAR);
    }

    private void showUserInfo() {
        if (null == mUserInfoObject) {
            return;
        }

        if (EmptyUtils.isEmpty(mUserInfoObject.getPicture())
                && EmptyUtils.isEmpty(mImageList)) {
            UiUtils.SnackbarText(getResources().getString(R.string.error_nohead));
            return;
        }

        String nic = mNickname.getText().toString();
        if (TextUtils.isEmpty(nic)) {
            UiUtils.SnackbarText(getResources().getString(R.string.enter_regist_info_unick_hint));
            return;
        }

        String bir = mBirthday.getText().toString();
        if (TextUtils.isEmpty(bir)) {
            UiUtils.SnackbarText(getResources().getString(R.string.enter_regist_info_birthday_hint));
            return;
        }

        String adr = mAddress.getText().toString();
        if (TextUtils.isEmpty(adr)) {
            UiUtils.SnackbarText(getResources().getString(R.string.enter_regist_info_address_hint));
            return;
        }

        ThirdUserInfo thirdUserInfo = new ThirdUserInfo();

        if (EmptyUtils.isNotEmpty(mImageList)) {
            String avatarpath = mImageList.get(0).path;
            thirdUserInfo.setUsericon(avatarpath);
        } else {
            thirdUserInfo.setUsericon(mUserInfoObject.getPicture());
        }

        thirdUserInfo.setNickname(nic);
        thirdUserInfo.setGender(String.valueOf(sextype));
        thirdUserInfo.setBirthday(bir);
        thirdUserInfo.setArea(adr);
        thirdUserInfo.setUserid(mUserInfoObject.getSub());

        Intent intent = new Intent(this, ShowUserInfoActivity.class);
        intent.putExtra("thirdinfo", thirdUserInfo);
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
                    .into(registIvAvater);

        }
    }
}
