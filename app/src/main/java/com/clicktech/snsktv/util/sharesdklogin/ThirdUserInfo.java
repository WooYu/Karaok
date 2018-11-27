package com.clicktech.snsktv.util.sharesdklogin;

import java.io.Serializable;

public class ThirdUserInfo implements Serializable {
    //    ong	getExpiresIn()
//    获取token的有效时常（单位：秒）
    private long expiresin;
    //    long	getExpiresTime()
//    获取token的有效截止时间（单位：毫秒）
    private long expirestime;
    //    java.lang.String	getPlatformNname()
//    获取平台的名称
    private String platformnname;
    //    int	getPlatformVersion()
//    获取授权账户的版本
    private int platformversion;
    //    java.lang.String	getToken()
//    获取授权账户的token
    private String token;
    //    java.lang.String	getTokenSecret()
//    获取授权账户token的secret
    private String secret;
    //    java.lang.String	getUserGender()
//    返回用户登陆账号的性别，男性为“m”，女性为“f”，如果没有此字段信息，则返回null
    private String gender;
    //    java.lang.String	getUserIcon()
//    返回用户登陆账号的头像
    private String usericon;
    //    java.lang.String	getUserId()
//    返回已经保存的用户id
    private String userid;
    //    java.lang.String	getUserName()
    private String username;

    private String thirdtype;//请求接口用
    private String loginmode;//登录模式
    private String nickname;//昵称
    private String birthday;//生日
    private String area;//地区

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getThirdtype() {
        return thirdtype;
    }

    public void setThirdtype(String thirdtype) {
        this.thirdtype = thirdtype;
    }

    public String getLoginmode() {
        return loginmode;
    }

    public void setLoginmode(String loginmode) {
        this.loginmode = loginmode;
    }

    public long getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(long expiresin) {
        this.expiresin = expiresin;
    }

    public long getExpirestime() {
        return expirestime;
    }

    public void setExpirestime(long expirestime) {
        this.expirestime = expirestime;
    }

    public String getPlatformnname() {
        return platformnname;
    }

    public void setPlatformnname(String platformnname) {
        this.platformnname = platformnname;
    }

    public int getPlatformversion() {
        return platformversion;
    }

    public void setPlatformversion(int platformversion) {
        this.platformversion = platformversion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
