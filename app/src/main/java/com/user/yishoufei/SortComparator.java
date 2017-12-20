package com.user.yishoufei;

import java.util.Comparator;

/**
 * Created by issuser on 2017/12/17.
 * 实现排序功能
 */

public class SortComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        ToDay_Trans a = (ToDay_Trans) lhs;
        ToDay_Trans b = (ToDay_Trans) rhs;

        return ((int) b.getId()-(int)a.getId());
    }
}
