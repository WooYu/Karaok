package com.clicktech.snsktv.util.indexsort;

import com.clicktech.snsktv.entity.SingerInfoEntity;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class NonJapanEnvirSingerComparator implements Comparator<SingerInfoEntity> {

    public int compare(SingerInfoEntity o1, SingerInfoEntity o2) {
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
