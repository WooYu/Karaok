package com.clicktech.snsktv.util.indexsort;

import com.clicktech.snsktv.entity.SongInfoBean;
import com.clicktech.snsktv.util.StringHelper;

import java.util.Comparator;

/**
 * Created by wy201 on 2018-02-05.
 * 日文环境比较器
 */

public class JapaneseEnvironComparator implements Comparator<SongInfoBean> {


    @Override
    public int compare(SongInfoBean o1, SongInfoBean o2) {
        String index1 = o1.getFirst_letter();
        String index2 = o2.getFirst_letter();

        int hash1 = StringHelper.getHashValuesByIndex(index1);
        int hash2 = StringHelper.getHashValuesByIndex(index2);
        if (hash1 > hash2) {
            return 1;
        } else {
            return -1;
        }
    }
}
