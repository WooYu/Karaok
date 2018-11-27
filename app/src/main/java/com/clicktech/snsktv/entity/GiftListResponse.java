package com.clicktech.snsktv.entity;


import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.ArrayList;

public class GiftListResponse extends BaseResponse {


    /**
     * add_time : 2016-12-23 15:04:11
     * gift_image : 111.jpg
     * gift_name : 苹果
     * gift_price : 6
     * id : 1
     * update_time : 2016-12-23 15:04:14
     */

    private ArrayList<PresentBean> giftList;
    /**
     * wallet : {"id":1,"level":0,"update_time":"2016-12-23 11:33:58","user_id":1,"wallet_coin":474,"wallet_flower":61}
     */

    private WalletBean wallet;

    public ArrayList<PresentBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(ArrayList<PresentBean> giftList) {
        this.giftList = giftList;
    }

    public WalletBean getWallet() {
        return wallet;
    }

    public void setWallet(WalletBean wallet) {
        this.wallet = wallet;
    }

    public static class WalletBean {
        /**
         * id : 1
         * level : 0
         * update_time : 2016-12-23 11:33:58
         * user_id : 1
         * wallet_coin : 474
         * wallet_flower : 61
         */

        private int id;
        private int level;
        private String update_time;
        private int user_id;
        private int wallet_coin;
        private int wallet_flower;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getWallet_coin() {
            return wallet_coin;
        }

        public void setWallet_coin(int wallet_coin) {
            this.wallet_coin = wallet_coin;
        }

        public int getWallet_flower() {
            return wallet_flower;
        }

        public void setWallet_flower(int wallet_flower) {
            this.wallet_flower = wallet_flower;
        }
    }
}
