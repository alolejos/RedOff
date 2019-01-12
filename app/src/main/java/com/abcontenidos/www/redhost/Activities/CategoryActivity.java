package com.abcontenidos.www.redhost.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Dbases.CategoryDao;
import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.MyRecyclerViewAdapterCategories;
import com.abcontenidos.www.redhost.Objets.Category;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.R;
import com.abcontenidos.www.redhost.Dbases.UserDao;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements MyRecyclerViewAdapterCategories.ItemClickListener, View.OnClickListener {

    MyRecyclerViewAdapterCategories adapter;
    Button categorySave;
    Intent i;
    BottomNavigationView bottomNavigationView;
    ArrayList<Category> listado;
    MyDbHelper helper;
    User user;
    Switch selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar myToolbar = findViewById(R.id.my_toolbar1);
        setSupportActionBar(myToolbar);
        bottomNavigationView = findViewById(R.id.my_toolbar_botom);

        categorySave = findViewById(R.id.button_category_save);

        categorySave.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_category);

        helper = new MyDbHelper(this, "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);
        listado = new ArrayList<>(categoryDao.getall());

        recyclerView.setLayoutManager(new LinearLayoutManager(this ));
        adapter = new MyRecyclerViewAdapterCategories(this, listado);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        MyDbHelper helperUser = new MyDbHelper(this, "user");
        SQLiteDatabase db1 = helperUser.getWritableDatabase();
        UserDao userDao = new UserDao(db1);
        user = userDao.get();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Inflate and initialize the bottom menu
        Menu bottomMenu = bottomNavigationView.getMenu();
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
                helper.close();
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.action_botom_main:
                finish();
                break;
            case R.id.action_botom_categories:
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

        String url ="http://redoff.bithive.cloud/ws/categories_save";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Quehay", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.getString("data").equals("Ok")){
                                showToastMessage("Guardado");
                            }else{
                                showToastMessage("Error!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastMessage("That didn't work! -- "+error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders()  {
                // Posting parameters to login url
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer "+user.getToken();
                headers.put("Authorization", auth);
                return headers;
            }

            @Override
            public byte[] getBody()  {
                return json.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        finish();
    }
}
