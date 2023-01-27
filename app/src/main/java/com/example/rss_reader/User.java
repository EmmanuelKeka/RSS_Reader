package com.example.rss_reader;

public class User {
    private Boolean isSub;
    private String rssLink;
    private String Email;
    private String lastNewListed;

    public User(String email,Boolean isSub, String rssLink) {
        this.isSub = isSub;
        this.rssLink = rssLink;
        Email = email;
    }
    public User(){

    }

    public Boolean getSub() {
        return isSub;
    }

    public void setSub(Boolean sub) {
        isSub = sub;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLastNewListed() {
        return lastNewListed;
    }
    public void setLastNewListed(String lastNewListed) {
        this.lastNewListed = lastNewListed;
    }
}
