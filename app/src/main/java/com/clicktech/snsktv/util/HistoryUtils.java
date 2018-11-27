package com.clicktech.snsktv.util;

import android.content.Context;
import android.text.TextUtils;

import com.clicktech.snsktv.arms.utils.DataHelper;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.entity.MusicHistoryEntity;
import com.clicktech.snsktv.entity.SearchHistory;
import com.clicktech.snsktv.entity.SingerInfoEntity;
import com.clicktech.snsktv.entity.SingerListWithCatecoryResponse;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static com.clicktech.snsktv.common.ConstantConfig.SEARCH_HISTORY;
import static com.clicktech.snsktv.common.ConstantConfig.SINGER_HISTORY;
import static com.clicktech.snsktv.common.ConstantConfig.SONG_SEARCH;

/**
 * Created by wy201 on 2018-01-23.
 * 历史记录相关的工具类
 */

public class HistoryUtils {
    //***********************搜索记录************************************************

    //保存搜索的记录
    public static void putSearchHistory(Context context, SearchHistory.SearchHistoryBean value) {
        boolean isAdd = false;
        SearchHistory entity;
        entity = getNormalSearchHistory(context);
        if (entity == null) {
            entity = new SearchHistory();
        }
        //判断是否包含，包含先移除再添加
        for (int i = 0; i < entity.getSearchHistory().size(); i++) {
            if (entity.getSearchHistory().get(i).getName().equals(value.getName())) {
                entity.getSearchHistory().remove(i);
                entity.getSearchHistory().add(value);
                isAdd = true;
                break;
            }
        }

        //如果不包含,直接添加
        if (!isAdd) {
            entity.getSearchHistory().add(value);
        }

        //保存的数据超过10条，移除第一条
        while (entity.getSearchHistory().size() > 10) {
            entity.getSearchHistory().remove(0);
        }

        //重新保存数据
        DataHelper.setStringSF(context, SEARCH_HISTORY, new Gson().toJson(entity));

    }

