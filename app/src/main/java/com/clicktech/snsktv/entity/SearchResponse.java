package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SearchResponse extends BaseResponse {


    private List<ResultListBean> resultList;
    private List<HotSearchBean> hotSearch;

    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public List<HotSearchBean> getHotSearch() {
        return hotSearch;
    }

    public void setHotSearch(List<HotSearchBean> hotSearch) {
        this.hotSearch = hotSearch;
    }

    public static class ResultListBean {
        /**
         * singer_or_song_name_us : ZJL
         * singer_or_song_id : 2
         * singer_or_song : 1
         * singer_or_song_name_jp : EXO（日）
         * singer_or_song_name_cn : EXO
         */

        private String singer_or_song_name_us;
        private String singer_or_song_id;
        private String singer_or_song;
        private String singer_or_song_name_jp;
        private String singer_or_song_name_cn;

        public String getSinger_or_song_name_us() {
            return singer_or_song_name_us;
        }

        public void setSinger_or_song_name_us(String singer_or_song_name_us) {
            this.singer_or_song_name_us = singer_or_song_name_us;
        }

        public String getSinger_or_song_id() {
            return singer_or_song_id;
        }

        public void setSinger_or_song_id(String singer_or_song_id) {
            this.singer_or_song_id = singer_or_song_id;
        }

        public String getSinger_or_song() {
            return singer_or_song;
        }

        public void setSinger_or_song(String singer_or_song) {
            this.singer_or_song = singer_or_song;
        }

        public String getSinger_or_song_name_jp() {
            return singer_or_song_name_jp;
        }

        public void setSinger_or_song_name_jp(String singer_or_song_name_jp) {
            this.singer_or_song_name_jp = singer_or_song_name_jp;
        }

        public String getSinger_or_song_name_cn() {
            return singer_or_song_name_cn;
        }

        public void setSinger_or_song_name_cn(String singer_or_song_name_cn) {
            this.singer_or_song_name_cn = singer_or_song_name_cn;
        }
    }

    public static class HotSearchBean {
        /**
         * search_word : 汪峰
         */

        private String search_word;

        public String getSearch_word() {
            return search_word;
        }

        public void setSearch_word(String search_word) {
            this.search_word = search_word;
        }
    }
}
