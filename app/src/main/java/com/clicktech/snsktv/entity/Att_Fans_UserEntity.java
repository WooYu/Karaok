package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 我的--粉丝列表，关注列表 用户信息
 */

public class Att_Fans_UserEntity {

    /**
     * 参数名	类型	说明
     * id	long	用户id
     * user_nickname	string	用户昵称
     * user_sex	int	0女 1男
     * user_photo	string	用户头像
     * attention_type	int	0未关注 1已关注
     */

    public String
            id,//	long	用户id
            user_nickname,//	string	用户昵称
            attention_type,//	0未关注 1已关注
            user_photo,//	string	用户昵称
            user_sex;//	int	0女 1男

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getAttention_type() {
        return attention_type;
    }

    public void setAttention_type(String attention_type) {
        this.attention_type = attention_type;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }
}
