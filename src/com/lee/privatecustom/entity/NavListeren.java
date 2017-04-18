package com.lee.privatecustom.entity;

/**
 * Created by 1 on 2016/12/1.
 */
public class NavListeren {
    private int hasmore;

    private int curpage;

    private int id;

    private String restype;

    private String name;

    private int method;

    private String pic;

    private int hasseq;

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

    public String getRestype() {
        return restype;
    }

    public void setRestype(String restype) {
        this.restype = restype;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getHasseq() {
        return hasseq;
    }

    public void setHasseq(int hasseq) {
        this.hasseq = hasseq;
    }

    @Override
    public String toString() {
        return "NavListeren{" +
                "hasmore=" + hasmore +
                ", curpage=" + curpage +
                ", id=" + id +
                ", restype='" + restype + '\'' +
                ", name='" + name + '\'' +
                ", method=" + method +
                ", pic='" + pic + '\'' +
                ", hasseq=" + hasseq +
                '}';
    }
}
