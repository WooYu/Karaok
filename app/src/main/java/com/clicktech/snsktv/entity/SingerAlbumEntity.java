package com.clicktech.snsktv.entity;


public class SingerAlbumEntity {


    /**
     * 相册 实体
     * user_id : 1
     * user_nickname : 123456
     * photo : a
     * id : 1
     * type :
     * add_time :
     * user_photo : 2017030615082329333081.jpg
     * status : 0
     */

    private int user_id;
    private String user_nickname;
    private String photo;
    private int id;
    private String type;
    private String add_time;
    private String user_photo;
    private int status;
    private boolean select;//是否被选中

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
