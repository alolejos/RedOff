package com.abcontenidos.www.redhost;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class PostDao {

    private SQLiteDatabase db;
    private SQLiteStatement statementSave;
    private String tableName = "posts";

    public PostDao(SQLiteDatabase db)
    {
        this.db=db;
        //statementSave = db.compileStatement("INSERT INTO personas (nombre,edad) VALUES(?,?)");
    }

    public Post get(int id)
    {
        Cursor c;
        Post post = null;
        c = db.rawQuery("SELECT id, name, details, category, image" +
                " FROM posts WHERE 1",null);

        if(c.moveToFirst())
        {
            post = new Post();
            post.setId(c.getString(0));
            post.setName(c.getString(1));
            post.setDetails(c.getString(2));
            post.setCategory(c.getString(3));
            post.setImage(c.getString(4));
        }
        c.close();
        return post;
    }

    public ArrayList<Post> getall()
    {
        Cursor c;
        ArrayList<Post> list = new ArrayList<>();
        c = db.rawQuery("SELECT * FROM posts WHERE 1",null);

        if(c.moveToFirst())
        {
            do{
                Post post = new Post();
                post.setId(c.getString(0));
                post.setName(c.getString(1));
                post.setDetails(c.getString(2));
                post.setCategory(c.getString(3));
                post.setImage(c.getString(4));
                list.add(post);
            }while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long save(Post post)
    {
        //statementSave.clearBindings();
        //statementSave.bindString(1, category.getName());
        //statementSave.bindString(2, category.getDetails());
        //return statementSave.executeInsert();
        ContentValues values = new ContentValues();
        values.put("id", post.getId());
        values.put("name", post.getName());
        values.put("details", post.getDetails());
        values.put("category", post.getCategory());
        values.put("image", post.getImage());
        return  db.insert(tableName, null, values);
    }

    public void update(Post post)
    {
        ContentValues values = new ContentValues();
        values.put("id", post.getId());
        values.put("name", post.getName());
        values.put("details", post.getDetails());
        values.put("category", post.getCategory());
        values.put("image", post.getImage());
        db.update("posts", values, "id="+post.getId(), null);
    }

    public void delete(Post post)
    {
        db.delete("posts","id="+post.getId(), null);
    }

    public void clear()
    {
        db.delete("posts",null, null);
    }
}

