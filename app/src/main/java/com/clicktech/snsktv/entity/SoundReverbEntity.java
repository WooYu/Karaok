package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/14.
 * 混响
 */

public class SoundReverbEntity implements Parcelable {
    public static final Parcelable.Creator<SoundReverbEntity> CREATOR = new Parcelable.Creator<SoundReverbEntity>() {
        @Override
        public SoundReverbEntity createFromParcel(Parcel source) {
            return new SoundReverbEntity(source);
        }

        @Override
        public SoundReverbEntity[] newArray(int size) {
            return new SoundReverbEntity[size];
        }
    };
    //效果描述
    private String effectDesc;
    //效果名称
    private String effectName;
    //图片
    private int resid;

    public SoundReverbEntity() {
    }

    protected SoundReverbEntity(Parcel in) {
        this.effectDesc = in.readString();
        this.effectName = in.readString();
        this.resid = in.readInt();
    }

    public String getEffectDesc() {
        return effectDesc;
    }

    public void setEffectDesc(String effectDesc) {
        this.effectDesc = effectDesc;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.effectDesc);
        dest.writeString(this.effectName);
        dest.writeInt(this.resid);
    }
}
