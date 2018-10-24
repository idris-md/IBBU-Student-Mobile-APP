package com.ibbumobile.model;

import android.graphics.Bitmap;

public class News {

    private final int newsID;
    private final Bitmap bitmap;
    private final String headline;
    private final String content;

    public News(int newsID, Bitmap bitmap, String headline, String content) {
        this.newsID = newsID;
        this.bitmap = bitmap;
        this.headline = headline;
        this.content = content;
    }

    public int getNewsID() {
        return newsID;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getHeadline() {
        return headline;
    }

    public String getContent() {
        return content;
    }
}
