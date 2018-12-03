package com.abcontenidos.www.redhost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView image;
    Switch aSwitch;
    EditText nameText, passwordText;
    TextView registerText, forgotText;
    Button loginButton;
    Intent intent;
    SharedPreferences sp;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        nameText = (EditText)findViewById(R.id.userText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        registerText = (TextView) findViewById(R.id.registerText);
        forgotText = (TextView) findViewById(R.id.forgotText);
        loginButton = (Button)findViewById(R.id.loginButton);
        image = (ImageView)findViewById(R.id.loginImage);
        aSwitch = (Switch)findViewById(R.id.switch1);

        context = getApplicationContext();

        loginButton.setOnClickListener(this);
        registerText.setOnClickListener(this);
        forgotText.setOnClickListener(this);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

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
                                        if(response.equals("none"))
                                        {
                                            showToastMessage("El mail ingresado no está en nuestros registros, ingrese un email válido.");
                                            nameText.getText().clear();
                                            passwordText.getText().clear();
                                        }else {


                                            // TOMAR LA RESPUESTA... PARSEARLA Y GUARDAR EL NOMBRE EL USUARIO
                                            // EN EL SHARED PREFERENCES... Y EL TOKEN Y GUARDAR LA FLAG
                                            try {
                                                JSONObject dataLog = new JSONObject(response);
                                                MyDbHelper helper = new MyDbHelper(context, "user");
                                                SQLiteDatabase db = helper.getWritableDatabase();
                                                UserDao userDao = new UserDao(db);

                                                userDao.clear();

                                                User user = new User();
                                                user.setId(dataLog.getInt("id"));
                                                user.setName(nameText.getText().toString());
                                                user.setToken(dataLog.getString("token"));
                                                user.setAddress(dataLog.getString("address"));
                                                user.setMail(dataLog.getString("mail"));
                                                user.setAge(dataLog.getString("age"));
                                                user.setGender(dataLog.getString("gender"));
                                                user.setBirthday(dataLog.getString("birthday"));
                                                user.setImage(dataLog.getString("image"));
                                                userDao.save(user);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            i.putExtra("key", response);
                                            startActivity(i);
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
                                params.put("User", nameText.getText().toString());
                                params.put("pass", passwordText.getText().toString());
                                return params;
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

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
