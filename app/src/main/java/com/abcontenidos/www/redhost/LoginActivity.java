package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
                String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                        "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
                Pattern pattern = Pattern.compile(emailPattern);
                if (user == "") {
                    showToastMessage("El campo está vacío, ingrese una dirección de correo");
                    nameText.getText().clear();
                }else {
                    Matcher matcher = pattern.matcher(user);
                    if (matcher.matches()) {
                        RequestQueue queue = Volley.newRequestQueue(this);
                        String url ="https://qareful.com/testurl?id="+user+"&pass="+password;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("none"))
                                        {
                                            showToastMessage("El mail ingresado no está en nuestros registros, ingrese un email válido.");
                                            nameText.getText().clear();
                                        }else {
                                            SharedPreferences.Editor ed = sp.edit();
                                            ed.putString("user", nameText.getText().toString());
                                            ed.putString("key", response);
                                            ed.putBoolean("state", aSwitch.isChecked());
                                            ed.apply();
                                            // hay que tomar los datos que devuelve... poner el name en el sharedpreferenc y guardar tambien ahi el token.
                                            // si el switch de permanencia esta en ON... guardar todo y pasar al mainactivity
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
                        });
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }else{
                        showToastMessage("Ingrese una dirección de correo válida. Por ejemplo: nombre@dominio.com");
                        nameText.getText().clear();
                    }
                }
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("key", "12345678");
                startActivity(intent);
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
