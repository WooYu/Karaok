package com.library.multimedia.lyricscontrols.parser;

import android.graphics.Color;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认的歌词解析器
 */
public class DefaultLrcParser implements ILrcParser {
    private static final DefaultLrcParser istance = new DefaultLrcParser();
    final String FLAG_RED = "(A)";
    final String FLAG_BLUE = "(B)";
    final String FLAG_GREEN = "(AB)";
    final int LRCCOLOR_RED = Color.rgb(229, 0, 79);
    final int LRCCOLOR_BLUE = Color.rgb(0, 189, 218);
    final int LRCCOLOR_GREEN = Color.rgb(27, 212, 66);
    int mCurTextColor = Color.WHITE;
    boolean mSwitchColor = false;
    int mCurRoleType = 0;//0：代表歌名、1：代表演唱红色、2：代表演唱蓝色、3：代表合唱

    private DefaultLrcParser() {
    }

    public static final DefaultLrcParser getIstance() {
        return istance;
    }

    /***
     * 将歌词文件里面的字符串 解析成一个List<LrcRow>
     */
    @Override
    public List<LrcRow> getLrcRows(String str) {

        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BufferedReader br = new BufferedReader(new StringReader(str));

        List<LrcRow> lrcRows = new ArrayList<>();
        String lrcLine;
        try {
            while ((lrcLine = br.readLine()) != null) {
                List<LrcRow> rows = createRows(lrcLine);
                if (rows != null && rows.size() > 0) {
                    lrcRows.addAll(rows);
                }
            }
            Collections.sort(lrcRows);
            int len = lrcRows.size();
            for (int i = 0; i < len - 1; i++) {
                lrcRows.get(i).setTotalTime(lrcRows.get(i + 1).getTime() - lrcRows.get(i).getTime());
            }
            lrcRows.get(len - 1).setTotalTime(5000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lrcRows;
    }

    /**
     * 将歌词文件中的某一行 解析成一个List<LrcRow>
     * 1、因为一行中可能包含了多个LrcRow对象
     * 比如  [03:33.02][00:36.37]当鸽子不再象征和平  ，就包含了2个对象
     * 2、不包含歌词名称
     *
     * @param lrcLine
     * @return
     */
    public List<LrcRow> createRows(String lrcLine) {
        if (!lrcLine.startsWith("[") || lrcLine.indexOf("]") != 9) {
            return null;
        }

        //最后一个"]"
        int lastIndexOfRightBracket = lrcLine.lastIndexOf("]");
        //歌词内容
        String content = lrcLine.substring(lastIndexOfRightBracket + 1, lrcLine.length());
        //截取出歌词时间，并将"[" 和"]" 替换为"-"   [offset:0]
        System.out.println("lrcLine=" + lrcLine);
        // -03:33.02--00:36.37-
        String times = lrcLine.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
        String[] timesArray = times.split("-");
        List<LrcRow> lrcRows = new ArrayList<>();

        for (String tem : timesArray) {
            if (TextUtils.isEmpty(tem.trim())) {
                continue;
            }
            try {
                //单行Flag时不添加到最终解析结果中
                if (content.equals(FLAG_RED)) {
                    mCurTextColor = LRCCOLOR_RED;
                    mCurRoleType = 1;
                    mSwitchColor = true;
                    return null;
                } else if (content.equals(FLAG_BLUE)) {
                    mCurTextColor = LRCCOLOR_BLUE;
                    mCurRoleType = 2;
                    mSwitchColor = true;
                    return null;
                } else if (content.equals(FLAG_GREEN)) {
                    mCurTextColor = LRCCOLOR_GREEN;
                    mCurRoleType = 3;
                    mSwitchColor = true;
                    return null;
                }

                if (content.contains(FLAG_RED)) {
                    mCurTextColor = LRCCOLOR_RED;
                    mCurRoleType = 1;
                    mSwitchColor = true;
                    content = content.replace(FLAG_RED, "");
                } else if (content.contains(FLAG_BLUE)) {
                    mCurTextColor = LRCCOLOR_BLUE;
                    mCurRoleType = 2;
                    mSwitchColor = true;
                    content = content.replace(FLAG_BLUE, "");
                } else if (content.contains(FLAG_GREEN)) {
                    mCurTextColor = LRCCOLOR_GREEN;
                    mCurRoleType = 3;
                    mSwitchColor = true;
                    content = content.replace(FLAG_GREEN, "");
                }
                int startTime = formatTime(tem);
                LrcRow lrcRow = new LrcRow(tem, startTime, content, mCurTextColor, mSwitchColor, mCurRoleType);
                mSwitchColor = false;
                lrcRows.add(lrcRow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lrcRows;
    }

    /****
     * 把歌词时间转换为毫秒值  如 将00:10.00  转为10000毫秒
     *
     * @param timeStr
     * @return
     */
    private int formatTime(String timeStr) {
        timeStr = timeStr.replace('.', ':');
        String[] times = timeStr.split(":");

        return Integer.parseInt(times[0]) * 60 * 1000
                + Integer.parseInt(times[1]) * 1000
                + Integer.parseInt(times[2]);
    }

}
