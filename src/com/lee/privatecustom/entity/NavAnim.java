package com.lee.privatecustom.entity;

/**
 * Created by 1 on 2016/11/23.
 */
public class NavAnim {
    private int hasmore;
    private int curpage;
    private int id;
    private String name;
    private int method;
    private String isLeaf;
    public int getHasmore() {
        return hasmore;
    }
    public void setHasmore(int hasmore) {
        this.hasmore = hasmore;
    }
    public int getCurpage() {
        return curpage;
    }
    public void setCurpage(int curpage) {
        this.curpage = curpage;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMethod() {
        return method;
    }
    public void setMethod(int method) {
        this.method = method;
    }
    public String getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }
    @Override
    public String toString() {
        return "Nav{" +
                "hasmore=" + hasmore +
                ", curpage=" + curpage +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", method=" + method +
                ", isLeaf='" + isLeaf + '\'' +
                '}';
    }
}
