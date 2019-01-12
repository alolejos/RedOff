package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.Dbases.PostDao;
import com.abcontenidos.www.redhost.Objets.Post;
import com.squareup.picasso.Picasso;


public class PostInfo extends AppCompatActivity {

    Post post;
    TextView name, details, commerce;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        // lectura del post
        MyDbHelper helperPosts = new MyDbHelper(this, "posts");
        SQLiteDatabase db1 = helperPosts.getWritableDatabase();
        PostDao postDao;
        postDao= new PostDao(db1);

        Intent intent = getIntent();
        int id = intent.getIntExtra("key", 0);
        id++;

        post = postDao.get(id);

        Log.d("numero y nombre post:", Integer.valueOf(id)+" / "+post.getName());

        name = findViewById(R.id.infoName);
        details = findViewById(R.id.infoDetails);
        commerce = findViewById(R.id.infoCommerce);
        imageView = findViewById(R.id.imagePost);

        name.setText(post.getName());
        details.setText(post.getCategory());
        commerce.setText(post.getCommerce());
        String image = "http://redoff.bithive.cloud/images/thumbails/"+post.getImage();
        Picasso.get().load(image).resize(400, 400).into(imageView);

    }
}
