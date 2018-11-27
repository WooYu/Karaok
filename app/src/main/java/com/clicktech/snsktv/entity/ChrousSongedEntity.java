package com.clicktech.snsktv.entity;

/**
 * Created by Administrator on 2017/1/19.
 * 我的--我的合唱--参与列表
 */

public class ChrousSongedEntity {
    /**
     * 参数名	类型	说明
     * id	long	作品id
     * works_desc	string	作品描述
     * user_photo	string	用户头像
     * user_district_dtl	string	区域名字
     * user_nickname	string	用户昵称
     * add_time	string	添加时间
     * user_sex	int	性别
     * works_score	int	作品得分
     */

    private String

            id,//long	作品id
            works_desc,//	string	作品描述
            user_photo,//	string	用户头像
            user_district_dtl,//	string	区域名字
            user_nickname,//string	用户昵称
            add_time,//string	添加时间
            user_sex,//	int	性别
            works_score;//	int	作品得分

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorks_desc() {
        return works_desc;
    }

    public void setWorks_desc(String works_desc) {
        this.works_desc = works_desc;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getUser_district_dtl() {
        return user_district_dtl;
    }

    public void setUser_district_dtl(String user_district_dtl) {
        this.user_district_dtl = user_district_dtl;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getWorks_score() {
        return works_score;
    }

    public void setWorks_score(String works_score) {
        this.works_score = works_score;
    }
}
