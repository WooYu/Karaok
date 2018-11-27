package com.clicktech.snsktv.util;

import android.content.Context;
import android.support.annotation.IntRange;
import android.text.TextUtils;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.arms.BuildConfig;
import com.clicktech.snsktv.arms.utils.ConstUtils;
import com.clicktech.snsktv.arms.utils.ConvertUtils;
import com.clicktech.snsktv.arms.utils.EmptyUtils;
import com.clicktech.snsktv.common.KtvApplication;
import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.entity.UserInfoBean;
import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.regex.Pattern;

/**
 * 处理String的类
 */
public class StringHelper {

    /**
     * 处理歌曲三国语言展示
     *
     * @param ja 日文
     * @param en 英文
     * @param zh 中文
     * @return
     */
    public static String getLau_With_J_U_C(String ja, String en, String zh) {

        KtvApplication application = KtvApplication.ktvApplication;
        String local = application.getLocaleCode();
        if (local.equals(application.getString(R.string.language_japan))) {
            return TextUtils.isEmpty(ja) ? TextUtils.isEmpty(zh) ? TextUtils.isEmpty(en) ? "" : en : zh : ja;

        } else if (local.equals(application.getString(R.string.language_china))) {
            return TextUtils.isEmpty(zh) ? TextUtils.isEmpty(ja) ? TextUtils.isEmpty(en) ? "" : en : ja : zh;

        } else if (local.equals(application.getString(R.string.language_english))) {
            return TextUtils.isEmpty(en) ? TextUtils.isEmpty(ja) ? TextUtils.isEmpty(zh) ? "" : zh : ja : en;
        }
        return TextUtils.isEmpty(ja) ? TextUtils.isEmpty(zh) ? TextUtils.isEmpty(en) ? "" : en : zh : ja;

    }

    //获取歌曲详情的请求id
    public static String getIDOfSongDetail(SongInfoBean songInfoBean) {
        if (EmptyUtils.isNotEmpty(songInfoBean.getWorks_id())) {
            return songInfoBean.getWorks_id();
        }
        if (EmptyUtils.isNotEmpty(songInfoBean.getId())) {
            return songInfoBean.getId();
        }
        if (EmptyUtils.isNotEmpty(songInfoBean.getSong_id())) {
            return songInfoBean.getId();
        }
        return "";
    }

    //获取用于接口请求的code
    public static String getLanguageCodeForInterface() {
        KtvApplication application = KtvApplication.ktvApplication;
        String local = application.getLocaleCode();
        if (local.equals(application.getString(R.string.language_japan))) {
            return application.getString(R.string.language_code_japan);

        } else if (local.equals(application.getString(R.string.language_china))) {
            return application.getString(R.string.language_code_china);

        } else if (local.equals(application.getString(R.string.language_english))) {
            return application.getString(R.string.language_code_english);
        }
        return "";
    }

    //获取排序索引
    public static String getSortIndex(String jpname, String enname, String cnname, String pingjia) {
        KtvApplication application = KtvApplication.ktvApplication;
        String local = application.getLocaleCode();
        if (local.equals(application.getString(R.string.language_japan))) {
            if (EmptyUtils.isNotEmpty(pingjia)) {
                return getSortIndexByPing(pingjia);
            }
            if (EmptyUtils.isNotEmpty(jpname)) {
                return getSortIndexByName(jpname);
            }
            if (EmptyUtils.isNotEmpty(enname)) {
                return getSortIndexByName(enname);
            }
            if (EmptyUtils.isNotEmpty(cnname)) {
                return getSortIndexByName(cnname);
            }

        } else if (local.equals(application.getString(R.string.language_china))) {
            if (EmptyUtils.isNotEmpty(cnname)) {
                return getSortIndexByName(cnname);
            }
            if (EmptyUtils.isNotEmpty(pingjia)) {
                return getSortIndexByPing(pingjia);
            }
            if (EmptyUtils.isNotEmpty(jpname)) {
                return getSortIndexByName(jpname);
            }
            if (EmptyUtils.isNotEmpty(enname)) {
                return getSortIndexByName(enname);
            }

        } else if (local.equals(application.getString(R.string.language_english))) {
            if (EmptyUtils.isNotEmpty(enname)) {
                return getSortIndexByName(enname);
            }
            if (EmptyUtils.isNotEmpty(pingjia)) {
                return getSortIndexByPing(pingjia);
            }
            if (EmptyUtils.isNotEmpty(jpname)) {
                return getSortIndexByName(jpname);
            }
            if (EmptyUtils.isNotEmpty(cnname)) {
                return getSortIndexByName(cnname);
            }
        }

        return KtvApplication.ktvApplication.getString(R.string.symbol_other);
    }

