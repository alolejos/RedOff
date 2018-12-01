package com.abcontenidos.www.redhost;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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
        c = db.rawQuery("SELECT id, name, mail, token, address, age, gender, birthday, image" +
                " FROM user WHERE 1",null);

        if(c.moveToFirst())
        {
            user = new User();
            user.setId(c.getInt(0));
            user.setName(c.getString(1));
            user.setMail(c.getString(2));
            user.setToken(c.getString(3));
            user.setAddress(c.getString(4));
            user.setAge(c.getString(5));
            user.setGender(c.getString(6));
            user.setBirthday(c.getString(7));
            user.setImage(c.getString(8));
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
                user.setId(c.getInt(0));
                user.setName(c.getString(1));
                user.setMail(c.getString(2));
                user.setToken(c.getString(3));
                user.setAddress(c.getString(5));
                user.setAge(c.getString(6));
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
        Log.d("User_dentro", "address:  "+user.address);
        ContentValues values = new ContentValues();
        values.put("id", user.getId());
        values.put("name", user.getName());
        values.put("mail", user.getMail());
        values.put("token", user.getToken());
        values.put("address", user.getAddress());
        values.put("age", user.getAge());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("image", user.getImage());
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
        values.put("age", user.getAge());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("image", user.getImage());
        db.update("user", values, "id="+user.getId(), null);
    }

    public void delete(User user)
    {
        db.delete("user","_id="+user.getId(), null);
    }

    public void clear()
    {
        db.delete("user","1", null);
    }
}

