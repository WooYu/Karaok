package com.clicktech.snsktv.entity;


/**
 * Created by Administrator on 2016/12/26.
 */

public class SingSingnerTypeBeanEntity {

    /**
     * addTime : 2016-12-22 18:07:22
     * categoryName : 内地男歌手
     * categoryType : 1
     * id : 1
     */

    private String addTime;
    private String category_name;
    private String category_type;
    private String id;

    public String getCategory_Type() {
        return category_type;
    }

    public void setCategory_Type(String addTime) {
        this.category_type = category_type;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
