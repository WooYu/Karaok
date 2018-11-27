package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AlbumDetailResponse extends BaseResponse {

    /**
     * albumGiftList : [{"flower_num":5,"send_uid":1,"coin_num":39,"user_nickname":"赵玉帅","works_id":1,"flower_or_gift":2,"user_photo":"2017030615082329333081.jpg"}]
     * total_coin_num : 86
     * albumWorksList : [{"comment_sum":0,"collect_sum":0,"works_second":18,"listen_count":39,"song_id":1,"share_sum":0,"works_name":"十二月的奇迹（中）","album_image":"111.jpg","album_name":"哈哈","user_nickname":"赵玉帅","gift_sum":0,"works_id":51,"works_desc":"你猜","user_photo":"2017030615082329333081.jpg","works_sum":0,"chorus_with":0,"works_image":"2017051118380337573003.png","works_score":88,"works_size":3879516,"works_url":"2017051118380354588600.mp4","phone_type":"iPhone","album_introduce":"范德萨","works_type":1,"works_level":"A","chorus_count":0,"user_id":1,"album_id":1,"add_time":"2016-07-14 14:37:04"}]
     * albumCommentList : [{"works_name":"小幸运（中）","user_id":31,"parent_id":0,"user_nickname":"舞","works_id":50,"comment_content":"hahaha","id":43,"add_time":"2017-05-24 11:07:56","user_photo":"2017050314531635508255.jpg","works_image":"2017051118041729641146.png"}]
     * total_flower_num : 5
     */


    private int total_coin_num;
    private int total_flower_num;
    private ArrayList<AlbumGiftListEntity> albumGiftList;
    private ArrayList<SongInfoBean> albumWorksList;
    private ArrayList<AlbumCommentListBean> albumCommentList;
    /**
     * attentionTypeAlbum : 1
     */

    private int attentionTypeAlbum;

    public int getTotal_coin_num() {
        return total_coin_num;
    }

    public void setTotal_coin_num(int total_coin_num) {
        this.total_coin_num = total_coin_num;
    }

    public int getTotal_flower_num() {
        return total_flower_num;
    }

    public void setTotal_flower_num(int total_flower_num) {
        this.total_flower_num = total_flower_num;
    }

    public ArrayList<AlbumGiftListEntity> getAlbumGiftList() {
        return albumGiftList;
    }

    public void setAlbumGiftList(ArrayList<AlbumGiftListEntity> albumGiftList) {
        this.albumGiftList = albumGiftList;
    }

    public ArrayList<SongInfoBean> getAlbumWorksList() {
        return albumWorksList;
    }

    public void setAlbumWorksList(ArrayList<SongInfoBean> albumWorksList) {
        this.albumWorksList = albumWorksList;
    }

    public ArrayList<AlbumCommentListBean> getAlbumCommentList() {
        return albumCommentList;
    }

    public void setAlbumCommentList(ArrayList<AlbumCommentListBean> albumCommentList) {
        this.albumCommentList = albumCommentList;
    }

    public int getAttentionTypeAlbum() {
        return attentionTypeAlbum;
    }

    public void setAttentionTypeAlbum(int attentionTypeAlbum) {
        this.attentionTypeAlbum = attentionTypeAlbum;
    }

    public static class AlbumCommentListBean {
        /**
         * works_name : 小幸运（中）
         * user_id : 31
         * parent_id : 0
         * user_nickname : 舞
         * works_id : 50
         * comment_content : hahaha
         * id : 43
         * add_time : 2017-05-24 11:07:56
         * user_photo : 2017050314531635508255.jpg
         * works_image : 2017051118041729641146.png
         */

        private String works_name;
        private int user_id;
        private int parent_id;
        private String user_nickname;
        private int works_id;
        private String comment_content;
        private int id;
        private String add_time;
        private String user_photo;
        private String works_image;

        public String getWorks_name() {
            return works_name;
        }

        public void setWorks_name(String works_name) {
            this.works_name = works_name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
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

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getWorks_image() {
            return works_image;
        }

        public void setWorks_image(String works_image) {
            this.works_image = works_image;
        }
    }
}
