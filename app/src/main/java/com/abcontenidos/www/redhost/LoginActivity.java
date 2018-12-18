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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okio.Utf8;

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
                                        Log.d("respuesta", response);
                                        MyDbHelper helper = new MyDbHelper(context, "user");
                                        SQLiteDatabase db = helper.getWritableDatabase();
                                        UserDao userDao = new UserDao(db);
                                        userDao.clear();
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            JSONObject data = jsonResponse.getJSONObject("data");
                                            String error = data.getString("error");
                                            if (error.equals("1")){
                                                showToastMessage("Error de usuario/contraseña");
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
                                                userDao.save(user);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if(response.equals("none"))
                                        {
                                            showToastMessage("El mail ingresado no está en nuestros registros, ingrese un email válido.");
                                            nameText.getText().clear();
                                            passwordText.getText().clear();
                                        }else {


                                            // TOMAR LA RESPUESTA... PARSEARLA Y GUARDAR EL NOMBRE EL USUARIO
                                            // EN EL SHARED PREFERENCES... Y EL TOKEN Y GUARDAR LA FLAG

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

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
