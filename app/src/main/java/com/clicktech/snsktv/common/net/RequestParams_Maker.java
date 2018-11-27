package com.clicktech.snsktv.common.net;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.ScoreResultEntity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/3/9.
 * 拼接请求参数
 */

public class RequestParams_Maker implements Serializable {

    final static String AUTH = "auth";
    final static String INFO = "info";
    final static String CODE = "code";
    final static String SIGN = "sign";

    /**
     * 获取Auth字段
     *
     * @return
     */
    public static String getAuthString() {
        return KtvApplication.ktvApplication.getApiAuth();
    }

    /**
     * 获取Sign字段
     *
     * @param auth
     * @return
     */
    public static String getSignString(String auth) {
        return KtvApplication.ktvApplication.getApiSign(auth);
    }

    /**
     * 获取Code字段
     *
     * @return
     */
    public static String getCodeString() {
        /**
         * zh_CN：中文，en_US：英文，ja_JP：日文；
         */
        KtvApplication application = KtvApplication.ktvApplication;
        String local = application.getLocaleCode();
        if (local.equals(application.getString(R.string.language_japan))) {
            return "ja_JP";
        } else if (local.equals(application.getString(R.string.language_china))) {
            return "zh_CN";
        } else if (local.equals(application.getString(R.string.language_english))) {
            return "en_US";
        }
        return "ja_JP";
    }

    //*****************************1、登录***********************************************

