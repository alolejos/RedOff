package com.abcontenidos.www.redhost.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.MyRecyclerViewAdapter;
import com.abcontenidos.www.redhost.Objets.Post;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.Dbases.PostDao;
import com.abcontenidos.www.redhost.R;
import com.abcontenidos.www.redhost.Dbases.UserDao;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    Intent i;
    SharedPreferences sp;
    Toolbar myToolbar;
    BottomNavigationView bottomNavigationView;
    User user;
    UserDao userDao;
    PostDao postDao;
    Integer from, to;
    ArrayList<Post> list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // carga elementos visuales
        myToolbar = findViewById(R.id.my_toolbar_main);
        bottomNavigationView = findViewById(R.id.my_toolbar_botom);
        setSupportActionBar(myToolbar);

        // lectura del usuario de la base de datos
        MyDbHelper helper = new MyDbHelper(this, "user");
        SQLiteDatabase db = helper.getWritableDatabase();
        userDao = new UserDao(db);
        user = userDao.get();

        // lectura de los posts
        MyDbHelper helperPosts = new MyDbHelper(this, "posts");
        SQLiteDatabase db1 = helperPosts.getWritableDatabase();
        postDao= new PostDao(db1);
        list = postDao.getall();

        // carga del Recyclerview
        recyclerView = findViewById(R.id.recycler_main);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Inflate and initialize the bottom menu
        Menu bottomMenu = bottomNavigationView.getMenu();
        Log.d("tamaño", "= "+bottomMenu.size());
        for (int i = 0; i < bottomMenu.size(); i++) {
            bottomMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_terminos:
                i = new Intent(this, TermsActivity.class);
                startActivity(i);
                break;
            case R.id.action_acerca:
            showToastMessage("Red Off es una aplicacion bla bla bla bla \n Desarrollado por Abcontenidos.com");
                break;
            case R.id.action_logout:
                userDao.delete();
                postDao.delete();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.action_botom_main:
                break;
            case R.id.action_botom_categories:
                i = new Intent(this, CategoryActivity.class);
                startActivity(i);
                break;
            case R.id.action_botom_profile:
                i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        i = new Intent(this, PostInfo.class);
        i.putExtra("key", position);
        startActivity(i);
    }
}
