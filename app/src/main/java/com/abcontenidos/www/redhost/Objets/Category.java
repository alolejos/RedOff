package com.abcontenidos.www.redhost.Objets;

public class Category {
    String name, details, image, selected;
    Integer id;

    public Category(){

    }
    public Category(Integer id, String name, String details, String image, String selected) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.image = image;
        this.selected = selected;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
