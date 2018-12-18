package com.abcontenidos.www.redhost;

public class Post {
    String id, name, details, category, image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Post(String id, String name, String details, String category, String image) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.category = category;
        this.image = image;
    }

    public Post(){}
}
