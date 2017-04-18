package com.lee.privatecustom.entity;

/**
 * Created by 1 on 2016/12/1.
 */
public class ItemListeren {
    private int hasmore;

    private int curpage;

    private String area;

    private String id;

    private int filesize;

    private double duration;

    private int method;

    private String downurl;

    private int playcnt;

    private int ismusic;

    private String name;

    private String artist;

    private int cateid;

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public int getPlaycnt() {
        return playcnt;
    }

    public void setPlaycnt(int playcnt) {
        this.playcnt = playcnt;
    }

    public int getIsmusic() {
        return ismusic;
    }

    public void setIsmusic(int ismusic) {
        this.ismusic = ismusic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCateid() {
        return cateid;
    }

    public void setCateid(int cateid) {
        this.cateid = cateid;
    }

    @Override
    public String toString() {
        return "ItemListeren{" +
                "hasmore=" + hasmore +
                ", curpage=" + curpage +
                ", area='" + area + '\'' +
                ", id='" + id + '\'' +
                ", filesize=" + filesize +
                ", duration=" + duration +
                ", method=" + method +
                ", downurl='" + downurl + '\'' +
                ", playcnt=" + playcnt +
                ", ismusic=" + ismusic +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", cateid=" + cateid +
                '}';
    }
}
