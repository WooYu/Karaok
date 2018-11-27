package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/18.
 * 发布歌曲信息
 */

public class PublishInforEntity implements Parcelable {

    public static final Creator<PublishInforEntity> CREATOR = new Creator<PublishInforEntity>() {
        @Override
        public PublishInforEntity createFromParcel(Parcel source) {
            return new PublishInforEntity(source);
        }

        @Override
        public PublishInforEntity[] newArray(int size) {
            return new PublishInforEntity[size];
        }
    };
    /**
     * userId : 1
     * song_id : 1
     * album_id : 1
     * works_second : 285
     * works_size : 5869
     * works_score : 8888
     * works_level : SSS
     * works_name : 小苹果
     * works_desc : 新发，快来听
     * works_type : 0
     * is_publish : 1
     * phone_type : iPhone
     * chorus_with : 0
     */

    private String userId;
    private String song_id;
    private String album_id;
    private String works_second;
    private String works_size;
    private String works_score;
    private String works_level;
    private String works_name;
    private String works_desc;
    private int works_type;
    private int is_publish;
    private String phone_type;
    private String chorus_with;
    private String photo;
    private String works;
    private String works_url;
    private String works_mv;
    private String works_photo;
    private String works_id;

    public PublishInforEntity() {
    }

    protected PublishInforEntity(Parcel in) {
        this.userId = in.readString();
        this.song_id = in.readString();
        this.album_id = in.readString();
        this.works_second = in.readString();
        this.works_size = in.readString();
        this.works_score = in.readString();
        this.works_level = in.readString();
        this.works_name = in.readString();
        this.works_desc = in.readString();
        this.works_type = in.readInt();
        this.is_publish = in.readInt();
        this.phone_type = in.readString();
        this.chorus_with = in.readString();
        this.photo = in.readString();
        this.works = in.readString();
        this.works_url = in.readString();
        this.works_mv = in.readString();
        this.works_photo = in.readString();
        this.works_id = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getWorks_second() {
        return works_second;
    }

    public void setWorks_second(String works_second) {
        this.works_second = works_second;
    }

    public String getWorks_size() {
        return works_size;
    }

    public void setWorks_size(String works_size) {
        this.works_size = works_size;
    }

    public String getWorks_score() {
        return works_score;
    }

    public void setWorks_score(String works_score) {
        this.works_score = works_score;
    }

    public String getWorks_level() {
        return works_level;
    }

    public void setWorks_level(String works_level) {
        this.works_level = works_level;
    }

    public String getWorks_name() {
        return works_name;
    }

    public void setWorks_name(String works_name) {
        this.works_name = works_name;
    }

    public String getWorks_desc() {
        return works_desc;
    }

    public void setWorks_desc(String works_desc) {
        this.works_desc = works_desc;
    }

    public int getWorks_type() {
        return works_type;
    }

    public void setWorks_type(int works_type) {
        this.works_type = works_type;
    }

    public int getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(int is_publish) {
        this.is_publish = is_publish;
    }

    public String getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(String phone_type) {
        this.phone_type = phone_type;
    }

    public String getChorus_with() {
        return chorus_with;
    }

    public void setChorus_with(String chorus_with) {
        this.chorus_with = chorus_with;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public String getWorks_url() {
        return works_url;
    }

    public void setWorks_url(String works_url) {
        this.works_url = works_url;
    }

    public String getWorks_mv() {
        return works_mv;
    }

    public void setWorks_mv(String works_mv) {
        this.works_mv = works_mv;
    }

    public String getWorks_photo() {
        return works_photo;
    }

    public void setWorks_photo(String works_photo) {
        this.works_photo = works_photo;
    }

    public String getWorks_id() {
        return works_id;
    }

    public void setWorks_id(String works_id) {
        this.works_id = works_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.song_id);
        dest.writeString(this.album_id);
        dest.writeString(this.works_second);
        dest.writeString(this.works_size);
        dest.writeString(this.works_score);
        dest.writeString(this.works_level);
        dest.writeString(this.works_name);
        dest.writeString(this.works_desc);
        dest.writeInt(this.works_type);
        dest.writeInt(this.is_publish);
        dest.writeString(this.phone_type);
        dest.writeString(this.chorus_with);
        dest.writeString(this.photo);
        dest.writeString(this.works);
        dest.writeString(this.works_url);
        dest.writeString(this.works_mv);
        dest.writeString(this.works_photo);
        dest.writeString(this.works_id);
    }
}