    /**
     * 第三方登录
     *
     * @param thirdType 是	String	qq,wx,wb,fb,tw,ln,yh
     * @param thirdId   是	String	第三方唯一KEY值
     * @return
     */
    public static Map<String, String> getLoginRequest(String thirdType, String thirdId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("thirdType", thirdType);
            obj.put("thirdId", thirdId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 首次登录填写用户信息
     * <p>
     * 参数名	必选	类型	说明
     * thirdType	是	String	qq,wx,wb,fb,tw,ln,yh
     * thirdId	是	String	第三方唯一KEY值
     * nickName	是	String	昵称
     * sex	是	int	性别 0女 1男
     * birthday	是	String	生日，格式注意如上面示例
     * districtDetail	是	String	地址
     * language	是	String	语言 1日文 2中文 3英文
     *
     * @return
     */
    public static Map<String, RequestBody> getRegistUserRequest(String thirdType, String thirdId,
                                                                String nickName, String sex,
                                                                String birthday, String districtDetail,
                                                                int language) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("thirdType", thirdType);
            obj.put("thirdId", thirdId);
            obj.put("nickName", nickName);
            obj.put("sex", sex);
            obj.put("birthday", birthday);
            obj.put("districtDetail", districtDetail);
            obj.put("language", language);

            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //修改头像用的OKGo

    /**
     * 修改用户信息
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	String	用户id
     * nickName	是	String	昵称
     * sex	是	int	性别 0女 1男
     * birthday	是	String	生日，格式注意如上面示例
     * districtDetail	是	String	地址
     * language	是	String	语言 1日文 2中文 3英文
     *
     * @return
     */
    public static Map<String, RequestBody> modifyUserInfo(String userId, String nickName, int sex,
                                                          String birthday, String districtDetail, int language) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("nickName", nickName);
            obj.put("sex", sex);
            obj.put("birthday", birthday);
            obj.put("districtDetail", districtDetail);
            obj.put("language", language);

            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 安卓查询有无新版本
     */
    public static Map<String, String> getCheckVersionRequest() {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 退出
     * 参数名	必选	类型	说明
     * userId	是	String	用户ID
     */
    public static Map<String, String> getLoginOutReqeust(String userId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 获取cos上的签名
     *
     * @param userId 是	String	用户ID
     * @return
     */
    public static Map<String, String> getSignCOS(String userId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************2、歌手**********************************

    /**
     * 歌手分类列表
     */
    public static Map<String, String> getSingerCategory() {
        Map<String, String> requestParams = new HashMap<>();
        try {
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌手分类查询列表
     * 参数名	必选	类型	说明
     * categoryId	是	String	分类id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     */
    public static Map<String, String> getSingerList_WithType(String categoryId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("categoryId", categoryId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌手歌曲查询列表
     * 参数名	必选	类型	说明
     * singerId	是	String	歌手id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     */
    public static Map<String, String> getSongList_WithSingerId(String singerId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("singerId", singerId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 点击K歌获取歌曲信息
     * 参数名	必选	类型	说明
     * userId	是	String	用户id，没登陆传0
     * songId	是	String	歌曲id
     * is_practice	否	int	可以为空，为1时是练唱
     */
    public static Map<String, String> getSingerSongToKGe(String userid, String songid, int is_practice) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("songId", songid);
            jsonObject.put("userId", userid);
            jsonObject.put("is_practice", is_practice);

            paramMap.put(INFO, jsonObject.toString());
            paramMap.put(AUTH, getAuthString());
            paramMap.put(CODE, getCodeString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return paramMap;
    }

    /**
     * 歌手歌曲获取歌曲信息
     * <p>
     * 参数名	必选	类型	说明
     * songId	是	String	歌曲id
     */
    public static Map<String, String> getSongInfo_WithSongId(String songId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("songId", songId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌手歌曲获取K歌列表
     * 参数名	必选	类型	说明
     * songId	是	String	歌曲id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     * type	是	String	1收听榜，2人气榜，（未添加 3评委榜，4合唱推荐）
     */
    public static Map<String, String> getLearnSingSongWorksList(String songId, int currentPage, int pageSize, int type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("songId", songId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************3、歌曲***********************************************

    /**
     * 歌曲分类列表
     * <p>
     * 参数名	必选	类型	说明
     * type	N	String	0全部 1常用 2年代 3主题
     */
    public static Map<String, String> getSongCategory(int type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌曲分类查询列表
     * <p>
     * 参数名	必选	类型	说明
     * categoryId	是	String	分类id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     */
    public static Map<String, String> getSongList_WithCategroy(String categoryId, int currentPage
            , int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("categoryId", categoryId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌曲按年代查询列表
     * 参数名	必选	类型	说明
     * bgnYear	N	String	开始年份
     * endYear	N	String	结束年份
     * currentPage	N	String	第几页
     * pageSize	N	String	每页几条
     */
    public static Map<String, String> getSongList_WithYearsBetween(String bgnYear, String endYear
            , int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("bgnYear", bgnYear);
            obj.put("endYear", endYear);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌曲合唱列表
     * 参数名	必选	类型	说明
     * currentPage	是	int	页码
     * pageSize	是	int	每页条数
     */
    public static Map<String, String> getChorusTypeList(int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 按歌手名搜索歌曲2
     * 参数名	必选	类型	说明
     * userId	N	String	用户id
     * singerName	Y	String	歌手名
     * currentPage	Y	String	第几页
     * pageSize	Y	String	每页几条
     */
    public static Map<String, String> getSongListBySingerName(String userId, String singerName,
                                                              int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {

            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("singerName", singerName);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 按歌名搜索歌曲2
     * 参数名	必选	类型	说明
     * userId	  N      String   用户id
     * songName                     歌名
     * currentPage                  第几页
     * pageSize                    每页几条
     */
    public static Map<String, String> getSongListBySongName(String userId, String songName,
                                                            int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {

            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("songName", songName);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 搜索框快速搜索1
     * 参数名	必选	类型	说明
     * 参数名	必选	类型	说明
     * searchWord	N	String	搜索词
     * currentPage	N	String	第几页
     * pageSize	N	String	每页几条
     */
    public static Map<String, String> getQuicklySearch(String searchWord, int currentPage,
                                                       int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {

            JSONObject obj = new JSONObject();
            obj.put("searchWord", searchWord);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 歌曲按歌手分类查询
     * 参数名	必选	类型	说明
     * singerCategoryId	是	String	歌手分类id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     * allow_chorus	N	String	是否支持合唱 0不支持 1支持 不传查全部
     */
    public static Map<String, String> getWorksListBySinger(String singerCategoryId,
                                                           int currentPage, int pageSize, int allow_chorus) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("singerCategoryId", singerCategoryId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            if (-1 != allow_chorus) {
                obj.put("allow_chorus", allow_chorus);
            }
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************4、首页**********************************

    /**
     * 首页显示接口
     *
     * @param userId 是	String	用户ID，未登录传 0
     * @return
     */
    public static Map<String, String> getHome(String userId) {
        HashMap<String, String> requestParams = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            requestParams.put(INFO, jsonObject.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 首页K歌选曲页面
     * 参数名	必选	类型	说明
     * userId	是	String	用户ID，未登录传 0
     * currentPage	是	int	第几页
     * pageSize	是	int	每页几条
     * type	是	String	type为 1是热榜推荐 2是猜你喜欢 3是新歌首发
     */
    public static Map<String, String> getHomeToKGe(String userId, int currentPage, int pageSize,
                                                   String type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 首页K歌选曲轮播列表
     * 参数名	必选	类型	说明
     * userId	是	String	用户ID，未登录传 0
     */
    public static Map<String, String> getK2MusicHallHomeBanners(String userId, int type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************5、榜单***********************************************
    //*****************************6、K歌**********************************

    //K歌发布保存（弃用）

    /**
     * 收听发布的作品
     *
     * @param userId    是	long	用户ID，未登录传 0
     * @param worksId   是	long	作品id
     * @param isNotPlay 是	int	为1时不统计播放次数，其他或者为空则统计
     * @return
     */
    public static Map<String, String> getDiscoverWorkInfo(String userId, String worksId, String isNotPlay) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("isNotPlay", isNotPlay);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 评论用户作品
     * userId	是	long	用户ID
     * worksId	是	long	评论的作品ID
     * parent_id	是	long	默认0，如果是回复，则为回复的评论id
     * comment_content	是	String	评论内容
     * to_user_id	否	long	在直接回复指定人时传此参数
     */
    public static Map<String, String> getUserReviews(String userId, String worksId, String parent_id
            , String comment_content, String to_user_id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("parent_id", parent_id);
            obj.put("comment_content", comment_content);
            obj.put("to_user_id", to_user_id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(getAuthString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 评论用户作品
     * userId	是	long	用户ID
     * worksId	是	long	评论的作品ID
     * parent_id	是	long	默认0，如果是回复，则为回复的评论id
     * comment_content	是	String	评论内容
     * to_user_id	否	long	在直接回复指定人时传此参数
     */
    public static Map<String, String> getUserReviews2(String userId, long worksId, long parent_id
            , String comment_content, long to_user_id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("parent_id", parent_id);
            obj.put("comment_content", comment_content);
            obj.put("to_user_id", to_user_id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(getAuthString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 评论列表带分页
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * worksId   是	long    作品ID
     * currentPage  是	string
     * pageSize   是	string
     */
    public static Map<String, String> getWorksCommentList(String userId, String worksId,
                                                          String currentPage, String pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);

            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 创建专辑
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	String 用户ID
     * worksIds	是	String 评论的作品ID，多个逗号拼接，k歌上至少五个
     * album_name	是	String	专辑名称
     * album_introduce	是	String	专辑介绍
     *
     * @return
     */
    public static Map<String, RequestBody> getCreateAlbum(String userId, String worksIds,
                                                          String album_name, String album_introduce) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksIds", worksIds);
            obj.put("album_name", album_name);
            obj.put("album_introduce", album_introduce);

            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
            RequestBody signinfo = RequestBody.create(MultipartBody.FORM, getSignString(getAuthString()));
            requestParams.put(SIGN, signinfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 专辑列表
     * 参数名	必选	类型	说明
     * userId	是	String	用户id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     */
    public static Map<String, String> getAlbumList(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 相册发布
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	    用户ID
     *
     * @return
     */
    public static Map<String, RequestBody> getReleasedAlbums(String userId) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);

            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
            RequestBody signinfo = RequestBody.create(MultipartBody.FORM, getSignString(getAuthString()));
            requestParams.put(SIGN, signinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 相册列表
     * 参数名	必选	类型	说明
     * userId	是	String	用户id
     * currentPage	是	String	第几页
     * pageSize	是	String	每页几条
     */
    public static Map<String, String> getSingerAlbumList(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 作品--送花
     * 参数名	必选	类型	说明
     * userId	是	long	用户ID
     * worksId	是	long	作品ID
     * flowerNum	是	int	送几多
     */
    public static Map<String, String> getSendFlowers(String userId, String worksId, int flowerNum) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("flowerNum", flowerNum);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 作品--送礼物
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * worksId	是	long	作品ID
     * giftId	是	long	礼物ID
     * giftNum  是   int     送几多
     */
    public static Map<String, String> getSendGifts(String userId, String worksId, String giftId, int giftNum) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("giftId", giftId);
            obj.put("giftNum", giftNum);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 设置默认相册封面
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	long	用户ID
     * photoId	是	long	相册ID
     */
    public static Map<String, String> getSetDefualtAlbumCover(String userId, String photoId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("photoId", photoId);

            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 送礼选取礼物列表
     * 参数名	必选	类型	说明
     * userId	是	long	用户ID
     */
    public static Map<String, String> getGistList(String userId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);

            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 练唱获得经验接口
     * userId	是	long	用户ID
     * song_id	是	long	歌曲ID
     * practice_score	是	int	练唱得分
     */
    public static Map<String, String> getPracticeExperience(String userId, String song_id, int practice_score) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("song_id", song_id);
            obj.put("practice_score", practice_score);

            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //K歌发布保存-2 使用的OKGO

    /**
     * 专辑详情
     * 参数名	必选	类型	说明
     * userId	是	String	用户id
     * albumId	是	String	第几页
     */
    public static Map<String, String> getAlbumDetail(String userId, String albumId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("albumId", albumId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 作品收到礼物排行
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * worksId	是	long	作品ID
     * currentPage
     * pageSize
     */
    public static Map<String, String> getRankOfGifts(String userId, String worksId,
                                                     int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("worksId", worksId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 修改作品再次发布
     * 参数名	必选	类型	说明
     * userId	是	long	用户ID
     * works_id	是	long	作品ID
     * album_id	是	long	专辑ID
     * works_name	是	String	作品名
     * works_desc	是	String	作品描述
     * is_publish	是	int	1私密 2发布
     * phone_type	是	String	手机类型
     */
    public static Map<String, RequestBody> getPublishWorkAgain(String userId, String works_id,
                                                               String album_id, String works_name,
                                                               String works_desc, int is_publish,
                                                               String phone_type) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("works_id", works_id);
            obj.put("album_id", album_id);
            obj.put("works_name", works_name);
            obj.put("works_desc", works_desc);
            obj.put("is_publish", is_publish);
            obj.put("phone_type", phone_type);

            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
            RequestBody bodySign = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(SIGN, bodySign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 相册批量删除图片
     * 参数名	必选	类型	说明
     * userId	是	long	用户ID，只能删除本人的照片
     * photoIds	是	string	照片id，多个用‘,’隔开
     *
     * @return
     */
    public static Map<String, String> getDeleteAlbum(String userId, String photoIds) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("photoIds", photoIds);

            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 专辑--送花
     * 参数名	必选	类型	说明
     * userId	是	long	用户ID
     * albumId	是	long	专辑ID
     * flowerNum	是	int	送几多
     */
    public static Map<String, String> getSendFlowersOfAlbum(String userId, String albumId, int flowerNum) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("albumId", albumId);
            obj.put("flowerNum", flowerNum);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 专辑--送礼物
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * albumId	是	long	专辑ID
     * giftId	是	long	礼物ID
     * giftNum  是   int     送几多
     */
    public static Map<String, String> getSendGiftsOfAlbum(String userId, String albumId, String giftId, int giftNum) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("albumId", albumId);
            obj.put("giftId", giftId);
            obj.put("giftNum", giftNum);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 评论用户专辑
     * userId	是	long	用户ID
     * albumId	是	long	评论的专辑ID
     * parent_id	是	long	默认0，如果是回复，则为回复的评论id
     * comment_content	是	String	评论内容
     */
    public static Map<String, String> getAlbumReview(String userId, String albumId, String parent_id
            , String comment_content) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("albumId", albumId);
            obj.put("parent_id", parent_id);
            obj.put("comment_content", comment_content);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(getAuthString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************7、动态***********************************************
    //*****************************8、发现**********************************

    /**
     * 首页
     * 发现-首页 轮播图，人气歌手，人气歌曲
     * code	否	string	编码  zh_CN：中文，en_US：英文，ja_JP：日文；
     * userId	否	string	用户id，未登录传0
     */
    public static Map<String, String> getDiscoveryIndex(String code, String userid) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("userId", userid);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 人气歌手 榜单 (全部)
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	每页条数
     */
    public static Map<String, String> getDiscoverHotSingers(int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 人气歌曲 榜单 (全部)
     * <p>
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	每页条数
     */
    public static Map<String, String> getDiscoverHotSongs(int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 试听排行榜（歌曲）
     * <p>
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	每页条数
     */
    public static Map<String, String> getListenRankSongs(int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 财富排行榜
     * <p>
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	每页条数
     * category	是	string	新增榜："HOUR" 日榜："DAY" 总榜："TOTAL"
     */
    public static Map<String, String> getRichRanking(int currentPage, int pageSize, String category) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("category", category);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 全国榜 （周榜）
     * <p>
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	每页条数
     */
    public static Map<String, String> getNationalPopularList(int currentPage, int pageSize, String city) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("city", city);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 好友排行榜
     * 参数名	必选	类型	说明
     * id	是	long	用户id
     */
    public static Map<String, String> getFriendRank(String id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************9、我的***********************************************

    /**
     * 用户信息
     * 参数名	必选	类型	说明
     * id	是	long	用户id
     */
    public static Map<String, String> getMineUserInfo(String id, int type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 关注 - 动态
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	页面条数
     * id	是	long	用户id
     */
    public static Map<String, String> getMine_AttentionList(String id, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 我的作品
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	页面条数
     * id	是	long	用户id
     * isPublish   	否	int	发布状态：1私密 2发布 不传 默认返回 私密和发布
     */
    public static Map<String, String> getWorks(int currentPage, int pageSize,
                                               String id, int isPublish) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("id", id);
            if (0 != isPublish) {
                obj.put("type", isPublish);
            }
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 热门排行
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static Map<String, String> getMine_PopularList(int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 粉丝 列表
     * 关注 列表
     *
     * @param currentPage 是	int	当前页
     * @param pageSize    是	int	页面条数
     * @param id          是	long	当前登陆 用户id
     * @param otherUid    否	long	用户id 当前登陆用户外别人的用户id
     * @return
     */
    public static Map<String, String> getFansAndAttentList(int currentPage, int pageSize,
                                                           String id, String otherUid) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("id", id);
            if (EmptyUtils.isNotEmpty(otherUid)) {
                requestParams.put("otherUid", otherUid);
            }
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 添加/取消关注
     * 参数名	必选	类型	说明
     * userId	是	long	当前用户id
     * attentionId	是	long	取消 关注的用户id
     */
    public static Map<String, String> getAddAndCancleAttention(String userId, String attentionId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("attentionId", attentionId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 搜索用户
     * 参数名	必选	类型	说明
     * condition		是	string	查询条件 昵称 或 用户 唯一码
     */
    public static Map<String, String> getSearchUser(String condition) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("condition", condition);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 添加/取消收藏
     * 参数名	必选	类型	说明
     * userId	是	long	当前用户id
     * workId	是	long	作品id 或 专辑id
     * type	    是  int     0: 歌曲 1：专辑
     */
    public static Map<String, String> getAddAndCancleCollection(String userId, String workId, int type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("workId", workId);
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 别的用户（个人中心）
     *
     * @param currentPage 是	int	页码
     * @param pageSize    是	int	每页条数
     * @param otherUid    是	long	用户id：要查询的用户的信息
     * @param id          否	long	用户id：当前登陆用户id 未登陆，不传此id
     * @return
     */
    public static Map<String, String> getPersonInfoOfOtherUser(int currentPage, int pageSize,
                                                               String otherUid, String id, int type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            obj.put("otherUid", otherUid);
            obj.put("id", id);
            obj.put("type", type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 用户反馈
     *
     * @param userId  是	long	当前登陆用户id
     * @param content 是	int	内容
     */
    public static Map<String, RequestBody> getUserFeedBack(String userId, String content) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("content", content);
            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 搜索用户通过11位编号
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * userNo	是	string	要查找的用户11位ID
     */
    public static Map<String, String> getSearchUserByID(String userId, String userNo) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("userNo", userNo);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 创建专辑选择作品列表
     *
     * @param userId 是	int	用户id
     * @return
     */
    public static Map<String, String> getSelectWorksForAlbum(String userId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 删除本地录音
     *
     * @param userId   用户id
     * @param works_id 本地录音id
     * @return
     */
    public static Map<String, String> getDeleteLocalWorks(String userId, String works_id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("works_id", works_id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }


    //*************************10、足迹***************************************************

    /**
     * 我的合唱
     * 参数名	必选	类型	说明
     * id	是	long	用户id
     */
    public static Map<String, String> getMyChorusList(String id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 合唱参与列表
     * 参数名	必选	类型	说明
     * id	是	long	作品id
     */
    public static Map<String, String> getMyChorusInList(String id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * Kcoin
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	string	用户id
     */
    public static Map<String, String> getKCoin(String user_id) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id", user_id);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }


    /**
     * 播放历史
     * <p>
     * 参数名	必选	类型	说明
     * currentPage	是	int	页码
     * pageSize	是	int	每页条数
     * userId	是	string	用户id
     */
    public static Map<String, String> getMyPlayHistory(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 收藏
     * 参数名	必选	类型	说明
     * currentPage	是	int	页码
     * pageSize	是	int	每页条数
     * userId	是	string	用户id
     */
    public static Map<String, String> getCollectList(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 已点伴奏
     * 参数名	必选	类型	说明
     * currentPage	是	int	页码
     * pageSize	是	int	每页条数
     * userId	是	string	用户id
     */
    public static Map<String, String> getHadAccompanied(String userId, String currentPage, String pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 我的练唱
     *
     * @param userId      是	long	用户id
     * @param currentPage 是	int	页码
     * @param pageSize    是	int	每页条
     * @return
     */
    public static Map<String, String> getMyPractice(String userId, String currentPage, String pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 已点伴奏删除
     * 已点练唱删除
     * 参数名	必选	类型	说明
     * userId	是	string	用户id
     * delId	是	int	删除的数据id
     */
    public static Map<String, String> getDeletedSongOfClicked(String userId, int delId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("delId", delId);
            requestParams.put(AUTH, getAuthString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************************11、消息**********************************

    /**
     * 已关注 人 评论
     * 未关注 人 评论
     * <p>
     * 参数名	必选	类型	说明
     * currentPage	是	int	页码
     * pageSize	是	int	每页条数
     * userId	是	string	用户id
     */
    public static Map<String, String> getCommentsOfInterested(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 最近礼物
     * 参数名	必选	类型	说明
     * currentPage	是	int	当前页
     * pageSize	是	int	页面条数
     * userId	是	long	用户id
     * code	否	string	默认 英文
     */

    public static Map<String, String> getRecentGiftsList(String userId, String code, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("code", code);
            obj.put("pageSize", pageSize);
            obj.put("currentPage", currentPage);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 最近听众
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	long	当前用户id
     * currentPage
     * pageSize
     */
    public static Map<String, String> getRecentListenerList(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 查询个人消息列表
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	long	当前用户id
     * currentPage
     * pageSize
     */
    public static Map<String, String> getSysMsgList(String userId, int currentPage, int pageSize) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("currentPage", currentPage);
            obj.put("pageSize", pageSize);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 读个人消息
     *
     * @param userId    是	long	用户ID
     * @param messageId 是	String	消息id
     * @return
     */
    public static Map<String, String> getReadPersonMsg(String userId, String messageId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("messageId", messageId);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 消息设置
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	string	用户id
     * receive_type  	是   int     0所有;1接收好友消息;2不接收任何人消息；-1查询当前状态；
     */

    public static Map<String, String> getChangeMsgSetting(String user_id, String receive_type) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", user_id);
            obj.put("receive_type", receive_type);
            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 举报
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * defendantId	是	long		举报人id
     * type	是	 long 举报类型:0-骚扰 1-广告 2-诈骗 3-色情 4-暴力 5-反动 6-盗歌 7-传销 8-其他
     * description	是	long	举报描述
     *
     * @return
     */
    public static Map<String, RequestBody> getReport(String userId, String defendantId, String type, String description) {
        Map<String, RequestBody> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("defendantId", defendantId);
            obj.put("type", type);
            obj.put("description", description);


            RequestBody bodyinfo = RequestBody.create(MultipartBody.FORM, obj.toString());
            requestParams.put(INFO, bodyinfo);
            RequestBody authinfo = RequestBody.create(MultipartBody.FORM, getAuthString());
            requestParams.put(AUTH, authinfo);
            RequestBody codeinfo = RequestBody.create(MultipartBody.FORM, getCodeString());
            requestParams.put(CODE, codeinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //***************************************12、打分************************************************

    /**
     * 发布作品保存得分数据
     *
     * @param entity
     * @return
     */
    public static Map<String, String> getPublishWorksScore(ScoreResultEntity entity) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            requestParams.put(INFO, new Gson().toJson(entity));
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
            requestParams.put(SIGN, getSignString(requestParams.get(AUTH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    /**
     * 查询歌曲所有作品平均得分接口
     * <p>
     * 参数名	必选	类型	说明
     * userId	是	long	用户id
     * songId	是	long	歌曲id
     *
     * @return
     */
    public static Map<String, String> getAverageScore(String userId, String songId) {
        Map<String, String> requestParams = new HashMap<>();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("songId", songId);

            requestParams.put(INFO, obj.toString());
            requestParams.put(AUTH, getAuthString());
            requestParams.put(CODE, getCodeString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParams;
    }

    //*****************其它***********************************

}
