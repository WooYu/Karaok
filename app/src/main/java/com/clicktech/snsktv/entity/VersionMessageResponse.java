package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by Administrator on 2017/4/25.
 * android 检查有无新版本
 */

public class VersionMessageResponse extends BaseResponse {


    /**
     * version : {"add_time":"2017-04-19 15:46:18","app_content":"最新","app_status":0,"app_url":"111.apk","app_version":"2","id":2,"is_force":1,"update_type":1}
     */

    private VersionBean version;

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class VersionBean {
        /**
         * add_time : 2017-04-19 15:46:18
         * app_content : 最新
         * app_status : 0
         * app_url : 111.apk
         * app_version : 2
         * id : 2
         * is_force : 1
         * update_type : 1
         */

        private String add_time;
        private String app_content;
        private int app_status;
        private String app_url;
        private String app_version;
        private int id;
        private int is_force;
        private int update_type;

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getApp_content() {
            return app_content;
        }

        public void setApp_content(String app_content) {
            this.app_content = app_content;
        }

        public int getApp_status() {
            return app_status;
        }

        public void setApp_status(int app_status) {
            this.app_status = app_status;
        }

        public String getApp_url() {
            return app_url;
        }

        public void setApp_url(String app_url) {
            this.app_url = app_url;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_force() {
            return is_force;
        }

        public void setIs_force(int is_force) {
            this.is_force = is_force;
        }

        public int getUpdate_type() {
            return update_type;
        }

        public void setUpdate_type(int update_type) {
            this.update_type = update_type;
        }
    }
}
