package com.clicktech.snsktv.module_discover.ui.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clicktech.snsktv.BuildConfig;
import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.UiUtils;
import com.clicktech.snsktv.common.WEActivity;
import com.clicktech.snsktv.common.inject.component.AppComponent;
import com.clicktech.snsktv.common.net.RequestParams_Maker;
import com.clicktech.snsktv.module_discover.contract.ReportContract;
import com.clicktech.snsktv.module_discover.inject.component.DaggerReportComponent;
import com.clicktech.snsktv.module_discover.inject.module.ReportModule;
import com.clicktech.snsktv.module_discover.presenter.ReportPresenter;
import com.clicktech.snsktv.util.GalleryUtil;
import com.clicktech.snsktv.widget.dialog.NetworkDialog;
import com.clicktech.snsktv.widget.lgimgcompressor.LGImgCompressor;
import com.clicktech.snsktv.widget.titlebar.HeaderView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
 * Created by Administrator on 2017/6/26.
 * 举报
 */

public class ReportActivity extends WEActivity<ReportPresenter> implements ReportContract.View,
        HeaderView.OnCustomTileListener, LGImgCompressor.CompressListener {

    private static final int IMAGE_PICKER = 10002;//换头像
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;
    private static final int REQUEST_CODE_CUT_PHOTO = 4;
    private static final int REQUEST_CODE_SELECT_PIC_KITKAT = 11;
    private static final int REQUEST_CODE_SELECT_PIC = 12;
    private final static int CAMERA_REQESTCODE = 100;

    @BindView(R.id.headerview)
    HeaderView headerview;
    @BindView(R.id.img_0)
    ImageView img0;
    @BindView(R.id.rl_0)
    RelativeLayout rl0;
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.img_3)
    ImageView img3;
    @BindView(R.id.rl_3)
    RelativeLayout rl3;
    @BindView(R.id.img_4)
    ImageView img4;
    @BindView(R.id.rl_4)
    RelativeLayout rl4;
    @BindView(R.id.img_5)
    ImageView img5;
    @BindView(R.id.rl_5)
    RelativeLayout rl5;
    @BindView(R.id.img_6)
    ImageView img6;
    @BindView(R.id.rl_6)
    RelativeLayout rl6;
    @BindView(R.id.img_7)
    ImageView img7;
    @BindView(R.id.rl_7)
    RelativeLayout rl7;
    @BindView(R.id.img_8)
    ImageView img8;
    @BindView(R.id.rl_8)
    RelativeLayout rl8;
    @BindView(R.id.add_img)
    RelativeLayout addImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.report_detail)
    EditText reportDetail;
    @BindView(R.id.update_img)
    ImageView update_img;
    @BindView(R.id.img_desc)
    TextView img_desc;
    String imgpath_choose = null;
    private int reportType = 0;
    private String mCameraImagePath;
    private Bitmap photo;
    private String mPhotoFilePath;
    private PopupWindow camerPopupWindow;
    private RxPermissions mRxPermissions;
    private String userId;
    private File imageFile;

    private NetworkDialog mNetworkDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerReportComponent
                .builder()
                .appComponent(appComponent)
                .reportModule(new ReportModule(this)) //请将ReportModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_report, null, false);
    }

    @Override
    protected void initData() {
        headerview.setTitleClickListener(this);
        userId = getIntent().getStringExtra("userId");

        mNetworkDialog = new NetworkDialog(this);
        initCamera();
    }

    @Override
    public void showLoading() {
        if (null != mNetworkDialog) {
            mNetworkDialog.showNetWorkDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (null != mNetworkDialog) {
            mNetworkDialog.dismissNetWorkDialog();
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
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick({R.id.rl_0, R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5, R.id.rl_6, R.id.rl_7, R.id.rl_8, R.id.add_img, R.id.upload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_0:
                showImg(0);
                requestReportType(1);
                break;
            case R.id.rl_1:
                showImg(1);
                requestReportType(1);
                break;
            case R.id.rl_2:
                showImg(2);
                requestReportType(2);
                break;
            case R.id.rl_3:
                showImg(3);
                requestReportType(3);
                break;
            case R.id.rl_4:
                showImg(4);
                requestReportType(4);
                break;
            case R.id.rl_5:
                showImg(5);
                requestReportType(5);
                break;
            case R.id.rl_6:
                showImg(6);
                requestReportType(6);
                break;
            case R.id.rl_7:
                showImg(7);
                requestReportType(7);
                break;
            case R.id.rl_8:
                showImg(8);
                requestReportType(8);
                break;
            case R.id.add_img:     //获取图片
                camerPopupWindow.showAtLocation(headerview, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.upload:
                submit();
                break;

        }
    }

    private void chooseAvatar() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 222);
                return;
            } else {

                mCameraImagePath = doTakePicture();//调用具体方法
            }
        } else {

            mCameraImagePath = doTakePicture();//调用具体方法
        }
    }

    public void requestReportType(int type) {
        reportType = type;
    }

    public void showImg(int index) {

        switch (index) {
            case 0:
                img0.setVisibility(View.VISIBLE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 1:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 2:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.VISIBLE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 3:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.VISIBLE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 4:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.VISIBLE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 5:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.VISIBLE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 6:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.VISIBLE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);
                break;

            case 7:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.VISIBLE);
                img8.setVisibility(View.GONE);
                break;

            case 8:
                img0.setVisibility(View.GONE);
                img1.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                img4.setVisibility(View.GONE);
                img5.setVisibility(View.GONE);
                img6.setVisibility(View.GONE);
                img7.setVisibility(View.GONE);
                img8.setVisibility(View.VISIBLE);
                break;

        }

    }

    public void submit() {

        String desc = reportDetail.getText().toString().trim();
        if (mPhotoFilePath == null) {

            mPresenter.report(RequestParams_Maker.getReport(mWeApplication.getUserID(), userId, String.valueOf(reportType), desc), null);

        } else {

            File file = new File(mPhotoFilePath);
            if (file.exists()) {
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("photo", "file.jpg", imageBody);
                mPresenter.report(RequestParams_Maker.getReport(mWeApplication.getUserID(), userId, String.valueOf(reportType), desc), part);
            } else {
                mPresenter.report(RequestParams_Maker.getReport(mWeApplication.getUserID(), userId, String.valueOf(reportType), desc), null);
            }

        }


    }

    private void initCamera() {

        View view = LayoutInflater.from(mWeApplication).inflate(R.layout.popwindow_camera, null);
        camerPopupWindow = new PopupWindow(view);
        camerPopupWindow.setFocusable(true);
        camerPopupWindow.setOutsideTouchable(false);
        camerPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        camerPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0000000000);
        camerPopupWindow.setBackgroundDrawable(dw);
        TextView photograph = (TextView) view.findViewById(R.id.photograph);
        TextView localCamera = (TextView) view.findViewById(R.id.local_camera);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.requestPermissions(1);
                camerPopupWindow.dismiss();
            }
        });
        localCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.requestPermissions(2);
                camerPopupWindow.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camerPopupWindow.dismiss();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQESTCODE) {
                LGImgCompressor.getInstance(this).withListener(this).
                        starCompressWithDefault(Uri.fromFile(imageFile).toString());
            }
        }

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String imagePath = images.get(0).path;
                if (!TextUtils.isEmpty(imagePath)) {
                    mPhotoFilePath = imagePath;
                    LGImgCompressor.getInstance(this).withListener(this).
                            starCompressWithDefault(imagePath);
                }
            }
        }

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CODE_SELECT_PIC_KITKAT
                || requestCode == REQUEST_CODE_SELECT_PIC) {
            String imagePath;
            if (requestCode == REQUEST_CODE_SELECT_PIC_KITKAT) {
                imagePath = GalleryUtil.selectPathUpKitkat(mWeApplication, data.getData());
            } else {
                imagePath = GalleryUtil.selectPathDownKitkat(mWeApplication, data.getData());
            }
            startPhotoZoom(imagePath, 150);

        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            if (mCameraImagePath != null) {
                LGImgCompressor.getInstance(this).withListener(this)
                        .starCompressWithDefault(mCameraImagePath);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 拍照获取图片
     */
    public String doTakePicture() {

        StringBuilder fileName = new StringBuilder();
        fileName.append(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        File folder = new File(fileName.toString());
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return null;
            }
        }
        fileName.append(System.currentTimeMillis());
        fileName.append(".JPEG");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(fileName.toString()));
            //来对目标应用临时授权该Uri所代表的文件。
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri fileUri = Uri.fromFile(new File(fileName.toString()));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        return fileName.toString();
    }

    private void startPhotoZoom(String filePath, int size) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            String url = "";
            Uri mUrl;
            Intent intent = new Intent("com.android.camera.action.CROP");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String murl = new File(filePath).getAbsolutePath();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DATA, filePath);
                intent.setDataAndType(mWeApplication.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues), "image/*");

            } else {
                mUrl = Uri.fromFile(new File(filePath));
                intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
            }

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void turnToImagePicker() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    public void setTitleLeftClick() {
        killMyself();
    }

    @Override
    public void setTitleRightClick() {
    }

    @Override
    public void takePictureFormCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = timeStamp + "_";
        File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        imageFile = null;
        try {
            imageFile = File.createTempFile(fileName, ".jpg", fileDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", imageFile);
            //来对目标应用临时授权该Uri所代表的文件。
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri fileUri = Uri.fromFile(imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        startActivityForResult(intent, CAMERA_REQESTCODE);
    }

    @Override
    public void onCompressStart() {

    }

    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;
        File file = new File(compressResult.getOutPath());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            update_img.setImageBitmap(bitmap);
            img_desc.setVisibility(View.GONE);
            float imageFileSize = file.length() / 1024f;
            Timber.e("image info width:" + bitmap.getWidth() + " height:" + bitmap.getHeight() +
                    " size:" + imageFileSize + "kb" + "imagePath:" + file.getAbsolutePath());
            mPhotoFilePath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
