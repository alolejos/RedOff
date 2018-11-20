package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    Intent i;
    SharedPreferences sp;
    Toolbar myToolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.my_toolbar_botom);
        setSupportActionBar(myToolbar);


        Bundle extras = getIntent().getExtras();
        String key = extras.getString("key");

        Promotion a = new Promotion("TV 12'", "TV Samsung 12 pulgadas LCD", "AUDIO y VIDEO", BitmapFactory.decodeResource(getResources(), R.drawable.tvlcd));
        Promotion b = new Promotion("20% ACEITES'", "20% de descuento en aceites", "ALIMENTOS", BitmapFactory.decodeResource(getResources(), R.drawable.aceite));
        Promotion c = new Promotion("TV 12'", "TV Samsung 12 pulgadas LCD", "AUDIO y VIDEO", BitmapFactory.decodeResource(getResources(), R.drawable.tvlcd));
        Promotion d = new Promotion("20% ACEITES'", "20% de descuento en aceites", "ALIMENTOS", BitmapFactory.decodeResource(getResources(), R.drawable.aceite));
        Promotion e = new Promotion("TV 12'", "TV Samsung 12 pulgadas LCD", "AUDIO y VIDEO", BitmapFactory.decodeResource(getResources(), R.drawable.tvlcd));
        Promotion f = new Promotion("20% ACEITES'", "20% de descuento en aceites", "ALIMENTOS", BitmapFactory.decodeResource(getResources(), R.drawable.aceite));
        ArrayList<Promotion> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_main);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        sp = getSharedPreferences("Login", MODE_PRIVATE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("view", Integer.toString(view.getId()));
        Log.d("Position", Integer.toString(position));
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
                finish();
                break;
            case R.id.action_botom_favorites:
                i = new Intent(this, CategoryActivity.class);
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
