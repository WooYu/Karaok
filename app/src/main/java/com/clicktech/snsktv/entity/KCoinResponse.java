package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

/**
 * Created by Administrator on 2017/6/5.
 */

public class KCoinResponse extends BaseResponse {

    /**
     * account : {"level":0,"user_photo":"2017030615082329333081.jpg","wallet_flower":7,"user_nickname":"人名1","wallet_coin":465}
     */

    private AccountBean account;

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public static class AccountBean {
        /**
         * level : 0
         * user_photo : 2017030615082329333081.jpg
         * wallet_flower : 7
         * user_nickname : 人名1
         * wallet_coin : 465
         */

        private int level;
        private String user_photo;
        private int wallet_flower;
        private String user_nickname;
        private int wallet_coin;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public int getWallet_flower() {
            return wallet_flower;
        }

        public void setWallet_flower(int wallet_flower) {
            this.wallet_flower = wallet_flower;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public int getWallet_coin() {
            return wallet_coin;
        }

        public void setWallet_coin(int wallet_coin) {
            this.wallet_coin = wallet_coin;
        }
    }
}
