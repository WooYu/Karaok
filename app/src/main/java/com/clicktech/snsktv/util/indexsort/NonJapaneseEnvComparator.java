package com.clicktech.snsktv.util.indexsort;

import com.clicktech.snsktv.entity.SongInfoBean;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class NonJapaneseEnvComparator implements Comparator<SongInfoBean> {

    public int compare(SongInfoBean o1, SongInfoBean o2) {
        String index1 = o1.getFirst_letter();
        String index2 = o2.getFirst_letter();
        if (index1.equals("#") && !index2.equals("#")) {
            return 1;
        } else if (!index1.equals("#") && index2.equals("#")) {
            return -1;
        } else {
            return index1.compareToIgnoreCase(index2);
        }
    }

}
