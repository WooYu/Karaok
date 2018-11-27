package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 * 作品收到礼物排行
 */

public class GIftRankResponse extends BaseResponse {


    /**
     * authorInfo : {"total_coin":66,"works_name":"小幸运（日）","total_flower":45,"works_desc":"我刚唱了一首歌曲，快来听听吧","author_id":7,"works_image":"sy_004.jpg"}
     * mineGift : {"mine_coin":66,"mine_flower":43}
     * worksGiftList : [{"flower_num":43,"send_uid":1,"coin_num":66,"user_nickname":"赵玉帅","works_id":6,"user_photo":"2017030615082329333081.jpg"},{"flower_num":2,"send_uid":31,"coin_num":0,"user_nickname":"舞","works_id":6,"user_photo":"2017050314531635508255.jpg"}]
     */

    private AuthorInfoBean authorInfo;
    private MineGiftBean mineGift;
    private List<AlbumGiftListEntity> worksGiftList;

    public AuthorInfoBean getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfoBean authorInfo) {
        this.authorInfo = authorInfo;
    }

    public MineGiftBean getMineGift() {
        return mineGift;
    }

    public void setMineGift(MineGiftBean mineGift) {
        this.mineGift = mineGift;
    }

    public List<AlbumGiftListEntity> getWorksGiftList() {
        return worksGiftList;
    }

    public void setWorksGiftList(List<AlbumGiftListEntity> worksGiftList) {
        this.worksGiftList = worksGiftList;
    }

    public static class AuthorInfoBean {
        /**
         * total_coin : 66
         * works_name : 小幸运（日）
         * total_flower : 45
         * works_desc : 我刚唱了一首歌曲，快来听听吧
         * author_id : 7
         * works_image : sy_004.jpg
         */

        private int total_coin;
        private String works_name;
        private int total_flower;
        private String works_desc;
        private int author_id;
        private String works_image;
        private String user_photo;
        private String user_nickname;

        public int getTotal_coin() {
            return total_coin;
        }

        public void setTotal_coin(int total_coin) {
            this.total_coin = total_coin;
        }

        public String getWorks_name() {
            return works_name;
        }

        public void setWorks_name(String works_name) {
            this.works_name = works_name;
        }

        public int getTotal_flower() {
            return total_flower;
        }

        public void setTotal_flower(int total_flower) {
            this.total_flower = total_flower;
        }

        public String getWorks_desc() {
            return works_desc;
        }

        public void setWorks_desc(String works_desc) {
            this.works_desc = works_desc;
        }

        public int getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(int author_id) {
            this.author_id = author_id;
        }

        public String getWorks_image() {
            return works_image;
        }

        public void setWorks_image(String works_image) {
            this.works_image = works_image;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }
    }

    public static class MineGiftBean {
        /**
         * mine_coin : 66
         * mine_flower : 43
         */

        private int mine_coin;
        private int mine_flower;

        public int getMine_coin() {
            return mine_coin;
        }

        public void setMine_coin(int mine_coin) {
            this.mine_coin = mine_coin;
        }

        public int getMine_flower() {
            return mine_flower;
        }

        public void setMine_flower(int mine_flower) {
            this.mine_flower = mine_flower;
        }
    }

    public static class WorksGiftListBean {
        /**
         * flower_num : 43
         * send_uid : 1
         * coin_num : 66
         * user_nickname : 赵玉帅
         * works_id : 6
         * user_photo : 2017030615082329333081.jpg
         */

        private int flower_num;
        private int send_uid;
        private int coin_num;
        private String user_nickname;
        private int works_id;
        private String user_photo;

        public int getFlower_num() {
            return flower_num;
        }

        public void setFlower_num(int flower_num) {
            this.flower_num = flower_num;
        }

        public int getSend_uid() {
            return send_uid;
        }

        public void setSend_uid(int send_uid) {
            this.send_uid = send_uid;
        }

        public int getCoin_num() {
            return coin_num;
        }

        public void setCoin_num(int coin_num) {
            this.coin_num = coin_num;
        }

        public String getUser_nickname() {
            return user_nickname;
        }

        public void setUser_nickname(String user_nickname) {
            this.user_nickname = user_nickname;
        }

        public int getWorks_id() {
            return works_id;
        }

        public void setWorks_id(int works_id) {
            this.works_id = works_id;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }
    }
}
