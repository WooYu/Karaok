package com.clicktech.snsktv.arms.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/8.
 */

public class BaseResponse implements Parcelable {
    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        public BaseResponse createFromParcel(Parcel source) {
            return new BaseResponse(source);
        }

        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };
    private String msg;
    private int errCode;

    public BaseResponse() {
    }

    protected BaseResponse(Parcel in) {
        this.msg = in.readString();
        this.errCode = in.readInt();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeInt(this.errCode);
    }
}
