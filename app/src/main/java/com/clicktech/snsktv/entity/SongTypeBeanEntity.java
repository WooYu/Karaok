package com.clicktech.snsktv.entity;


/**
 * Created by Administrator on 2016/12/26.
 */

public class SongTypeBeanEntity {


    /**
     * add_time :
     * category_image : 111.jpg
     * category_name : 情歌对唱
     * category_type : 3
     * id : 5
     * language : 1
     * parent_id : 0
     * sing_count : 0
     * update_account :
     */

    private String add_time;
    private String category_image;
    private String category_name;
    private int category_type;
    private String id;
    private int language;
    private String parent_id;
    private String sing_count;
    private String update_account;

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_type() {
        return category_type;
    }

    public void setCategory_type(int category_type) {
        this.category_type = category_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSing_count() {
        return sing_count;
    }

    public void setSing_count(String sing_count) {
        this.sing_count = sing_count;
    }

    public String getUpdate_account() {
        return update_account;
    }

    public void setUpdate_account(String update_account) {
        this.update_account = update_account;
    }
}
