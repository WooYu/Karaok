package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clicktech.snsktv.arms.base.BaseResponse;

public class SignCosResponse extends BaseResponse implements Parcelable {

    public static final Creator<SignCosResponse> CREATOR = new Creator<SignCosResponse>() {
        @Override
        public SignCosResponse createFromParcel(Parcel source) {
            return new SignCosResponse(source);
        }

        @Override
        public SignCosResponse[] newArray(int size) {
            return new SignCosResponse[size];
        }
    };
    /**
     * appId : 1252264909
     * sign : 88CRjahmOuOnrydRtjOrjrrHkA5hPTEyNTIyNjQ5MDkmYj1sY3dvcmxkJms9QUtJRERTZGxxcjR3
     * MDIwd3VwQWZZUEVRb0FWbTBCRUZyREFCJmU9MTQ5NTYxMzQxMiZ0PTE0OTU1MjcwMTImcj00NzQ2
     * MjQ3NDcmZj0=
     * region : tj
     * bucketName : lcworld
     * directory : /
     */

    private int appId;
    private String sign;
    private String region;
    private String bucketName;
    private String directory;

    public SignCosResponse() {
    }

    protected SignCosResponse(Parcel in) {
        super(in);
        this.appId = in.readInt();
        this.sign = in.readString();
        this.region = in.readString();
        this.bucketName = in.readString();
        this.directory = in.readString();
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.appId);
        dest.writeString(this.sign);
        dest.writeString(this.region);
        dest.writeString(this.bucketName);
        dest.writeString(this.directory);
    }
}
