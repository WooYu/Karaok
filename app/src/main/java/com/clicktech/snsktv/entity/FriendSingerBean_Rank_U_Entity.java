package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 好友排行榜  好友bean
 */

public class FriendSingerBean_Rank_U_Entity {
    /**
     * 参数名	类型	说明
     * number	int	周播放次数
     * id	long	用户id
     * is_lead	int	0不是主唱 1是主唱
     * user_age	int	年龄
     * user_district_dtl	String	区域名字
     * user_district_id	String	地区id
     * user_nickname	String	用户昵称
     * user_photo	String	用户头像
     * user_sex	int	0女 1男
     */

    private String
            number,//	int	周播放次数
            id,//long	用户id
            is_lead,//int	0不是主唱 1是主唱
            user_age,//	int	年龄
            user_district_dtl,//	String	区域名字
            user_district_id,//String	地区id
            user_nickname,//	String	用户昵称
            user_photo,//	String	用户头像
            user_sex;//int	0女 1男


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_lead() {
        return is_lead;
    }

    public void setIs_lead(String is_lead) {
        this.is_lead = is_lead;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
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

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }
}
