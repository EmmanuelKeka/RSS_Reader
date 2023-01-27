package com.example.rss_reader;

import android.graphics.Bitmap;
import android.media.Image;

public class Feed {
    private String title;
    private String description;
    private String pubDate;
    private String link;
    private Bitmap image;
    private String imageLink;
    private String UserId;


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getUserId() {
        return UserId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
