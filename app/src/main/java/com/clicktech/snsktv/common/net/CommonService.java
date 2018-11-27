package com.clicktech.snsktv.common.net;

import com.clicktech.snsktv.arms.base.BaseResponse;
import com.clicktech.snsktv.entity.AlbumDetailResponse;
import com.clicktech.snsktv.entity.AlbumResponse;
import com.clicktech.snsktv.entity.ArtworksDetailResponse;
import com.clicktech.snsktv.entity.Att_Fans_UserListResponse;
import com.clicktech.snsktv.entity.Att_ListResponse;
import com.clicktech.snsktv.entity.AverageScoreResponse;
import com.clicktech.snsktv.entity.ChorusTypeResponse;
import com.clicktech.snsktv.entity.ChrousSonged_ListResponse;
import com.clicktech.snsktv.entity.ChrousSongs_ListResponse;
import com.clicktech.snsktv.entity.CommentListResponse;
import com.clicktech.snsktv.entity.DiscoverHomeResponse;
import com.clicktech.snsktv.entity.DiscoverHotSingersResponse;
import com.clicktech.snsktv.entity.DiscoverHotSongsResponse;
import com.clicktech.snsktv.entity.DiscoverListenRankSongsResponse;
import com.clicktech.snsktv.entity.FriendSinger_Rank_UResponse;
import com.clicktech.snsktv.entity.GIftRankResponse;
import com.clicktech.snsktv.entity.GiftListResponse;
import com.clicktech.snsktv.entity.HomeShowResponse;
import com.clicktech.snsktv.entity.KCoinResponse;
import com.clicktech.snsktv.entity.LatelyListenerResponse;
import com.clicktech.snsktv.entity.ListOfWorksResponse;
import com.clicktech.snsktv.entity.ListenHistoryListResponse;
import com.clicktech.snsktv.entity.MineCollectionResponse;
import com.clicktech.snsktv.entity.MineGiftListResponse;
import com.clicktech.snsktv.entity.MineUserInfoResponse;
import com.clicktech.snsktv.entity.MsgSettingResponse;
import com.clicktech.snsktv.entity.MusicHallHomeResponse;
import com.clicktech.snsktv.entity.OthersAlbumResponse;
import com.clicktech.snsktv.entity.PopularListResponse;
import com.clicktech.snsktv.entity.PopularSingerResponse;
import com.clicktech.snsktv.entity.PopularSongResponse;
import com.clicktech.snsktv.entity.RankingResponse;
import com.clicktech.snsktv.entity.ResponseAttentonCollectResponse;
import com.clicktech.snsktv.entity.RichListResponse;
import com.clicktech.snsktv.entity.SearchBySongNameResponse;
import com.clicktech.snsktv.entity.SearchResponse;
import com.clicktech.snsktv.entity.SearchUserResponse;
import com.clicktech.snsktv.entity.SignCosResponse;
import com.clicktech.snsktv.entity.SingInfoResponse;
import com.clicktech.snsktv.entity.SingSingnerTypeResponse;
import com.clicktech.snsktv.entity.SingerAlbumResponse;
import com.clicktech.snsktv.entity.SingerListWithCatecoryResponse;
import com.clicktech.snsktv.entity.SongInfoWithSongIDResponse;
import com.clicktech.snsktv.entity.SongListWithCatecoryResponse;
import com.clicktech.snsktv.entity.SongListWithSingerIDResponse;
import com.clicktech.snsktv.entity.SongTypeResponse;
import com.clicktech.snsktv.entity.SoundHistoryListResponse;
import com.clicktech.snsktv.entity.SystemMsgResponse;
import com.clicktech.snsktv.entity.UserInfoResponse;
import com.clicktech.snsktv.entity.VersionMessageResponse;
import com.clicktech.snsktv.entity.WorksCommentListResponse;
import com.clicktech.snsktv.entity.WorksForAlbumResponse;
import com.clicktech.snsktv.entity.WorksListResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 存放通用的一些API
 */
