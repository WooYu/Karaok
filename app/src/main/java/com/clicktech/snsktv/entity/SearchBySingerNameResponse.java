package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SearchBySingerNameResponse extends BaseResponse {


    private List<SongListBean> songList;

    public List<SongListBean> getSongList() {
        return songList;
    }

    public void setSongList(List<SongListBean> songList) {
        this.songList = songList;
    }

    public static class SongListBean {
        /**
         * singer_name_cn : 汪峰
         * is_new : 1
         * prelude_second : 20
         * song_name_us : rising girl（英）
         * sing_count : 1
         * singer_name_jp : 汪峰（日）
         * lyric_url : risinggirl.lrc
         * song_name_jp : rising girl（日）
         * singer_id : 1
         * song_id : 3
         * singer_name_us : WF
         * grade_file_url : null
         * accompany_size : 3096
         * song_name_cn : rising girl（中）
         * song_image : 111.jpg
         * mv_url : risinggirl.mp3
         * original_url : risinggirl.mp3
         * accompany_url : risinggirl.mp3
         */

        private String singer_name_cn;
        private int is_new;
        private int prelude_second;
        private String song_name_us;
        private int sing_count;
        private String singer_name_jp;
        private String lyric_url;
        private String song_name_jp;
        private int singer_id;
        private int song_id;
        private String singer_name_us;
        private Object grade_file_url;
        private int accompany_size;
        private String song_name_cn;
        private String song_image;
        private String mv_url;
        private String original_url;
        private String accompany_url;

        public String getSinger_name_cn() {
            return singer_name_cn;
        }

        public void setSinger_name_cn(String singer_name_cn) {
            this.singer_name_cn = singer_name_cn;
        }

        public int getIs_new() {
            return is_new;
        }

        public void setIs_new(int is_new) {
            this.is_new = is_new;
        }

        public int getPrelude_second() {
            return prelude_second;
        }

        public void setPrelude_second(int prelude_second) {
            this.prelude_second = prelude_second;
        }

        public String getSong_name_us() {
            return song_name_us;
        }

        public void setSong_name_us(String song_name_us) {
            this.song_name_us = song_name_us;
        }

        public int getSing_count() {
            return sing_count;
        }

        public void setSing_count(int sing_count) {
            this.sing_count = sing_count;
        }

        public String getSinger_name_jp() {
            return singer_name_jp;
        }

        public void setSinger_name_jp(String singer_name_jp) {
            this.singer_name_jp = singer_name_jp;
        }

        public String getLyric_url() {
            return lyric_url;
        }

        public void setLyric_url(String lyric_url) {
            this.lyric_url = lyric_url;
        }

        public String getSong_name_jp() {
            return song_name_jp;
        }

        public void setSong_name_jp(String song_name_jp) {
            this.song_name_jp = song_name_jp;
        }

        public int getSinger_id() {
            return singer_id;
        }

        public void setSinger_id(int singer_id) {
            this.singer_id = singer_id;
        }

        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        public String getSinger_name_us() {
            return singer_name_us;
        }

        public void setSinger_name_us(String singer_name_us) {
            this.singer_name_us = singer_name_us;
        }

        public Object getGrade_file_url() {
            return grade_file_url;
        }

        public void setGrade_file_url(Object grade_file_url) {
            this.grade_file_url = grade_file_url;
        }

        public int getAccompany_size() {
            return accompany_size;
        }

        public void setAccompany_size(int accompany_size) {
            this.accompany_size = accompany_size;
        }

        public String getSong_name_cn() {
            return song_name_cn;
        }

        public void setSong_name_cn(String song_name_cn) {
            this.song_name_cn = song_name_cn;
        }

        public String getSong_image() {
            return song_image;
        }

        public void setSong_image(String song_image) {
            this.song_image = song_image;
        }

        public String getMv_url() {
            return mv_url;
        }

        public void setMv_url(String mv_url) {
            this.mv_url = mv_url;
        }

        public String getOriginal_url() {
            return original_url;
        }

        public void setOriginal_url(String original_url) {
            this.original_url = original_url;
        }

        public String getAccompany_url() {
            return accompany_url;
        }

        public void setAccompany_url(String accompany_url) {
            this.accompany_url = accompany_url;
        }
    }
}
