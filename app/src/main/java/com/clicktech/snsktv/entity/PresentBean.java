package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wy201 on 2018-02-07.
 * 礼物实体
 */

public class PresentBean implements Parcelable {
    public static final Parcelable.Creator<PresentBean> CREATOR = new Parcelable.Creator<PresentBean>() {
        @Override
        public PresentBean createFromParcel(Parcel source) {
            return new PresentBean(source);
        }

        @Override
        public PresentBean[] newArray(int size) {
            return new PresentBean[size];
        }
    };
    private String add_time;
    private String gift_image;
    private String gift_name;
    private int gift_price;
    private int id;
    private String update_time;

    public PresentBean() {
    }

    protected PresentBean(Parcel in) {
        this.add_time = in.readString();
        this.gift_image = in.readString();
        this.gift_name = in.readString();
        this.gift_price = in.readInt();
        this.id = in.readInt();
        this.update_time = in.readString();
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public int getGift_price() {
        return gift_price;
    }

    public void setGift_price(int gift_price) {
        this.gift_price = gift_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.add_time);
        dest.writeString(this.gift_image);
        dest.writeString(this.gift_name);
        dest.writeInt(this.gift_price);
        dest.writeInt(this.id);
        dest.writeString(this.update_time);
    }
}
