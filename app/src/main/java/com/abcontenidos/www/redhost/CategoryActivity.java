package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryActivity extends AppCompatActivity implements MyRecyclerViewAdapterCategories.ItemClickListener, View.OnClickListener {

    MyRecyclerViewAdapterCategories adapter;
    Button categorySave;
    Intent i;
    SharedPreferences sp;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(myToolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.my_toolbar_botom);

        MyDbHelper helper = new MyDbHelper(this, "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);

        categorySave = (Button)findViewById(R.id.button_category_save);
        categorySave.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_category);

        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new LinearLayoutManager(this  ));
        adapter = new MyRecyclerViewAdapterCategories(this, categoryDao.getall());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        sp = getSharedPreferences("Login", MODE_PRIVATE);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {



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
                SharedPreferences.Editor ed = sp.edit();
                ed.clear();
                ed.apply();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.action_botom_main:
                finish();
                break;
            case R.id.action_botom_favorites:
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
}
