package com.abcontenidos.www.redhost;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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

    public Category get(int id)
    {
        Cursor c;
        Category category = null;
        c = db.rawQuery("SELECT _id,nombre,edad" +
                " FROM personas WHERE _id=" +id,null);

        if(c.moveToFirst())
        {
            category = new Category();
            category.setId(c.getInt(0));
            category.setName(c.getString(1));
            category.setDetails(c.getString(2));
            category.setImage(c.getString(3));
            category.setSelected(c.getString(4));
        }
        c.close();
        return category;
    }

    public ArrayList<Category> getall()
    {
        Cursor c;
        ArrayList<Category> list = new ArrayList<>();
        Category category = null;
        c = db.rawQuery("SELECT * FROM categories WHERE 1",null);

        if(c.moveToFirst())
        {
            do{
                category = new Category();
                category.setId(c.getInt(0));
                category.setName(c.getString(1));
                category.setDetails(c.getString(2));
                category.setSelected(c.getString(3));
                category.setImage(c.getString(4));
                list.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long save(Category category)
    {
        //statementSave.clearBindings();
        //statementSave.bindString(1, category.getName());
        //statementSave.bindString(2, category.getDetails());
        //return statementSave.executeInsert();
        ContentValues values = new ContentValues();
        values.put("id", category.getId());
        values.put("name", category.getName());
        values.put("details", category.getDetails());
        values.put("image", category.getImage());
        values.put("selected", category.getSelected());
        return  db.insert(tableName, null, values);
    }

    public void update(Category category)
    {
        ContentValues values = new ContentValues();
        values.put("id", category.getId());
        values.put("name", category.getName());
        values.put("details", category.getDetails());
        values.put("image", category.getImage());
        values.put("selected", category.getSelected());

        db.update("categories", values, "id="+category.getId(), null);
    }

    public void delete(Category category)
    {
        db.delete("categories","_id="+category.getId(), null);
    }
}

