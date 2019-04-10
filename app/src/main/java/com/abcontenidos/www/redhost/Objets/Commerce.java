package com.abcontenidos.www.redhost.Objets;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class Commerce {
    String id;
    String name;
    String mail;
    String token;
    String address;
    String gender;
    String birthday;
    String image;
    String pass;
    String commerce_id;
    String commerce_name;
    String commerce_address;
    String commerce_phone;
    String commerce_email;
    String commerce_web;
    ArrayList commerce_images;

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

    public String getCommerce_id() {
        return commerce_id;
    }

    public void setCommerce_id(String commerce_id) {
        this.commerce_id = commerce_id;
    }

    public String getCommerce_name() {
        return commerce_name;
    }

    public void setCommerce_name(String commerce_name) {
        this.commerce_name = commerce_name;
    }

    public String getCommerce_address() {
        return commerce_address;
    }

    public void setCommerce_address(String commerce_address) {
        this.commerce_address = commerce_address;
    }

    public String getCommerce_phone() {
        return commerce_phone;
    }

    public void setCommerce_phone(String commerce_phone) {
        this.commerce_phone = commerce_phone;
    }

    public String getCommerce_email() {
        return commerce_email;
    }

    public void setCommerce_email(String commerce_email) {
        this.commerce_email = commerce_email;
    }

    public String getCommerce_web() {
        return commerce_web;
    }

    public void setCommerce_web(String commerce_web) {
        this.commerce_web = commerce_web;
    }

    public ArrayList getCommerce_image() {
        return commerce_images;
    }

    public void setCommerce_image(ArrayList commerce_image) {
        this.commerce_images = commerce_image;
    }

    public Commerce(){

    }

    public Commerce(String name, String mail, String token, String address, String gender, String birthday, String id, String image, String pass) {
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
