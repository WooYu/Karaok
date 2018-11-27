package com.clicktech.snsktv.arms.widget.imageloader.glide;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.Target;
import com.clicktech.snsktv.arms.BuildConfig;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.arms.widget.imageloader.ImageConfig;

import java.io.File;

/**
 * Created by jess on 8/5/16 15:19
 * contact with jess.yan.effort@gmail.com
 * 这里放Glide专属的配置信息,可以一直扩展字段,如果外部调用时想让图片加载框架
 * 做一些操作,比如清除或则切换缓存策略,则可以定义一个int类型的变量,内部根据int做不同过的操作
 * 其他操作同理
 */
public class GlideImageConfig extends ImageConfig {
    private int cacheStrategy;//0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
    private Transformation<Bitmap> transformation;//glide用它来改变图形的形状
    private Target[] targets;
    private ImageView[] imageViews;
    private boolean isClearMemory;//清理内存缓存
    private boolean isClearDiskCache;//清理本地缓存

    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.fileImage = builder.fileImage;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.targets = builder.targets;
        this.imageViews = builder.imageViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public static Buidler builder() {
        return new Buidler();
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public Transformation<Bitmap> getTransformation() {
        return transformation;
    }

    public Target[] getTargets() {
        return targets;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public static final class Buidler {
        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private int cacheStrategy;//0对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
        private Transformation<Bitmap> transformation;//glide用它来改变图形的形状
        private Target[] targets;
        private ImageView[] imageViews;
        private boolean isClearMemory;//清理内存缓存
        private boolean isClearDiskCache;//清理本地缓存

        private File fileImage;

        private Buidler() {
        }

        public Buidler url(String imgurl) {
            if (EmptyUtils.isEmpty(imgurl)) {
                return this;
            }
            if (imgurl.startsWith("https") || imgurl.startsWith("http") || imgurl.startsWith("www")) {
                this.url = imgurl;
            } else {
                this.url = BuildConfig.APP_DOMAIN_File + imgurl;
            }
            return this;
        }

        public Buidler url(File fileImage) {
            this.fileImage = fileImage;
            return this;
        }

        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Buidler errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Buidler imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Buidler cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Buidler transformation(Transformation<Bitmap> transformation) {
            this.transformation = transformation;
            return this;
        }

        public Buidler targets(Target... targets) {
            this.targets = targets;
            return this;
        }

        public Buidler imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public Buidler isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public Buidler isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }


        public GlideImageConfig build() {
            return new GlideImageConfig(this);
        }
    }
}
