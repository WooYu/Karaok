package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 标准的专辑实体
 */
public class AlbumBean implements Parcelable {


    public static final Parcelable.Creator<AlbumBean> CREATOR = new Parcelable.Creator<AlbumBean>() {
        public AlbumBean createFromParcel(Parcel source) {
            return new AlbumBean(source);
        }

        public AlbumBean[] newArray(int size) {
            return new AlbumBean[size];
        }
    };
    /**
     * 专辑
     * album_introduce : 很不错
     * comment_sum : 0
     * collect_sum : 0
     * share_sum : 0
     * user_id : 1
     * album_image :
     * album_name : hh
     * user_nickname : 123456
     * gift_sum : 0
     * id : 3
     * add_time : 2017-03-15 16:26:58
     * user_photo : 2017030615082329333081.jpg
     * works_sum : 4
     */

    private String album_introduce;
    private String comment_sum;
    private String collect_sum;
    private String share_sum;
    private String user_id;
    private String album_image;
    private String album_name;
    private String user_nickname;
    private String gift_sum;
    private String id;
    private String add_time;
    private String user_photo;
    private String songNum;
    private String works_sum;

    public AlbumBean() {
    }

    protected AlbumBean(Parcel in) {
        this.album_introduce = in.readString();
        this.comment_sum = in.readString();
        this.collect_sum = in.readString();
        this.share_sum = in.readString();
        this.user_id = in.readString();
        this.album_image = in.readString();
        this.album_name = in.readString();
        this.user_nickname = in.readString();
        this.gift_sum = in.readString();
        this.id = in.readString();
        this.add_time = in.readString();
        this.user_photo = in.readString();
        this.songNum = in.readString();
        this.works_sum = in.readString();
    }

    public String getAlbum_introduce() {
        return album_introduce;
    }

    public void setAlbum_introduce(String album_introduce) {
        this.album_introduce = album_introduce;
    }

    public String getComment_sum() {
        return comment_sum;
    }

    public void setComment_sum(String comment_sum) {
        this.comment_sum = comment_sum;
    }

    public String getCollect_sum() {
        return collect_sum;
    }

    public void setCollect_sum(String collect_sum) {
        this.collect_sum = collect_sum;
    }

    public String getShare_sum() {
        return share_sum;
    }

    public void setShare_sum(String share_sum) {
        this.share_sum = share_sum;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getGift_sum() {
        return gift_sum;
    }

    public void setGift_sum(String gift_sum) {
        this.gift_sum = gift_sum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getSongNum() {
        return songNum;
    }

    public void setSongNum(String songNum) {
        this.songNum = songNum;
    }

    public String getWorks_sum() {
        return works_sum;
    }

    public void setWorks_sum(String works_sum) {
        this.works_sum = works_sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.album_introduce);
        dest.writeString(this.comment_sum);
        dest.writeString(this.collect_sum);
        dest.writeString(this.share_sum);
        dest.writeString(this.user_id);
        dest.writeString(this.album_image);
        dest.writeString(this.album_name);
        dest.writeString(this.user_nickname);
        dest.writeString(this.gift_sum);
        dest.writeString(this.id);
        dest.writeString(this.add_time);
        dest.writeString(this.user_photo);
        dest.writeString(this.songNum);
        dest.writeString(this.works_sum);
    }
}
