package com.clicktech.snsktv.module_discover.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.module_discover.contract.WebContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerWebComponent;
import com.clicktech.snsktv.module_discover.inject.module.WebModule;
import com.clicktech.snsktv.module_discover.presenter.WebPresenter;
import com.clicktech.snsktv.widget.titlebar.HeaderView;

import butterknife.BindView;
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
 * Created by Administrator on 2017/4/22.
 */

public class WebActivity extends WEActivity<WebPresenter> implements WebContract.View, HeaderView.OnCustomTileListener {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.headerview)
    HeaderView headerView;
    private String url;
    private String title;

    //    private WebView webView;
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerWebComponent
                .builder()
                .appComponent(appComponent)
                .webModule(new WebModule(this)) //请将WebModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_web, null, false);
    }

    @Override
    protected void initData() {

        headerView.setTitleClickListener(this);
        getUrl();
        headerView.setTitleName(title);
        initSetting();
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


    private void initSetting() {


        WebSettings webSettings = webView.getSettings();
//        if (null != webSettings) {
        webSettings.setJavaScriptEnabled(true);
//            webView.addJavascriptInterface(new jsInterface(mContext), "jsobj");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//
//            }
//
//                                     @Override
//                                     public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                                         handler.proceed();
//
//                                         super.onReceivedSslError(view, handler, error);
//
//                                     }
//                                 }
//
//
//
//
//        );
        //自适应屏幕大小
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);//针对部分页面使用存储功能导致无法显示页面
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setAllowContentAccess(true);
//			webSettings.setDefaultZoom(defaultZoomDensity);
        webSettings.setSavePassword(true);
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);   //缓存
        webSettings.setSaveFormData(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(false);


//            mWebview.setHorizontalScrollbarOverlay(true);
//            mWebview.setHorizontalScrollBarEnabled(false);
//            mWebview.setVerticalScrollBarEnabled(false);
//            webSettings.setPluginState(WebSettings.PluginState.ON);
//            //设置数据库缓存路径
//            mWebview.getSettings().setDatabasePath(cacheDirPath);
//            //设置  Application Caches 缓存目录
//            mWebview.getSettings().setAppCachePath(cacheDirPath);
//            //开启 Application Caches 功能
//            mWebview.getSettings().setAppCacheEnabled(true);


//        }

        webView.loadUrl(url);
//        webView.loadUrl("https://www.baidu.com/");
//        webView.loadUrl("http://www.9ku.com/");
//        webView.loadUrl("http://www.9ku.com/");

        Timber.e("url" + url + "；" + title + "-----------------------------");


    }

    private void getUrl() {

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");


    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {

    }
}
