package com.lee.privatecustom.entity;

import java.util.List;

/**
 * Created by 1 on 2016/11/23.
 */
public class RootAnim {
    private List<NavAnim> nav ;
    private List<ItemAnim> list ;
    public List<NavAnim> getNav() {
        return nav;
    }

    public void setNav(List<NavAnim> nav) {
        this.nav = nav;
    }

    public List<ItemAnim> getList() {
        return list;
    }

    public void setList(List<ItemAnim> list) {
        this.list = list;
    }
}