package com.lee.privatecustom.entity;

import java.util.List;

/**
 * Created by 1 on 2016/12/1.
 */
public class RootListeren {
    private List<NavListeren> nav ;

    private List<ItemListeren> list ;

    public List<NavListeren> getNav() {
        return nav;
    }

    public void setNav(List<NavListeren> nav) {
        this.nav = nav;
    }

    public List<ItemListeren> getList() {
        return list;
    }

    public void setList(List<ItemListeren> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RootListeren{" +
                "nav=" + nav +
                ", list=" + list +
                '}';
    }
}
