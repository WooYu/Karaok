package com.clicktech.snsktv.entity;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class SingerInfoEntity implements Parcelable {

    public static final Creator<SingerInfoEntity> CREATOR = new Creator<SingerInfoEntity>() {
        @Override
        public SingerInfoEntity createFromParcel(Parcel source) {
            return new SingerInfoEntity(source);
        }

        @Override
        public SingerInfoEntity[] newArray(int size) {
            return new SingerInfoEntity[size];
        }
    };
    /**
     * singer_id : 1
     * singer_name_cn : 汪峰
     * singer_name_us : WF
     * category_name : 内地男歌手
     * singer_photo : 111.jpg
     * add_time : 2016-12-22 18:05:30
     */

    private String singer_id;
    private String singer_name_cn;
    private String singer_name_us;
    private String category_name;
    private String singer_photo;
    private String add_time;
    private String singer_name_jp;
    private String singer_name_jp_ping;
    private String first_letter;

    public SingerInfoEntity() {
    }

    protected SingerInfoEntity(Parcel in) {
        this.singer_id = in.readString();
        this.singer_name_cn = in.readString();
        this.singer_name_us = in.readString();
        this.category_name = in.readString();
        this.singer_photo = in.readString();
        this.add_time = in.readString();
        this.singer_name_jp = in.readString();
        this.singer_name_jp_ping = in.readString();
        this.first_letter = in.readString();
    }

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getSinger_name_cn() {
        return singer_name_cn;
    }

    public void setSinger_name_cn(String singer_name_cn) {
        this.singer_name_cn = singer_name_cn;
    }

    public String getSinger_name_us() {
        return singer_name_us;
    }

    public void setSinger_name_us(String singer_name_us) {
        this.singer_name_us = singer_name_us;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSinger_photo() {
        return singer_photo;
    }

    public void setSinger_photo(String singer_photo) {
        this.singer_photo = singer_photo;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getSinger_name_jp() {
        return singer_name_jp;
    }

    public void setSinger_name_jp(String singer_name_jp) {
        this.singer_name_jp = singer_name_jp;
    }

    public String getSinger_name_jp_ping() {
        return singer_name_jp_ping;
    }

    public void setSinger_name_jp_ping(String singer_name_jp_ping) {
        this.singer_name_jp_ping = singer_name_jp_ping;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.singer_id);
        dest.writeString(this.singer_name_cn);
        dest.writeString(this.singer_name_us);
        dest.writeString(this.category_name);
        dest.writeString(this.singer_photo);
        dest.writeString(this.add_time);
        dest.writeString(this.singer_name_jp);
        dest.writeString(this.singer_name_jp_ping);
        dest.writeString(this.first_letter);
    }
}
