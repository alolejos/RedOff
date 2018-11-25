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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements MyRecyclerViewAdapterCategories.ItemClickListener, View.OnClickListener {

    MyRecyclerViewAdapterCategories adapter;
    Button categorySave;
    Intent i;
    SharedPreferences sp;
    BottomNavigationView bottomNavigationView;
    ArrayList<Category> listado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(myToolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.my_toolbar_botom);

        categorySave = (Button)findViewById(R.id.button_category_save);
        categorySave.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_category);

        MyDbHelper helper = new MyDbHelper(this, "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);

        int numberOfColumns = 2;

        listado = new ArrayList<>(categoryDao.getall());
        recyclerView.setLayoutManager(new LinearLayoutManager(this  ));
        adapter = new MyRecyclerViewAdapterCategories(this, listado);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        sp = getSharedPreferences("Login", MODE_PRIVATE);
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

    @Override
    public void onItemClick(View view, int position) {
        Log.d("algo", "item click");
    }

    @Override
    public void onClick(View v) {
        MyDbHelper helper = new MyDbHelper(this, "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);
        for (int tr = 0; tr<listado.size(); tr++){
            categoryDao.update(listado.get(tr));
        }
        final String json = new Gson().toJson(listado);
        Log.d("Json_Gson", json);

        RequestQueue queue;

        queue = Volley.newRequestQueue(this);

        String url ="http://redoff.bithive.cloud/ws/categories_save";

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("respuesta_save", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })  {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody()  {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

        };

        // Adding request to request queue
        queue.add(strReq);

        finish();
    }
}