public interface CommonService {

    //*******************1、登录***********************************************

    /**
     * 相册发布
     */
    String INSERTWORKSPHOTOSINFO = "works/app/works/insertWorksPhotosInfo";
    /**
     * K歌发布保存
     */
    String PUBLISHWORKS = "works/app/works/publishWorks2";
    //修改作品再次发布
    String EDITWORKSFORPUBLISH = "works/app/works/editWorksForPublish";
    //分享视频
    String SHARE_URL = "works/app/website.do?";

    /**
     * 第三方登录
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/user/thirdLogin")
    Observable<UserInfoResponse> getUsers(@FieldMap Map<String, String> info);

    /**
     * 首次登录填写用户信息
     *
     * @param info
     * @param part
     * @return
     */
    @Multipart
    @POST("user/app/user/saveUserInfoByThird")
    Observable<UserInfoResponse> getRegistUsers(@PartMap Map<String, RequestBody> info, @Part MultipartBody.Part part);

    //**********************2、歌手************************************************

    /**
     * 修改头像
     *
     * @param info
     * @param part
     * @return
     */
    @Multipart
    @POST("user/app/user/saveUserPhoto")
    Observable<UserInfoResponse> modifyPhoto(@PartMap Map<String, RequestBody> info, @Part MultipartBody.Part part);

    /**
     * 修改用户信息
     *
     * @param info
     * @param part
     * @return
     */
    @Multipart
    @POST("user/app/user/saveUserInfo")
    Observable<BaseResponse> modifyUserInfo(@PartMap Map<String, RequestBody> info, @Part MultipartBody.Part part);

    /**
     * 安卓查询有无新版本
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/index/appVersion")
    Observable<VersionMessageResponse> checkNewVersion(@FieldMap Map<String, String> info);

    /**
     * 退出登录
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/user/loginOut")
    Observable<BaseResponse> signOut(@FieldMap Map<String, String> info);

    /**
     * 获取COS上传sign
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/file/signCOS")
    Observable<SignCosResponse> signCOS(@FieldMap Map<String, String> info);

    /**
     * 歌手分类列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("singer/app/singer/singerCategory")
    Observable<SingSingnerTypeResponse> getSingerCategory(@FieldMap Map<String, String> info);

    //*****************3、歌曲**************************************************

    /**
     * 歌手分类查询列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("singer/app/singer/singerList")
    Observable<SingerListWithCatecoryResponse> getSingerList_WithType(@FieldMap Map<String, String> info);

    /**
     * 歌手歌曲查询列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("singer/app/singer/singerSongList")
    Observable<SongListWithSingerIDResponse> getSongList_WithSingerId(@FieldMap Map<String, String> info);

    /**
     * 去K歌获取歌曲信息
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("singer/app/singer/singerSongToKGe")
    Observable<SongInfoWithSongIDResponse> getSingerSongToKGe(@FieldMap Map<String, String> info);

    /**
     * 歌手歌曲获取歌曲信息
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("singer/app/singer/singerSongDetail")
    Observable<SongInfoWithSongIDResponse> getSongInfoWithSongID(@FieldMap Map<String, String> info);

    /**
     * 歌手歌曲获取K歌列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("singer/app/singer/singerSongWorksList")
    Observable<ListOfWorksResponse> getSingerSongWorksList(@FieldMap Map<String, String> info);

    /**
     * 歌曲分类列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songCategory")
    Observable<SongTypeResponse> getSongCategory(@FieldMap Map<String, String> info);

    /**
     * 歌曲分类查询列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songList")
    Observable<SongListWithCatecoryResponse> getSongList_WithCatecory(@FieldMap Map<String, String> info);

    /**
     * 歌曲按年代查询列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songListByYears")
    Observable<SongListWithCatecoryResponse> getSongList_WithYearsBetween(@FieldMap Map<String, String> info);

    //**************************4、首页******************************************************

    /**
     * 歌曲合唱列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songListForChorus")
    Observable<ChorusTypeResponse> getChorusList(@FieldMap Map<String, String> info);

    /**
     * 按歌名搜索歌曲2
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songListBySongName")
    Observable<SearchBySongNameResponse> serachBySongName(@FieldMap Map<String, String> info);

    /**
     * 按歌手名搜索歌曲2
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songListBySingerName")
    Observable<SearchBySongNameResponse> serachBySingerName(@FieldMap Map<String, String> info);

    //*************************5、榜单**********************************************

    //*************************6、K歌************************************************

    /**
     * 搜索框快速搜索1
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/quickSearchSingerSong")
    Observable<SearchResponse> serach(@FieldMap Map<String, String> info);

    /**
     * 歌曲按歌手分类查询
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("song/app/song/songListBySingerCategory")
    Observable<SongListWithCatecoryResponse> getWorksList(@FieldMap Map<String, String> info);

    /**
     * 首页显示接口
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/index/home")
    Observable<HomeShowResponse> getHome(@FieldMap Map<String, String> info);

    /**
     * 首页K歌选曲页面
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/index/homeToKGe")
    Observable<SingInfoResponse> getHomeToKGe(@FieldMap Map<String, String> info);

    /**
     * 首页K歌选曲轮播列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/index/homeCarouselFigure")
    Observable<MusicHallHomeResponse> getMusicHallBanners(@FieldMap Map<String, String> info);

    /**
     * 收听发布的作品
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/toListenOrWatchWorks")
    Observable<ArtworksDetailResponse> getReleaseWorkInfo(@FieldMap Map<String, String> info);

    /**
     * 评论用户作品
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/saveWorksComment")
    Observable<BaseResponse> getUserReviews(@FieldMap Map<String, String> info);

    /**
     * 评论列表带分页
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/selectWorksCommentList")
    Observable<WorksCommentListResponse> getWorksCommentList(@FieldMap Map<String, String> info);

    /**
     * 创建专辑
     *
     * @param info
     * @param part
     * @return
     */
    @Multipart
    @POST("works/app/works/insertWorksAlbumInfo")
    Observable<BaseResponse> albumProduct(@PartMap Map<String, RequestBody> info, @Part MultipartBody.Part part);

