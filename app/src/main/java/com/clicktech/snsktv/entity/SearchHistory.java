package com.clicktech.snsktv.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SearchHistory {

    private List<SearchHistoryBean> searchHistory = new ArrayList<>();

    public List<SearchHistoryBean> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(List<SearchHistoryBean> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public static class SearchHistoryBean {
        /**
         * name : 汪峰
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
