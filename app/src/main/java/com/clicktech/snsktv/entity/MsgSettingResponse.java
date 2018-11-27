package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MsgSettingResponse extends BaseResponse {

    /**
     * ps : {"id":1,"live_broadcast_message":1,"receive_type":2,"sound_default":1,"user_id":1}
     */

    private PsBean ps;

    public PsBean getPs() {
        return ps;
    }

    public void setPs(PsBean ps) {
        this.ps = ps;
    }

    public static class PsBean {
        /**
         * id : 1
         * live_broadcast_message : 1
         * receive_type : 2
         * sound_default : 1
         * user_id : 1
         */

        private int id;
        private int live_broadcast_message;
        private int receive_type;
        private int sound_default;
        private int user_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLive_broadcast_message() {
            return live_broadcast_message;
        }

        public void setLive_broadcast_message(int live_broadcast_message) {
            this.live_broadcast_message = live_broadcast_message;
        }

        public int getReceive_type() {
            return receive_type;
        }

        public void setReceive_type(int receive_type) {
            this.receive_type = receive_type;
        }

        public int getSound_default() {
            return sound_default;
        }

        public void setSound_default(int sound_default) {
            this.sound_default = sound_default;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
