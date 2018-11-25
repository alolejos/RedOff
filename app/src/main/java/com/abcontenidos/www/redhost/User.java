package com.abcontenidos.www.redhost;

public class User {
    String name, mail, token, direction, age, gender, birthday;
    Integer id;

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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User(String name, String mail, String token, String direction, String age, String gender, String birthday, Integer id) {
        this.name = name;
        this.mail = mail;
        this.token = token;
        this.direction = direction;
        this.age = age;
        this.gender = gender;
        this.birthday = birthday;
        this.id = id;
    }

    public User(){

    }

}
