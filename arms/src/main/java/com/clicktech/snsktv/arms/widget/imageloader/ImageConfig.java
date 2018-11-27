package com.clicktech.snsktv.arms.widget.imageloader;

import android.widget.ImageView;

import java.io.File;

/**
 * Created by jess on 8/5/16 15:19
 * contact with jess.yan.effort@gmail.com
 * 这里是图片加载配置信息的基类,可以定义一些所有图片加载框架都可以用的通用参数
 */
public class ImageConfig {
    protected String url;
    protected File fileImage;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;


    public String getUrl() {
        return url;
    }

    public File getFileImage() {
        return fileImage;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }
}
