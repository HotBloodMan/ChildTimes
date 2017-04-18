package com.lee.privatecustom.entity;

import java.util.List;

public class Root {
    private List<Nav> nav ;

    private List<Item> list ;

    public void setNav(List<Nav> nav){
        this.nav = nav;
    }
    public List<Nav> getNav(){
        return this.nav;
    }
    public void setList(List<Item> list){
        this.list = list;
    }
    public List<Item> getList(){
        return this.list;
    }

}