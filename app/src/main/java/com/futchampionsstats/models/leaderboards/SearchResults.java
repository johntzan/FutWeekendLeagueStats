package com.futchampionsstats.models.leaderboards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResults{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("console")
    @Expose
    private String console;
    @SerializedName("twitch")
    @Expose
    private Object twitch;
    @SerializedName("youtube")
    @Expose
    private Object youtube;
    @SerializedName("twitter")
    @Expose
    private Object twitter;
    @SerializedName("futwiz")
    @Expose
    private Object futwiz;
    @SerializedName("club")
    @Expose
    private String club;
    @SerializedName("fname")
    @Expose
    private Object fname;
    @SerializedName("nickname")
    @Expose
    private Object nickname;
    @SerializedName("lname")
    @Expose
    private Object lname;
    @SerializedName("face")
    @Expose
    private String face;
    @SerializedName("regionalid1")
    @Expose
    private String regionalid1;
    @SerializedName("regionalrank1")
    @Expose
    private String regionalrank1;
    @SerializedName("regionalid2")
    @Expose
    private String regionalid2;
    @SerializedName("regionalrank2")
    @Expose
    private String regionalrank2;
    @SerializedName("regionalid3")
    @Expose
    private String regionalid3;
    @SerializedName("regionalrank3")
    @Expose
    private String regionalrank3;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("urlname")
    @Expose
    private String urlname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public Object getTwitch() {
        return twitch;
    }

    public void setTwitch(Object twitch) {
        this.twitch = twitch;
    }

    public Object getYoutube() {
        return youtube;
    }

    public void setYoutube(Object youtube) {
        this.youtube = youtube;
    }

    public Object getTwitter() {
        return twitter;
    }

    public void setTwitter(Object twitter) {
        this.twitter = twitter;
    }

    public Object getFutwiz() {
        return futwiz;
    }

    public void setFutwiz(Object futwiz) {
        this.futwiz = futwiz;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public Object getFname() {
        return fname;
    }

    public void setFname(Object fname) {
        this.fname = fname;
    }

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public Object getLname() {
        return lname;
    }

    public void setLname(Object lname) {
        this.lname = lname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getRegionalid1() {
        return regionalid1;
    }

    public void setRegionalid1(String regionalid1) {
        this.regionalid1 = regionalid1;
    }

    public String getRegionalrank1() {
        return regionalrank1;
    }

    public void setRegionalrank1(String regionalrank1) {
        this.regionalrank1 = regionalrank1;
    }

    public String getRegionalid2() {
        return regionalid2;
    }

    public void setRegionalid2(String regionalid2) {
        this.regionalid2 = regionalid2;
    }

    public String getRegionalrank2() {
        return regionalrank2;
    }

    public void setRegionalrank2(String regionalrank2) {
        this.regionalrank2 = regionalrank2;
    }

    public String getRegionalid3() {
        return regionalid3;
    }

    public void setRegionalid3(String regionalid3) {
        this.regionalid3 = regionalid3;
    }

    public String getRegionalrank3() {
        return regionalrank3;
    }

    public void setRegionalrank3(String regionalrank3) {
        this.regionalrank3 = regionalrank3;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

}
