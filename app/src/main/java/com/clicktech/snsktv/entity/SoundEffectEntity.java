package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/14.
 * 变音
 */

public class SoundEffectEntity implements Parcelable {
    public static final Parcelable.Creator<SoundEffectEntity> CREATOR = new Parcelable.Creator<SoundEffectEntity>() {
        @Override
        public SoundEffectEntity createFromParcel(Parcel source) {
            return new SoundEffectEntity(source);
        }

        @Override
        public SoundEffectEntity[] newArray(int size) {
            return new SoundEffectEntity[size];
        }
    };
    private int resid;
    private String effectname;
    private String effectimg;
    private boolean selected;

    public SoundEffectEntity() {
    }

    protected SoundEffectEntity(Parcel in) {
        this.resid = in.readInt();
        this.effectname = in.readString();
        this.effectimg = in.readString();
        this.selected = in.readByte() != 0;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public String getEffectname() {
        return effectname;
    }

    public void setEffectname(String effectname) {
        this.effectname = effectname;
    }

    public String getEffectimg() {
        return effectimg;
    }

    public void setEffectimg(String effectimg) {
        this.effectimg = effectimg;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resid);
        dest.writeString(this.effectname);
        dest.writeString(this.effectimg);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }
}
