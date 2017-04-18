package com.lee.privatecustom.entity;

/**
 * Created by ipaynow0323 on 2016/11/9.
 */

public class Item {

    private int hasmore;

    private int curpage;

    private String area;

    private String id;

    private String restype;

    private int filesize;

    private double duration;

    private int method;

    private String downurl;

    private String pic;

    private int playcnt;

    private int ismusic;

    private String name;

    private String artist;

    private int cateid;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setRestype(String restype){
        this.restype = restype;
    }
    public String getRestype(){
        return this.restype;
    }
    public void setFilesize(int filesize){
        this.filesize = filesize;
    }
    public int getFilesize(){
        return this.filesize;
    }
    public void setDuration(double duration){
        this.duration = duration;
    }
    public double getDuration(){
        return this.duration;
    }
    public void setMethod(int method){
        this.method = method;
    }
    public int getMethod(){
        return this.method;
    }
    public void setDownurl(String downurl){
        this.downurl = downurl;
    }
    public String getDownurl(){
        return this.downurl;
    }
    public void setPic(String pic){
        this.pic = pic;
    }
    public String getPic(){
        return this.pic;
    }
    public void setPlaycnt(int playcnt){
        this.playcnt = playcnt;
    }
    public int getPlaycnt(){
        return this.playcnt;
    }
    public void setIsmusic(int ismusic){
        this.ismusic = ismusic;
    }
    public int getIsmusic(){
        return this.ismusic;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }
    public String getArtist(){
        return this.artist;
    }
    public void setCateid(int cateid){
        this.cateid = cateid;
    }
    public int getCateid(){
        return this.cateid;
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
    public void setArea(String area){
        this.area = area;
    }
    public String getArea(){
        return this.area;
    }

}
