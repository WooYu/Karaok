package com.clicktech.snsktv.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2.
 * 标准的用户信息实体
 */

public class UserInfoBean implements Parcelable, Serializable {


    public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
    /**
     * add_time : 2017-03-01 19:11:34.108
     * del_status : 1
     * id : 1
     * is_lead : 0
     * language :
     * latitude :
     * login_count : 0
     * login_ip : 127.0.0.1
     * longitude :
     * third_facebook :
     * third_line :
     * third_qq : 123456
     * third_twitter :
     * third_wb :
     * third_wx :
     * third_yahoo :
     * user_age : 6
     * user_birthday : 2010-10-10
     * user_district_dtl : 中国北京
     * user_district_id :
     * user_nickname : 123456
     * user_photo :
     * user_realname :
     * user_sex : 1
     */

    private String add_time;
    private int del_status;
    private String id;
    private int is_lead;
    private String language;
    private String latitude;
    private int login_count;
    private String login_ip;
    private String longitude;
    private String third_facebook;
    private String third_line;
    private String third_qq;
    private String third_twitter;
    private String third_wb;
    private String third_wx;
    private String third_yahoo;
    private String user_age;
    private String user_birthday;
    private String user_district_dtl;
    private String user_district_id;
    private String user_nickname;
    private String user_photo;
    private String user_realname;
    private String user_no;
    private int user_sex;
    private int user_level;
    private String photo;

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.add_time = in.readString();
        this.del_status = in.readInt();
        this.id = in.readString();
        this.is_lead = in.readInt();
        this.language = in.readString();
        this.latitude = in.readString();
        this.login_count = in.readInt();
        this.login_ip = in.readString();
        this.longitude = in.readString();
        this.third_facebook = in.readString();
        this.third_line = in.readString();
        this.third_qq = in.readString();
        this.third_twitter = in.readString();
        this.third_wb = in.readString();
        this.third_wx = in.readString();
        this.third_yahoo = in.readString();
        this.user_age = in.readString();
        this.user_birthday = in.readString();
        this.user_district_dtl = in.readString();
        this.user_district_id = in.readString();
        this.user_nickname = in.readString();
        this.user_photo = in.readString();
        this.user_realname = in.readString();
        this.user_no = in.readString();
        this.user_sex = in.readInt();
        this.user_level = in.readInt();
        this.photo = in.readString();
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getDel_status() {
        return del_status;
    }

    public void setDel_status(int del_status) {
        this.del_status = del_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIs_lead() {
        return is_lead;
    }

    public void setIs_lead(int is_lead) {
        this.is_lead = is_lead;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getLogin_count() {
        return login_count;
    }

    public void setLogin_count(int login_count) {
        this.login_count = login_count;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getThird_facebook() {
        return third_facebook;
    }

    public void setThird_facebook(String third_facebook) {
        this.third_facebook = third_facebook;
    }

    public String getThird_line() {
        return third_line;
    }

    public void setThird_line(String third_line) {
        this.third_line = third_line;
    }

    public String getThird_qq() {
        return third_qq;
    }

    public void setThird_qq(String third_qq) {
        this.third_qq = third_qq;
    }

    public String getThird_twitter() {
        return third_twitter;
    }

    public void setThird_twitter(String third_twitter) {
        this.third_twitter = third_twitter;
    }

    public String getThird_wb() {
        return third_wb;
    }

    public void setThird_wb(String third_wb) {
        this.third_wb = third_wb;
    }

    public String getThird_wx() {
        return third_wx;
    }

    public void setThird_wx(String third_wx) {
        this.third_wx = third_wx;
    }

    public String getThird_yahoo() {
        return third_yahoo;
    }

    public void setThird_yahoo(String third_yahoo) {
        this.third_yahoo = third_yahoo;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_district_dtl() {
        return user_district_dtl;
    }

    public void setUser_district_dtl(String user_district_dtl) {
        this.user_district_dtl = user_district_dtl;
    }

    public String getUser_district_id() {
        return user_district_id;
    }

    public void setUser_district_id(String user_district_id) {
        this.user_district_id = user_district_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getUser_realname() {
        return user_realname;
    }

    public void setUser_realname(String user_realname) {
        this.user_realname = user_realname;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.add_time);
        dest.writeInt(this.del_status);
        dest.writeString(this.id);
        dest.writeInt(this.is_lead);
        dest.writeString(this.language);
        dest.writeString(this.latitude);
        dest.writeInt(this.login_count);
        dest.writeString(this.login_ip);
        dest.writeString(this.longitude);
        dest.writeString(this.third_facebook);
        dest.writeString(this.third_line);
        dest.writeString(this.third_qq);
        dest.writeString(this.third_twitter);
        dest.writeString(this.third_wb);
        dest.writeString(this.third_wx);
        dest.writeString(this.third_yahoo);
        dest.writeString(this.user_age);
        dest.writeString(this.user_birthday);
        dest.writeString(this.user_district_dtl);
        dest.writeString(this.user_district_id);
        dest.writeString(this.user_nickname);
        dest.writeString(this.user_photo);
        dest.writeString(this.user_realname);
        dest.writeString(this.user_no);
        dest.writeInt(this.user_sex);
        dest.writeInt(this.user_level);
        dest.writeString(this.photo);
    }
}
