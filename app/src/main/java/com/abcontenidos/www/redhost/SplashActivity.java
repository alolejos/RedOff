package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp1;
    String name;
    Integer id;
    RequestQueue queue;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        flag = sp1.getBoolean("flag", false);
        name = sp1.getString("user", null);
        id = sp1.getInt("id", 0);

        queue = Volley.newRequestQueue(this);

        String url ="http://redoff.bithive.cloud/ws/categories";

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("respuesta1", response);
                        if(response!="none"){
                            try {
                                saveCategories(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("id", Integer.toString(id));
                    return params;
                }

        };

        // Adding request to request queue
        queue.add(strReq);


        //if (state){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    Intent i;
                    if(flag){
                        i = new Intent(SplashActivity.this, MainActivity.class);
                    }else{
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    i.putExtra("token", id);
                    startActivity(i);
                    finish();
                }
            }, 1000);
    }

    public void saveCategories(String response) throws JSONException {
        Category category = new Category();
        JSONArray jsonArray = new JSONArray(response);
        MyDbHelper helper = new MyDbHelper(this, "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);
        db.execSQL("delete from categories");

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("problema", e.toString());
            }
            category.setId(Integer.valueOf(jsonObj.getString("id")  ));
            category.setName(jsonObj.getString("name"));
            category.setDetails(jsonObj.getString("details"));
            category.setImage(jsonObj.getString("image"));
            category.setSelected(jsonObj.getString("selected"));
            categoryDao.save(category);
        }
    }
}
