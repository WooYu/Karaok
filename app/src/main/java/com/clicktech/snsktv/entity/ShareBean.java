package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wy201 on 2018-03-09.
 * 分享实体
 */

public class ShareBean implements Parcelable {
    public static final Parcelable.Creator<ShareBean> CREATOR = new Parcelable.Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
    private String platformName;
    private int platformImg;
    private String language;

    public ShareBean() {
    }

    protected ShareBean(Parcel in) {
        this.platformName = in.readString();
        this.platformImg = in.readInt();
        this.language = in.readString();
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public int getPlatformImg() {
        return platformImg;
    }

    public void setPlatformImg(int platformImg) {
        this.platformImg = platformImg;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.platformName);
        dest.writeInt(this.platformImg);
        dest.writeString(this.language);
    }
}
