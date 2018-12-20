package com.abcontenidos.www.redhost;


public class User {
    String id, name, mail, token, address, gender, birthday, image, pass;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(){

    }

    public User(String name, String mail, String token, String address, String gender, String birthday, String id, String image, String pass) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.token = token;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
        this.image = image;
        this.pass = pass ;

    }


}
