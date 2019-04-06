package com.abcontenidos.www.redhost.Dbases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.abcontenidos.www.redhost.Objets.User;

import java.util.ArrayList;

public class UserDao {

    private SQLiteDatabase db;
    private SQLiteStatement statementSave;
    private String tableName = "user";

    public UserDao(SQLiteDatabase db)
    {
        this.db=db;
        //statementSave = db.compileStatement("INSERT INTO personas (nombre,edad) VALUES(?,?)");
    }

    public User get()
    {
        Cursor c;
        User user = null;
        c = db.rawQuery("SELECT id, name, mail, token, address, gender, birthday, image, " +
                "commerce_id, commerce_name, commerce_address, commerce_phone, commerce_email, commerce_web, commerce_image" +
                " FROM user WHERE 1",null);

        if(c.moveToFirst())
        {
            user = new User();
            user.setId(c.getString(0));
            user.setName(c.getString(1));
            user.setMail(c.getString(2));
            user.setToken(c.getString(3));
            user.setAddress(c.getString(4));
            user.setGender(c.getString(5));
            user.setBirthday(c.getString(6));
            user.setImage(c.getString(7));
            user.setCommerce_id(c.getString(8));
            user.setCommerce_name(c.getString(9));
            user.setCommerce_address(c.getString(10));
            user.setCommerce_phone(c.getString(11));
            user.setCommerce_email(c.getString(12));
            user.setCommerce_web(c.getString(13));
            user.setCommerce_image(c.getString(14));
        }
        c.close();
        return user;
    }

    public ArrayList<User> getall()
    {
        Cursor c;
        ArrayList<User> list = new ArrayList<>();
        User user= null;
        c = db.rawQuery("SELECT * FROM user WHERE 1",null);

        if(c.moveToFirst())
        {
            do{
                user = new User();
                user.setId(c.getString(0));
                user.setName(c.getString(1));
                user.setMail(c.getString(2));
                user.setToken(c.getString(3));
                user.setAddress(c.getString(5));
                user.setGender(c.getString(7));
                user.setBirthday(c.getString(8));
                user.setImage(c.getString(9));
                list.add(user);
            }while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long save(User user)
    {
        //statementSave.clearBindings();
        //statementSave.bindString(1, category.getName());
        //statementSave.bindString(2, category.getDetails());
        //return statementSave.executeInsert();
        Log.d("User_dentro", "address:  "+user.getAddress());
        ContentValues values = new ContentValues();
        values.put("id_base", user.getId());
        values.put("name", user.getName());
        values.put("mail", user.getMail());
        values.put("token", user.getToken());
        values.put("address", user.getAddress());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("image", user.getImage());
        values.put("commerce_id", user.getCommerce_id());
        values.put("commerce_name", user.getCommerce_name());
        values.put("commerce_address", user.getCommerce_address());
        values.put("commerce_phone", user.getCommerce_phone());
        values.put("commerce_email", user.getCommerce_email());
        values.put("commerce_web", user.getCommerce_web());
        values.put("commerce_image", user.getCommerce_image());
        return  db.insert(tableName, null, values);
    }

    public void update(User user)
    {
        ContentValues values = new ContentValues();
        values.put("id", user.getId());
        values.put("name", user.getName());
        values.put("mail", user.getMail());
        values.put("token", user.getToken());
        values.put("address", user.getAddress());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("image", user.getImage());
        db.update("user", values, "id="+user.getId(), null);
    }

    public void delete()
    {
        db.delete("user",null, null);
    }

    public void clear()
    {
        db.delete("user",null, null);
    }
}