    //根据平假名获取日语首字母
    private static String getSortIndexByPing(String pingjia) {
        String pjm1 = "[あいうえお]";
        String pjm2 = "[かきくけこ]";
        String pjm3 = "[さしすせそ]";
        String pjm4 = "[たちつてと]";
        String pjm5 = "[なにぬねの]";
        String pjm6 = "[はひふへほ]";
        String pjm7 = "[まみむめも]";
        String pjm8 = "[やゆよ]";
        String pjm9 = "[らりるれろ]";
        String pjm10 = "[わを]";
        String pjm11 = "[ん]";

        pingjia = pingjia.substring(0, 1);
        if (pingjia.matches(pjm1)) {
            return "あ";
        } else if (pingjia.matches(pjm2)) {
            return "か";
        } else if (pingjia.matches(pjm3)) {
            return "さ";
        } else if (pingjia.matches(pjm4)) {
            return "た";
        } else if (pingjia.matches(pjm5)) {
            return "な";
        } else if (pingjia.matches(pjm6)) {
            return "は";
        } else if (pingjia.matches(pjm7)) {
            return "ま";
        } else if (pingjia.matches(pjm8)) {
            return "や";
        } else if (pingjia.matches(pjm9)) {
            return "ら";
        } else if (pingjia.matches(pjm10)) {
            return "わ";
        } else if (pingjia.matches(pjm11)) {
            return "ん";
        } else {
            return KtvApplication.ktvApplication.getString(R.string.symbol_other);
        }
    }

    //获取日语的哈希值，用于排序
    public static int getHashValuesByIndex(String index) {
        if (index.equals("あ")) {
            return 1;
        } else if (index.equals("か")) {
            return 2;
        } else if (index.equals("さ")) {
            return 3;
        } else if (index.equals("た")) {
            return 4;
        } else if (index.equals("な")) {
            return 5;
        } else if (index.equals("は")) {
            return 6;
        } else if (index.equals("ま")) {
            return 7;
        } else if (index.equals("や")) {
            return 8;
        } else if (index.equals("ら")) {
            return 9;
        } else if (index.equals("わ")) {
            return 10;
        } else if (index.equals("ん")) {
            return 11;
        } else {
            return 12;
        }
    }

    //根据名字来获取排序索引
    private static String getSortIndexByName(String name) {
        String firstLetter = name.substring(0, 1);
        //英文
        String regex = "[A-Z]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(firstLetter).matches()) {
            return firstLetter.toUpperCase();
        }

        //中文
        char firstLetterChar = name.toCharArray()[0];
        try {
            if (ChineseHelper.isChinese(firstLetterChar)) {
                return PinyinHelper.getShortPinyin(String.valueOf(firstLetterChar)).toUpperCase();
            } else if (ChineseHelper.isTraditionalChinese(firstLetterChar)) {
                return PinyinHelper.getShortPinyin(String.valueOf(
                        ChineseHelper.convertToSimplifiedChinese(firstLetterChar))).toUpperCase();
            }
        } catch (PinyinException e) {
            e.printStackTrace();
            return getSortIndexByPing(firstLetter);
        }

