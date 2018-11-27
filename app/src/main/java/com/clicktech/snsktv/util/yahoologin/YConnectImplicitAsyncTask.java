package com.clicktech.snsktv.util.yahoologin;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.module_enter.ui.activity.RegistUser_YaHooActivity;

import jp.co.yahoo.yconnect.YConnectImplicit;
import jp.co.yahoo.yconnect.core.api.ApiClientException;
import jp.co.yahoo.yconnect.core.oidc.IdTokenObject;
import jp.co.yahoo.yconnect.core.oidc.OIDCDisplay;
import jp.co.yahoo.yconnect.core.oidc.OIDCPrompt;
import jp.co.yahoo.yconnect.core.oidc.OIDCScope;
import jp.co.yahoo.yconnect.core.oidc.UserInfoObject;
import jp.co.yahoo.yconnect.core.util.YConnectLogger;

/**
 * Implicit Sample Class
 *
 * @author Copyright (C) 2017 Yahoo Japan Corporation. All Rights Reserved.
 */
public class YConnectImplicitAsyncTask extends AsyncTask<String, Integer, Long> {

    private final static String TAG = YConnectImplicitAsyncTask.class.getSimpleName();

    private Activity activity;
    private Handler handler;

    private String clientId;
    private String idTokenString;

    //同意キャンセル時の挙動を指定
    private String bail;
    //最大認証経過時間
    private String maxAge;


    public YConnectImplicitAsyncTask(Activity activity, String idTokenString) {
        this.activity = activity;
        this.clientId = RegistUser_YaHooActivity.clientId;
        this.idTokenString = idTokenString;
        this.bail = RegistUser_YaHooActivity.BAIL;
        this.maxAge = RegistUser_YaHooActivity.MAX_AGE;
        handler = new Handler();
    }

    @Override
    protected Long doInBackground(String... params) {

        Log.d(TAG, params[0]);

        // YConnectインスタンス取得
        YConnectImplicit yconnect = YConnectImplicit.getInstance();

        try {
            // Access Tokenの読み込み
            String storedAccessToken = DataHelper.getStringSF(activity, "yahoo_access_token");

            /***************
             Verify ID Token.
             ***************/

            Log.i(TAG, "Verify ID Token.");

            // nonceの読み込み
            String nonce = DataHelper.getStringSF(activity, "yahoo_nonce");

            // ID Tokenの検証
            yconnect.verifyIdToken(idTokenString, clientId, nonce, storedAccessToken, maxAge);

            // 復号化されたID Token情報を取得
            final IdTokenObject idtokenObject = yconnect.getIdTokenObject();

            /*****************
             Request UserInfo.
             *****************/

            Log.i(TAG, "Request UserInfo.");

            YConnectLogger.setLogLevel(YConnectLogger.DEBUG);

            // UserInfoエンドポイントにリクエスト
            yconnect.requestUserInfo(storedAccessToken);
            // UserInfo情報を取得
            final UserInfoObject userInfoObject = yconnect.getUserInfoObject();

            handler.post(new Runnable() {
                public void run() {
                    if (activity instanceof RegistUser_YaHooActivity) {
                        ((RegistUser_YaHooActivity) activity).updateView(userInfoObject);
                    }
                }
            });

        } catch (ApiClientException e) {

            // エラーレスポンスが"Invalid_Token"であるかチェック
            if ("invalid_token".equals(e.getError())) {

                /*********************
                 Refresh Access Token.
                 *********************/

                Log.i(TAG, "Refresh Access Token.");

                // ImplicitフローでAccess Tokenの有効期限がきれた場合は
                // 初回と同様に新たにAccess Tokenを取得してください

                String state = "44GC44Ga44GrWeOCk+ODmuODreODmuODrShez4leKQ==";
                String nonce = "KOOAjeODu8+J44O7KeOAjVlhaG9vISAo77yP44O7z4njg7sp77yPSkFQQU4=";
                String display = OIDCDisplay.TOUCH;
                String[] prompt = {OIDCPrompt.DEFAULT};
                String[] scope = {OIDCScope.OPENID, OIDCScope.PROFILE, OIDCScope.EMAIL, OIDCScope.ADDRESS};

                DataHelper.setStringSF(activity, "yahoo_state", state);
                DataHelper.setStringSF(activity, "yahoo_nonce", nonce);

                yconnect.init(RegistUser_YaHooActivity.clientId,
                        RegistUser_YaHooActivity.customUriScheme, state, display, prompt, scope, nonce, bail, maxAge);
                yconnect.requestAuthorization(activity);

            }

            Log.e(TAG, "error=" + e.getError() + ", error_description=" + e.getErrorDescription());
        } catch (Exception e) {
            Log.e(TAG, "error=" + e.getMessage());
        }

        return 1L;
    }

}