    /**
     * 专辑列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/selectWorksAlbumList")
    Observable<AlbumResponse> getAlbumList(@FieldMap Map<String, String> info);

    /**
     * 相册发布
     *
     * @param info
     * @param part
     * @return
     */
    @Multipart
    @POST("works/app/works/insertWorksPhotosInfo")
    Observable<BaseResponse> getReleasedAlbums(@PartMap Map<String, RequestBody> info, @Part MultipartBody.Part part);

    //TODO:设置默认相册封面

    /**
     * 相册列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/selectWorksPhotosList")
    Observable<SingerAlbumResponse> getSingerAlbumList(@FieldMap Map<String, String> info);

    //TODO:练唱获得经验接口

    /**
     * 作品--送花
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("gift/app/gift/sendingFlowers")
    Observable<BaseResponse> givingFlowers(@FieldMap Map<String, String> info);

    /**
     * 送礼物
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("gift/app/gift/sendingGifts")
    Observable<BaseResponse> givingGift(@FieldMap Map<String, String> info);

    /**
     * 送礼选取礼物列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("gift/app/gift/giftList")
    Observable<GiftListResponse> giftList(@FieldMap Map<String, String> info);

    /**
     * 专辑详情
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/selectWorksAlbumDetail")
    Observable<AlbumDetailResponse> getAlbumDetail(@FieldMap Map<String, String> info);

    /**
     * 作品收到礼物排行
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/worksGiftListTop")
    Observable<GIftRankResponse> giftRank(@FieldMap Map<String, String> info);

    /**
     * 相册批量删除图片
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/deleteWorksPhotos")
    Observable<BaseResponse> deleteAlbum(@FieldMap Map<String, String> info);

    /**
     * 专辑-送花
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("gift/app/gift/sendingAlbumFlowers")
    Observable<BaseResponse> sendingAlbumFlowers(@FieldMap Map<String, String> info);

    /**
     * 专辑-送礼物
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("gift/app/gift/sendingAlbumGifts")
    Observable<BaseResponse> sendingAlbumGifts(@FieldMap Map<String, String> info);

    //*************************7、动态****************************************************

    //*************************8、发现****************************************************

    /**
     * 评论用户专辑
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/saveAlbumComment")
    Observable<BaseResponse> saveAlbumComment(@FieldMap Map<String, String> info);

    /**
     * 首页
     * 发现-首页 轮播图，人气歌手，人气歌曲
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/discoveryIndex")
    Observable<DiscoverHomeResponse> getDiscoveryHomeIndex(@FieldMap Map<String, String> info);

    /**
     * 人气歌手 榜单 (全部)
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/popularSinger")
    Observable<DiscoverHotSingersResponse> getHotSingers(@FieldMap Map<String, String> info);

    /**
     * 人气歌手 榜单 (全部)新
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/popularSinger")
    Observable<PopularSingerResponse> getPopSingers(@FieldMap Map<String, String> info);

    /**
     * 人气歌曲 榜单(全部) 新
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/popularSong")
    Observable<PopularSongResponse> getPopSongs(@FieldMap Map<String, String> info);

    /**
     * 人气歌曲 榜单(全部)
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/popularSong")
    Observable<DiscoverHotSongsResponse> getHotSongs(@FieldMap Map<String, String> info);

    /**
     * 试听排行榜（歌曲）
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/auditionCharts")
    Observable<DiscoverListenRankSongsResponse> getListenRankSongs(@FieldMap Map<String, String> info);

    /**
     * 财富排行榜
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/fortuneList")
    Observable<RichListResponse> getDiacoverRichList(@FieldMap Map<String, String> info);

    /**
     * 全国榜 （周榜）
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/popularSingerForCountry")
    Observable<RankingResponse> getDiacoverNationalPopularList(@FieldMap Map<String, String> info);

    //******************************9、我的*******************************************************

    /**
     * 好友排行榜
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/attentionCharts")
    Observable<FriendSinger_Rank_UResponse> getFriendSingersRankList(@FieldMap Map<String, String> info);

    /**
     * 用户信息
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/indexUserInfo")
    Observable<MineUserInfoResponse> getMineUserInfo(@FieldMap Map<String, String> info);

    /**
     * 关注-动态
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/myAttention")
    Observable<Att_ListResponse> getMine_AttentionList(@FieldMap Map<String, String> info);

    /**
     * 我的作品
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/myWorks")
    Observable<WorksListResponse> getMine_MyWorksList(@FieldMap Map<String, String> info);

    /**
     * 热门
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/popularSongForWeek")
    Observable<PopularListResponse> getMine_PopularHot_List(@FieldMap Map<String, String> info);

    /**
     * 粉丝列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/myFans")
    Observable<Att_Fans_UserListResponse> getMine_Fans_User_List(@FieldMap Map<String, String> info);

    /**
     * 关注列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/attentionList")
    Observable<Att_Fans_UserListResponse> getMine_Att_User_List(@FieldMap Map<String, String> info);

    /**
     * 添加关注
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/addAttention")
    Observable<ResponseAttentonCollectResponse> getMine_AddAtten(@FieldMap Map<String, String> info);

    /**
     * 取消关注
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/delAttention")
    Observable<ResponseAttentonCollectResponse> getMine_CancleAtten(@FieldMap Map<String, String> info);

    /**
     * 搜索用户
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/searchUser")
    Observable<SearchUserResponse> searchUser(@FieldMap Map<String, String> info);

    /**
     * 添加收藏
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/addWorksStore")
    Observable<ResponseAttentonCollectResponse> addCollect(@FieldMap Map<String, String> info);

    /**
     * 取消收藏
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/cancelWorksStore")
    Observable<ResponseAttentonCollectResponse> cancleCollect(@FieldMap Map<String, String> info);

    //TODO:用户反馈

    //TODO：搜索用户通过11位编号

    /**
     * 别的用户（个人中心） 第一页
     *
     * @param info
     * @return
     */