        //日文
        return getSortIndexByPing(firstLetter);
    }

    /**
     * 转换kb到合适单位
     *
     * @param filesize
     * @param oldunit
     * @return
     */
    public static String getFileSize(Context context, String filesize, ConstUtils.MemoryUnit oldunit) {
        long convertsize = EmptyUtils.isEmpty(filesize) ? 0 : Long.parseLong(filesize);
        //转换成字节大小
        long byteSize = ConvertUtils.memorySize2Byte(convertsize, oldunit);
        //转换成MB单位
        double mbSize = ConvertUtils.byte2MemorySize(byteSize, ConstUtils.MemoryUnit.MB);
        return String.format(context.getString(R.string.format_filesize), mbSize + 0.005);
    }

    /**
     * 根据排名获取图片（前三个是奖杯，后7个是数字）
     *
     * @param position
     * @return
     */
    public static int getImageResourceByRanking(@IntRange(from = 0, to = 9) int position) {
        int resid = 0;
        switch (position) {
            case 0:
                resid = R.mipmap.nation_golde_medal;
                break;
            case 1:
                resid = R.mipmap.nation_silver_medal;
                break;
            case 2:
                resid = R.mipmap.nation_bronze_medal;
                break;
            case 3:
                resid = R.mipmap.ranking_4;
                break;
            case 4:
                resid = R.mipmap.ranking_5;
                break;
            case 5:
                resid = R.mipmap.ranking_6;
                break;
            case 6:
                resid = R.mipmap.ranking_7;
                break;
            case 7:
                resid = R.mipmap.ranking_8;
                break;
            case 8:
                resid = R.mipmap.ranking_9;
                break;
            case 9:
                resid = R.mipmap.ranking_10;
                break;
        }
        return resid;
    }

    /**
     * 根据排名获取图片(全部是数字）
     *
     * @param position
     * @return
     */
    public static int getImageResource2ByRanking(@IntRange(from = 0, to = 9) int position) {
        int resid = 0;
        switch (position) {
            case 0:
                resid = R.mipmap.ranking_1;
                break;
            case 1:
                resid = R.mipmap.ranking_2;
                break;
            case 2:
                resid = R.mipmap.ranking_3;
                break;
            case 3:
                resid = R.mipmap.ranking_4;
                break;
            case 4:
                resid = R.mipmap.ranking_5;
                break;
            case 5:
                resid = R.mipmap.ranking_6;
                break;
            case 6:
                resid = R.mipmap.ranking_7;
                break;
            case 7:
                resid = R.mipmap.ranking_8;
                break;
            case 8:
                resid = R.mipmap.ranking_9;
                break;
            case 9:
                resid = R.mipmap.ranking_10;
                break;
        }
        return resid;
    }

    //获取图片路径
    public static String getImageUrl(String imgurl) {
        if (EmptyUtils.isEmpty(imgurl)) {
            return "";
        }
        if (imgurl.startsWith("https") || imgurl.startsWith("http") || imgurl.startsWith("www")) {
            return imgurl;
        } else {
            return BuildConfig.APP_DOMAIN_File + imgurl;
        }
    }

    //获取登录方式
    public static int getLoginMode(UserInfoBean userInfoBean) {
        if (EmptyUtils.isNotEmpty(userInfoBean.getThird_facebook())) {
            return R.string.thirdtype_fb;
        } else if (EmptyUtils.isNotEmpty(userInfoBean.getThird_line())) {
            return R.string.thirdtype_ln;
        } else if (EmptyUtils.isNotEmpty(userInfoBean.getThird_twitter())) {
            return R.string.loginmode_tw;
        } else if (EmptyUtils.isNotEmpty(userInfoBean.getThird_yahoo())) {
            return R.string.loginmode_yh;
        } else if (EmptyUtils.isNotEmpty(userInfoBean.getThird_qq())) {
            return R.string.loginmode_qq;
        } else if (EmptyUtils.isNotEmpty(userInfoBean.getThird_wx())) {
            return R.string.loginmode_wx;
        } else if (EmptyUtils.isNotEmpty(userInfoBean.getThird_wb())) {
            return R.string.loginmode_wb;
        }
        return R.string.app_name;
    }

    /**
     * 获取作品时长
     *
     * @param worksecond 单位秒
     * @return
     */
    public static String getWorkTimeBySecond(Context context, String worksecond) {
        if (EmptyUtils.isEmpty(worksecond)) {
            return "";
        }
        int time = Integer.parseInt(worksecond);
        int minute = 0;
        if (time > 60) {
            minute = time / 60;
            time %= 60;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(minute);
        stringBuilder.append(context.getString(R.string.symbol_unit_minute));
        stringBuilder.append(time);
        stringBuilder.append(context.getString(R.string.symbol_unit_second));
        return stringBuilder.toString();
    }
}
