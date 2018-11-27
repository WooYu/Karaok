package com.clicktech.snsktv.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-22.
 * mv的配置
 * 效果名称‘效果说明’效果图‘效果配置
 */

public class MVConfig implements Serializable {
    private int configname;
    private String configdesc;
    private int effectpicture;
    private String config;
    private int layouttype;
    private boolean openbackcamera;
    private int level;

    public int getConfigname() {
        return configname;
    }

    public void setConfigname(int configname) {
        this.configname = configname;
    }

    public String getConfigdesc() {
        return configdesc;
    }

    public void setConfigdesc(String configdesc) {
        this.configdesc = configdesc;
    }

    public int getEffectpicture() {
        return effectpicture;
    }

    public void setEffectpicture(int effectpicture) {
        this.effectpicture = effectpicture;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public int getLayouttype() {
        return layouttype;
    }

    public void setLayouttype(int layouttype) {
        this.layouttype = layouttype;
    }

    public boolean isOpenbackcamera() {
        return openbackcamera;
    }

    public void setOpenbackcamera(boolean openbackcamera) {
        this.openbackcamera = openbackcamera;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
