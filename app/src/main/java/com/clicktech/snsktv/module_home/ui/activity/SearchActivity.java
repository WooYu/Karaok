package com.clicktech.snsktv.module_home.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.entity.SearchHistory;
import com.clicktech.snsktv.module_home.contract.SearchContract;
import com.clicktech.snsktv.module_home.inject.component.DaggerSearchComponent;
import com.clicktech.snsktv.module_home.inject.module.SearchModule;
import com.clicktech.snsktv.module_home.presenter.SearchPresenter;
import com.clicktech.snsktv.module_home.ui.fragment.SearchReferFragment;
import com.clicktech.snsktv.module_home.ui.fragment.VerbalAssociateFragment;
import com.clicktech.snsktv.util.HistoryUtils;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

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
 * Created by Administrator on 2017/1/11.
 * 搜索歌曲或者歌手
 */

public class SearchActivity extends WEActivity<SearchPresenter> implements
        SearchContract.View, View.OnClickListener {


    public static final int RequestCode_VoiceSearch = 0x0011;
    @BindView(R.id.et_searchkey)
    EditText searchKeyET;
    @BindView(R.id.iv_voicesearch)
    ImageView voiceSearchIV;
    @BindView(R.id.iv_clear)
    ImageView clearInputIV;
    private SearchReferFragment mReferFragment;
    private VerbalAssociateFragment mAssociateFragment;
    private Fragment mCurFragment;
    private String mClassType;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchComponent
                .builder()
                .appComponent(appComponent)
                .searchModule(new SearchModule(this)) //请将SearchModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_search, null, false);
    }

    @Override
    protected void initData() {
        mClassType = getIntent().getStringExtra("classtype");

        initEditText();
        initSearchLayout();
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

    /**
     * 初始化搜索的布局
     */
    private void initSearchLayout() {
        mReferFragment = SearchReferFragment.newInstance();
        mAssociateFragment = VerbalAssociateFragment.newInstance();

        mCurFragment = mReferFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_search, mReferFragment);
        fragmentTransaction.add(R.id.fl_search, mAssociateFragment);
        fragmentTransaction.show(mReferFragment).hide(mAssociateFragment);
        fragmentTransaction.commit();
    }

    @OnClick({R.id.iv_voicesearch, R.id.iv_clear, R.id.tv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_voicesearch://语音搜索
                onVoiceSearchClicked();
                break;
            case R.id.iv_clear://清空输入
                searchKeyET.setText("");
                break;
            case R.id.tv_cancel://返回键
                hideInputBox();
                break;
        }
    }

    /**
     * 切换Fragment
     */
    private void switchFragment(Fragment fragment) {
        if (mCurFragment != fragment) {
            if (fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment)
                        .show(fragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment)
                        .add(R.id.fl_search, fragment)
                        .commit();
            }
            mCurFragment = fragment;
        }
    }

    @Override
    public void turn2ResultShow(String text) {
        searchKeyET.setText(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data)
            return;

        //语言搜索
        if (requestCode == RequestCode_VoiceSearch && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (EmptyUtils.isEmpty(results)) {
                return;
            }
            searchKeyET.setText(results.get(0));
        }
    }

    private void initEditText() {
        RxTextView.textChangeEvents(searchKeyET)
                .debounce(300, TimeUnit.MILLISECONDS)  //debounce:每次文本更改后有300毫秒的缓冲时间，默认在computation调度器
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        boolean filter = TextUtils.isEmpty(textViewTextChangeEvent.text());
                        switchSearchButton(filter);
                        return !filter;
                    }
                })
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        sendSearchRequest();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e("异常：" + throwable.getMessage());
                    }
                });
    }

    /**
     * 是否显示清除按钮
     * 1.如果EditText没有检查到输入，那么
     * A.切换到SearchReferFragment
     * B.启用语音搜索
     * 2.如果EditText检查到输入，那么
     * A.切换到VerbalAssociateFragment
     * B.启用删除按钮
     *
     * @param noInput
     */
    private void switchSearchButton(boolean noInput) {
        if (noInput) {
            switchFragment(mReferFragment);
            clearInputIV.setVisibility(View.GONE);
            voiceSearchIV.setVisibility(View.VISIBLE);
        } else {
            switchFragment(mAssociateFragment);
            clearInputIV.setVisibility(View.VISIBLE);
            voiceSearchIV.setVisibility(View.GONE);
        }
    }

    /**
     * 发送搜索请求
     */
    private void sendSearchRequest() {
        String searkey = searchKeyET.getText().toString().trim();
        if (EmptyUtils.isEmpty(searkey)) {
            return;
        }
        SearchHistory.SearchHistoryBean bean = new SearchHistory.SearchHistoryBean();
        bean.setName(searkey);
        HistoryUtils.putSearchHistory(mWeApplication, bean);

        Bundle bundle = new Bundle();
        bundle.putString("searchkey", searkey);
        bundle.putString("classtype", mClassType);
        mAssociateFragment.setData(bundle);
        mReferFragment.setData(bundle);
    }

    private void onVoiceSearchClicked() {
        try {
            if (!isApkInstalled(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)) {
                showMessage(getString(R.string.tip_voicesearch_uninstalled));
                return;
            }
            //通过Intent传递语音识别的模式，开启语音
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            //语言模式和自由模式的语音识别
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            //提示语音开始
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.tip_voicesearch_hint));
            //开始语音识别
            startActivityForResult(intent, RequestCode_VoiceSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //是否安装了语言搜索功能
    public boolean isApkInstalled(String strIntent) {
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(new Intent(strIntent), 0);
        return activities != null && !activities.isEmpty();
    }

    //隐藏输入框
    public void hideInputBox() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchKeyET.getWindowToken(), 0); //强制隐藏键盘

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                killMyself();
            }
        }, 500);
    }


}