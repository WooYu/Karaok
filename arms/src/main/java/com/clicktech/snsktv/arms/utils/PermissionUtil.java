package com.clicktech.snsktv.arms.utils;

import android.Manifest;

import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.rxerrorhandler.core.RxErrorHandler;
import com.clicktech.snsktv.rxerrorhandler.handler.ErrorHandleSubscriber;
import com.tbruyelle.rxpermissions.RxPermissions;

import timber.log.Timber;

/**
 * Created by jess on 17/10/2016 10:09
 * Contact with jess.yan.effort@gmail.com
 */

public class PermissionUtil {
    public static final String TAG = "Permission";

    /**
     * 获取应用程序必备权限,以下权限必备，否则不能进入应用。
     * permission:android.permission.READ_PHONE_STATE（读取电话状态）
     * group:android.permission-group.STORAGE（读取外部存储卡）
     * permission:android.permission.READ_EXTERNAL_STORAGE
     * permission:android.permission.WRITE_EXTERNAL_STORAGE
     *
     * @param requestPermission
     * @param rxPermissions
     * @param errorHandler
     */
    public static void appNecessaryPermission(final RequestPermission requestPermission, RxPermissions
            rxPermissions, RxErrorHandler errorHandler) {
        boolean isPermissionsGranted = rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE)
                && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (isPermissionsGranted) {
            requestPermission.onRequestPermissionSuccess();
        } else {
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE)
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                requestPermission.onRequestPermissionRefuse();
                            }

                        }
                    });
        }
    }

    /**
     * 获取录音的权限(Manifest.permission.RECORD_AUDIO（录音） 、Manifest.permission_group.STORAGE )
     *
     * @param requestPermission
     * @param rxPermissions
     * @param errorHandler
     */
    public static void recordaudio(final RequestPermission requestPermission,
                                   RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)
                        && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                        && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过，则申请
            rxPermissions
                    .request(Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                requestPermission.onRequestPermissionRefuse();
                            }
                        }
                    });
        }
    }

    /**
     * 获取录制MV的权限(Manifest.permission.CAMERA（拍照权限）、Manifest.permission.RECORD_AUDIO、
     * Manifest.permission.WRITE_EXTERNAL_STORAGE、Manifest.permission.READ_EXTERNAL_STORAGE）
     *
     * @param requestPermission
     * @param rxPermissions
     * @param errorHandler
     */
    public static void recordmv(final RequestPermission requestPermission,
                                RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.CAMERA)
                        && rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)
                        && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过，则申请
            rxPermissions
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                requestPermission.onRequestPermissionRefuse();
                            }
                        }
                    });
        }
    }

    //更换头像的权限申请
    public static void selectPhoto(final RequestPermission requestPermission,
                                   RxPermissions rxPermissions, RxErrorHandler errorHandler) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.CAMERA)
                        && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过，则申请
            rxPermissions
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                requestPermission.onRequestPermissionRefuse();
                            }
                        }
                    });
        }
    }

    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(final RequestPermission requestPermission,
                                       RxPermissions rxPermissions, final BaseView view,
                                       RxErrorHandler errorHandler) {
        //先确保是否已经申请过摄像头，和写入外部存储的权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申请过，则申请
            rxPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                Timber.tag(TAG).d("request WRITE_EXTERNAL_STORAGE success");
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                view.showMessage("request WRITE_EXTERNAL_STORAGE permissons failure");
                            }
                        }
                    });
        }
    }


    public interface RequestPermission {
        void onRequestPermissionSuccess();

        void onRequestPermissionRefuse();
    }

}

