package com.abcontenidos.www.redhost.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.Dbases.PostDao;
import com.abcontenidos.www.redhost.Objets.Post;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.R;
import com.abcontenidos.www.redhost.Dbases.UserDao;
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

    String name, id;
    RequestQueue queue;
    Boolean flag;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        queue = Volley.newRequestQueue(this);

        MyDbHelper helper = new MyDbHelper(this, "user");
        SQLiteDatabase db = helper.getWritableDatabase();
        UserDao userDao = new UserDao(db);

        // Carga del objeto user
        user = userDao.get();

        flag = (user==null)?false:true;

        Log.d("Flag: ", flag.toString());

        //if (state){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(flag){
                        postsrecharge();
                    }else{
                        Intent i;
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, 3000);
    }

    private void postsrecharge() {
        final String url ="http://redoff.bithive.cloud/ws/posts";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("respuesta", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray posteos = jsonResponse.getJSONArray("data");
                            save_posteos(posteos);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastMessage("Problemas con la conexión -- "+error.toString());
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
                return url.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void save_posteos(JSONArray posteos) {
        Post post = new Post();
        MyDbHelper helperPosts = new MyDbHelper(this, "posts");
        SQLiteDatabase db_posts = helperPosts.getWritableDatabase();
        PostDao postsDao = new PostDao(db_posts);
        db_posts.execSQL("delete from posts");
        for (int i = 0; i < posteos.length(); i++){
            JSONObject jsonObj = null;
            try {
                jsonObj = posteos.getJSONObject(i);
                post.setName(jsonObj.getString("name"));
                post.setDetails(jsonObj.getString("details"));
                post.setImage(jsonObj.getString("image"));
                post.setCategory(jsonObj.getString("category"));
                post.setCommerce(jsonObj.getString("commerce"));
                post.setAddresscommece(jsonObj.getString("address"));
                post.setCelcommerce(jsonObj.getString("phone"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("problema", e.toString());
            }
            postsDao.save(post);
        }
        Intent i = new Intent(SplashActivity.this, PrincipalActivity.class);
        i.putExtra("token", user.getToken());
        startActivity(i);
        finish();
    }
    private void showToastMessage (String message) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
}
