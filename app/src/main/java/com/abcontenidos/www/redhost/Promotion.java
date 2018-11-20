package com.abcontenidos.www.redhost;

import android.graphics.Bitmap;

public class Promotion {
    private String name, details, category;
    private Bitmap thumbail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Bitmap getThumbail() {
        return thumbail;
    }

    public void setThumbail(Bitmap thumbail) {
        this.thumbail = thumbail;
    }

    public Promotion(String name, String details, String category, Bitmap thumbail) {
        this.category = category;
        this.name = name;
        this.details = details;
        this.thumbail = thumbail;
    }
}
