package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 * 好友排行榜
 */

public class FriendSinger_Rank_UResponse extends BaseResponse {
    private FriendSinger_Rank_U_Entity_inside attentionCharts;

    public FriendSinger_Rank_U_Entity_inside getAttentionCharts() {
        return attentionCharts;
    }

    public void setAttentionCharts(FriendSinger_Rank_U_Entity_inside attentionCharts) {
        this.attentionCharts = attentionCharts;
    }

    public class FriendSinger_Rank_U_Entity_inside {
        /**
         * 本周、上周、2周前，3周前，一个月前
         * week0,week1,week2,week3,week4
         */
        private List<FriendSingerBean_Rank_U_Entity> week0;
        private List<FriendSingerBean_Rank_U_Entity> week1;
        private List<FriendSingerBean_Rank_U_Entity> week2;
        private List<FriendSingerBean_Rank_U_Entity> week3;
        private List<FriendSingerBean_Rank_U_Entity> week4;

        public List<FriendSingerBean_Rank_U_Entity> getWeek0() {
            return week0;
        }

        public void setWeek0(List<FriendSingerBean_Rank_U_Entity> week0) {
            this.week0 = week0;
        }

        public List<FriendSingerBean_Rank_U_Entity> getWeek1() {
            return week1;
        }

        public void setWeek1(List<FriendSingerBean_Rank_U_Entity> week1) {
            this.week1 = week1;
        }

        public List<FriendSingerBean_Rank_U_Entity> getWeek2() {
            return week2;
        }

        public void setWeek2(List<FriendSingerBean_Rank_U_Entity> week2) {
            this.week2 = week2;
        }

        public List<FriendSingerBean_Rank_U_Entity> getWeek3() {
            return week3;
        }

        public void setWeek3(List<FriendSingerBean_Rank_U_Entity> week3) {
            this.week3 = week3;
        }

        public List<FriendSingerBean_Rank_U_Entity> getWeek4() {
            return week4;
        }

        public void setWeek4(List<FriendSingerBean_Rank_U_Entity> week4) {
            this.week4 = week4;
        }

    }
}
