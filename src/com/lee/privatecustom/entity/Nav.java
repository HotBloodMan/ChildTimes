package com.lee.privatecustom.entity;
public class Nav {
    private int hasmore;

    private int curpage;

    private int id;

    private String restype;

    private String name;

    private int method;

    private String pic;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setRestype(String restype){
        this.restype = restype;
    }
    public String getRestype(){
        return this.restype;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setMethod(int method){
        this.method = method;
    }
    public int getMethod(){
        return this.method;
    }
    public void setPic(String pic){
        this.pic = pic;
    }
    public String getPic(){
        return this.pic;
    }

    public void setHasmore(int hasmore){
        this.hasmore = hasmore;
    }
    public int getHasmore(){
        return this.hasmore;
    }
    public void setCurpage(int curpage){
        this.curpage = curpage;
    }
    public int getCurpage(){
        return this.curpage;
    }

}