    //倒序拿到搜索的历史记录
    public static SearchHistory getSearchHistoryReverse(Context context) {
        String userJson = DataHelper.getStringSF(context, SEARCH_HISTORY);
        if (!TextUtils.isEmpty(userJson)) {
            try {
                SearchHistory entity = new Gson().fromJson(userJson, SearchHistory.class);
                SearchHistory entitySort = new SearchHistory();
//                倒序输出
                for (int i = 0; i < entity.getSearchHistory().size(); i++) {
                    entitySort.getSearchHistory().add(entity.getSearchHistory().get(entity.getSearchHistory().size() - 1 - i));
                }
                return entitySort;

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //拿到搜索的历史记录
    private static SearchHistory getNormalSearchHistory(Context context) {
        String json = DataHelper.getStringSF(context, SEARCH_HISTORY);
        if (!TextUtils.isEmpty(json)) {
            try {
                return new Gson().fromJson(json, SearchHistory.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //清空搜索的历史记录
    public static void clearSearchHistory(Context context) {
        SearchHistory entity;
        entity = getNormalSearchHistory(context);
        if (entity == null) {
            entity = new SearchHistory();
        }
        if (EmptyUtils.isNotEmpty(entity.getSearchHistory())) {
            entity.getSearchHistory().clear();
        }
        DataHelper.setStringSF(context, SEARCH_HISTORY, new Gson().toJson(entity));
    }


    //***************************已点歌手记录****************************************************
    //保存已点歌手记录
    public static void putSingerHistory(Context context, SingerInfoEntity value) {   //如果存在 添加了的数据，将数据删除再添加

        String singerName = "";
        String addSingerName = "";
        addSingerName = StringHelper.getLau_With_J_U_C(value.getSinger_name_jp(),
                value.getSinger_name_us(), value.getSinger_name_cn());
        int index = -1;
        SingerListWithCatecoryResponse entity;
        entity = getNormalSingerHistory(context);
        if (entity == null) {
            entity = new SingerListWithCatecoryResponse();
        }

        if (entity.getSingerList().size() > 0) {
            for (int i = 0; i < entity.getSingerList().size(); i++) {
                singerName = StringHelper.getLau_With_J_U_C(entity.getSingerList().get(i).
                                getSinger_name_jp(), entity.getSingerList().get(i).getSinger_name_us(),
                        entity.getSingerList().get(i).getSinger_name_cn());
                if (singerName.equals(addSingerName)) {
                    index = i;
                    break;
                }
            }
        }

        if (index != -1) {
            entity.getSingerList().remove(index);
        }

        entity.getSingerList().add(value);

        while (entity.getSingerList().size() > 6) {
            entity.getSingerList().remove(0);
        }

        DataHelper.setStringSF(context, SINGER_HISTORY, new Gson().toJson(entity));

    }

    //拿到已点歌手的历史记录   正常输出
    public static SingerListWithCatecoryResponse getNormalSingerHistory(Context context) {
        String userJson = DataHelper.getStringSF(context, SINGER_HISTORY);
        if (!TextUtils.isEmpty(userJson)) {
            try {
                return new Gson().fromJson(userJson, SingerListWithCatecoryResponse.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //拿到已点歌手的历史记录   倒序输出
    public static SingerListWithCatecoryResponse getSingerHistoryReverse(Context context) {
        String userJson = DataHelper.getStringSF(context, SINGER_HISTORY);
        if (!TextUtils.isEmpty(userJson)) {
            try {
                SingerListWithCatecoryResponse entity = new Gson().fromJson(userJson, SingerListWithCatecoryResponse.class);
                SingerListWithCatecoryResponse entitySort = new SingerListWithCatecoryResponse();

                //倒序输出
                for (int i = 0; i < entity.getSingerList().size(); i++) {
                    entitySort.getSingerList().add(entity.getSingerList().get(entity.getSingerList().size() - 1 - i));
                }
                return entitySort;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //************************************已点歌曲的记录*****************************************
    //保存已点的歌曲信息
    public static void putMusicHistory(Context context, SongInfoBean value) {
        String musicName = "";
        String addMusicName = "";
        addMusicName = StringHelper.getLau_With_J_U_C(value.getSong_name_jp(),
                value.getSong_name_us(), value.getSong_name_cn());
        int index = -1;
        MusicHistoryEntity entity;
        entity = getNormalMusicHistory(context);
        if (entity == null) {
            entity = new MusicHistoryEntity();
        }

        if (entity.getSingerList().size() > 0)
            for (int i = 0; i < entity.getSingerList().size(); i++) {
                musicName = StringHelper.getLau_With_J_U_C(
                        entity.getSingerList().get(i).getSong_name_jp(),
                        entity.getSingerList().get(i).getSong_name_us(),
                        entity.getSingerList().get(i).getSong_name_cn());
                if (musicName.equals(addMusicName)) {
                    index = i;
                }
            }

        if (index != -1) {
            entity.getSingerList().remove(index);
        }
        entity.getSingerList().add(value);
        while (entity.getSingerList().size() > 6) {
            entity.getSingerList().remove(0);
        }
        DataHelper.setStringSF(context, SONG_SEARCH, new Gson().toJson(entity));
    }

    //拿到已点歌曲正常输出
    public static MusicHistoryEntity getNormalMusicHistory(Context context) {
        String userJson = DataHelper.getStringSF(context, SONG_SEARCH);
        if (!TextUtils.isEmpty(userJson)) {
            try {
                return new Gson().fromJson(userJson, MusicHistoryEntity.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //拿到已点歌曲信息倒序
    public static MusicHistoryEntity getMusicHistoryReverse(Context context) {
        String userJson = DataHelper.getStringSF(context, SONG_SEARCH);
        if (!TextUtils.isEmpty(userJson)) {
            try {
                MusicHistoryEntity entity1 = new Gson().fromJson(userJson, MusicHistoryEntity.class);
                MusicHistoryEntity entity2 = new MusicHistoryEntity();
                for (int i = 0; i < entity1.getSingerList().size(); i++) {
                    entity2.getSingerList().add(entity1.getSingerList().get(entity1.getSingerList().size() - 1 - i));
                }
                return entity2;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