    @FormUrlEncoded
    @POST("user/app/personalCenter/indexOtherUser")
    Observable<OthersAlbumResponse> getOtherUserInfo(@FieldMap Map<String, String> info);

    //创建专辑选择作品列表
    @FormUrlEncoded
    @POST("works/app/works/selectWorksForAlbum")
    Observable<WorksForAlbumResponse> getSelectWorksForAlbum(@FieldMap Map<String, String> info);
    //******************************************10、足迹*********************************************

    //删除本地录音
    @FormUrlEncoded
    @POST("works/app/works/deleteLocalWorks")
    Observable<BaseResponse> getDeleteLocalWorks(@FieldMap Map<String, String> info);

    /**
     * 我的合唱
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/myChorus")
    Observable<ChrousSongs_ListResponse> getMine_MyChrousSongsList(@FieldMap Map<String, String> info);

    /**
     * 合唱参与列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/chorusList")
    Observable<ChrousSonged_ListResponse> getMine_ChrousSongedList(@FieldMap Map<String, String> info);

    /**
     * K币
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/myKAccount")
    Observable<KCoinResponse> getkCoin(@FieldMap Map<String, String> info);

    /**
     * 播放历史
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/worksHistory")
    Observable<ListenHistoryListResponse> getMine_MyListenHistoryList(@FieldMap Map<String, String> info);

    /**
     * 收藏
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/worksStore")
    Observable<MineCollectionResponse> getMine_MyCollectionList(@FieldMap Map<String, String> info);

    /**
     * 已点伴奏
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/myAccompany")
    Observable<SoundHistoryListResponse> getMine_MySoundHistoryList(@FieldMap Map<String, String> info);

    //******************************11、消息*****************************************

    /**
     * 已点伴奏删除
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/personalCenter/myAccompanyDel")
    Observable<BaseResponse> getMyAccompanyDel(@FieldMap Map<String, String> info);

    /**
     * 已关注人评论
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/worksCommentByConcerned")
    Observable<CommentListResponse> getWorksCommentByConcernedList(@FieldMap Map<String, String> info);

    /**
     * 未关注人评论
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/worksCommentByUnConcerned")
    Observable<CommentListResponse> getWorksCommentByUnConcernedList(@FieldMap Map<String, String> info);

    /**
     * 最近礼物
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("gift/app/gift/recentGift")
    Observable<MineGiftListResponse> getMineGiftList(@FieldMap Map<String, String> info);

    /**
     * 最近听众接口
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/worksListenerCurrent")
    Observable<LatelyListenerResponse> latelyListen(@FieldMap Map<String, String> info);

    //TODO:读个人消息

    /**
     * 查询个人消息列表
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/message/messageList")
    Observable<SystemMsgResponse> getSystemMsgList(@FieldMap Map<String, String> info);

    /**
     * 消息设置
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("user/app/message/jpushSetting")
    Observable<MsgSettingResponse> msgSetting(@FieldMap Map<String, String> info);


    //**********************************12、打分************************

    /**
     * 举报
     *
     * @param info
     * @return
     */
    @Multipart
    @POST("user/app/personalCenter/inform")
    Observable<BaseResponse> report(@PartMap Map<String, RequestBody> info, @Part MultipartBody.Part part);

    /**
     * 发布作品保存得分数据
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/publishWorksScore")
    Observable<BaseResponse> publishWorksScore(@FieldMap Map<String, String> info);

    //******************************13、其它**********************************

    /**
     * 查询歌曲所有作品平均得分接口
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("works/app/works/songWorksAverageScore")
    Observable<AverageScoreResponse> getAverageScore(@FieldMap Map<String, String> info);

    /**
     * 现在上传和下载使用的是OkGo
     * 下载文件，通过动态的地址
     *
     * @param fileUrl
     * @return
     */
    @Deprecated()
    @GET
    Observable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
