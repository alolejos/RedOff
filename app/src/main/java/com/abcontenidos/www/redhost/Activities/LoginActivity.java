package com.abcontenidos.www.redhost.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Dbases.CategoryDao;
import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.Objets.Category;
import com.abcontenidos.www.redhost.Objets.Post;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.Dbases.PostDao;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView image;
    Switch aSwitch;
    EditText nameText, passwordText;
    TextView registerText, forgotText;
    Button loginButton;
    Intent intent;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        nameText = findViewById(R.id.userText);
        passwordText = findViewById(R.id.passwordText);
        registerText = findViewById(R.id.registerText);
        forgotText = findViewById(R.id.forgotText);
        loginButton = findViewById(R.id.loginButton);
        image = findViewById(R.id.loginImage);
        aSwitch = findViewById(R.id.switch1);

        context = getApplicationContext();

        loginButton.setOnClickListener(this);
        registerText.setOnClickListener(this);
        forgotText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                String user = nameText.getText().toString();
                String password = passwordText.getText().toString();
                if (user.trim().length()==0 || password.trim().length()==0) {
                    showToastMessage("Alguno de los campos están vacíos. Ingrese correo y contraseña");
                    nameText.getText().clear();
                    passwordText.getText().clear();
                }else {
                    if (Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                        RequestQueue queue = Volley.newRequestQueue(this);
                        String url ="http://redoff.bithive.cloud/ws/login";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        MyDbHelper helper = new MyDbHelper(context, "user");
                                        SQLiteDatabase db = helper.getWritableDatabase();
                                        UserDao userDao = new UserDao(db);
                                        userDao.clear();
                                        Log.d("viene", response);
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            JSONObject data = jsonResponse.getJSONObject("data");
                                            String error = data.getString("error");
                                            if (error.equals("1")){
                                                showToastMessage("Error de usuario/contraseña");
                                                nameText.getText().clear();
                                                passwordText.getText().clear();
                                            }else{
                                                JSONObject listado = data.getJSONObject("message");
                                                User user = new User();
                                                user.setId(listado.getString("id"));
                                                user.setName(listado.getString("fullname"));
                                                user.setToken(jsonResponse.getString("token"));
                                                user.setAddress(listado.getString("address"));
                                                user.setMail(listado.getString("email"));
                                                user.setGender(listado.getString("gender"));
                                                user.setBirthday(listado.getString("birthday"));
                                                user.setImage(listado.getString("image"));
                                                user.setCommerce_id(listado.getString("commerce_id"));
                                                user.setCommerce_name(listado.getString("commerce_name"));
                                                user.setCommerce_address(listado.getString("commerce_address"));
                                                user.setCommerce_phone(listado.getString("commerce_phone"));
                                                user.setCommerce_email(listado.getString("commerce_email"));
                                                user.setCommerce_web(listado.getString("commerce_web"));
                                                user.setCommerce_image(listado.getString("commerce_image"));

                                                userDao.save(user);
                                                JSONArray posteos = data.getJSONArray("posts");
                                                save_posteos(posteos);
                                                JSONArray categories = data.getJSONArray("categories");
                                                save_categories(categories);
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
                            protected Map<String, String> getParams() {
                                // Posting parameters to login url
                                Map<String, String> params = new HashMap<>();
                                //params.put("user", nameText.getText().toString());
                                //params.put("pass", passwordText.getText().toString());
                                return params;
                            }

                            @Override
                            public byte[] getBody()  {
                                JSONObject data = new JSONObject();
                                byte[] sendBody = null;
                                try {
                                    data.put("user", nameText.getText().toString());
                                    data.put("pass", passwordText.getText().toString());
                                    sendBody = data.toString().getBytes("utf-8");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } finally {
                                    return sendBody;
                                }
                            }

                            /*@Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("Content-Type", "application/json");
                                return params;
                            }*/

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }else{
                        showToastMessage("Ingrese una dirección de correo válida. Por ejemplo: nombre@dominio.com");
                        nameText.getText().clear();
                        passwordText.getText().clear();
                    }
                }

                break;
            case R.id.registerText:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forgotText:
                intent = new Intent(this, ForgotActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void save_categories(JSONArray categories) {
        Category category = new Category();
        MyDbHelper helperCategories = new MyDbHelper(this, "categories");
        SQLiteDatabase db_categories = helperCategories.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db_categories);
        db_categories.execSQL("delete from categories");
        for (int i = 0; i < categories.length(); i++){
            JSONObject jsonObj = null;
            try {
                jsonObj = categories.getJSONObject(i);
                category.setId(jsonObj.getInt("id"));
                category.setName(jsonObj.getString("name"));
                category.setDetails(jsonObj.getString("details"));
                category.setImage("http://redoff.bithive.cloud/files/categories/thumbs/"+jsonObj.getString("image"));
                category.setSelected(jsonObj.getString("selected"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("problema", e.toString());
            }
            categoryDao.save(category);
        }
        Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
        //i.putExtra("key", response);
        startActivity(i);
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
    }

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
