package com.clicktech.snsktv.module_mine.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.common.ConstantConfig;
import com.clicktech.snsktv.entity.PushMessageEntity;
import com.clicktech.snsktv.module_enter.ui.activity.MainActivity;
import com.clicktech.snsktv.module_mine.ui.activity.MessageActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Timber.tag(TAG).d("This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Timber.tag(TAG).d("Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Timber.tag(TAG).d("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Timber.tag(TAG).d("[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Timber.tag(TAG).d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Timber.tag(TAG).d("[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Timber.tag(TAG).d("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Timber.tag(TAG).d("[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Timber.tag(TAG).d("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Timber.tag(TAG).d("[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Timber.tag(TAG).d("[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Gson gson = new Gson();
        PushMessageEntity pushMessageEntity = gson.fromJson(extras, PushMessageEntity.class);
        if (null != pushMessageEntity) {
            int msgtype = pushMessageEntity.getMsgType();

//            if(msgtype ==context.getResources().getInteger(R.integer.msgtype_albumgift_notfollowed)){
//                //最近听众
//            int num = DataHelper.getIntergerSF(context, ConstantConfig.PUSHMESSAGE_RECENTLYAUDIENCE);
//            DataHelper.setIntergerSF(context, ConstantConfig.PUSHMESSAGE_RECENTLYAUDIENCE, ++num);
//            }
            if (msgtype == context.getResources().getInteger(R.integer.msgtype_albumgift_notfollowed)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_workgift_notfollowed)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_albumgift_followed)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_workgift_followed)) {
                //礼物消息
                int num = DataHelper.getIntergerSF(context, ConstantConfig.PUSHMESSAGE_GIFT);
                DataHelper.setIntergerSF(context, ConstantConfig.PUSHMESSAGE_GIFT, ++num);

            } else if (msgtype == context.getResources().getInteger(R.integer.msgtype_system)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_someonewatchme)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_dailywelfare)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_publish_concerned)) {
                //系统消息
                int num = DataHelper.getIntergerSF(context, ConstantConfig.PUSHMESSAGE_SYSTEM);
                DataHelper.setIntergerSF(context, ConstantConfig.PUSHMESSAGE_SYSTEM, ++num);

            } else if (msgtype == context.getResources().getInteger(R.integer.msgtype_workcomment_followed)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_albumcomment_followed)) {
                //已关注人评论
                int num = DataHelper.getIntergerSF(context, ConstantConfig.PUSHMESSAGE_COMMENT_FOLLOWED);
                DataHelper.setIntergerSF(context, ConstantConfig.PUSHMESSAGE_COMMENT_FOLLOWED, ++num);

            } else if (msgtype == context.getResources().getInteger(R.integer.msgtype_workcomment_notfollowed)
                    || msgtype == context.getResources().getInteger(R.integer.msgtype_albumcomment_notfollowed)) {
                //未关注人评论
                int num = DataHelper.getIntergerSF(context, ConstantConfig.PUSHMESSAGE_COMMENT_NOTFOLLOWED);
                DataHelper.setIntergerSF(context, ConstantConfig.PUSHMESSAGE_COMMENT_NOTFOLLOWED, ++num);
            }

        }

        Intent msgIntent = new Intent(MainActivity.ACTION_MSGPUSH);
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }
}
