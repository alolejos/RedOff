package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp1;
    String key, name;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        Boolean state = sp1.getBoolean("state", false);
        name = sp1.getString("user", null);
        key = sp1.getString("key", null);
        queue = Volley.newRequestQueue(this);

        //if (state){
            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {
                    // This method will be executed once the timer is over

                    String url ="http://redoff.bithive.cloud/ws/index";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("response", " "+response);
                                    try {
                                        saveCategories(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Voller ", "error: "+error.toString());
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("key", key);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        /*else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);
        }*/
    }

    public void saveCategories(String response) throws JSONException {
        Category category = new Category();
        JSONArray jsonArray = null;
        MyDbHelper helper = new MyDbHelper(this, "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);
        db.execSQL("delete from categories");

        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            category.setId(jsonObj.getInt("id"));
            category.setName(jsonObj.getString("name"));
            category.setDetails(jsonObj.getString("details"));
            category.setImage(jsonObj.getString("image"));
            category.setSelected(jsonObj.getString("selected"));
            categoryDao.save(category);
        }
    }
}
