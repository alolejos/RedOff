package com.abcontenidos.www.redhost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        email = (EditText)findViewById(R.id.forgotEmail);

        button = (Button)findViewById(R.id.forgotButton);

        button.setOnClickListener(this);



    }

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onClick(View v) {

        if (v.equals(findViewById(R.id.forgotButton))) {
            String mail = email.getText().toString();
            String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                    "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
            Pattern pattern = Pattern.compile(emailPattern);
            Log.d("mail", email.getText().toString());
            Log.d("pattern", emailPattern);
            if (mail == "Ingrese su correo") {
                showToastMessage("El campo está vacío, ingrese una dirección de correo");
                email.getText().clear();
            }else {
                Matcher matcher = pattern.matcher(mail);
                if (matcher.matches()) {
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url ="https://qareful.com/testurl?id="+email.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("none"))
                                    {
                                        showToastMessage("El mail ingresado no está en nuestros registros, ingrese un email válido.");
                                        email.getText().clear();
                                    }else if (response.equals("esta")){
                                        showToastMessage("Enviamos un correo electrónico a "+email.getText().toString()+". Revisa tu cuenta y sigue los pasos");
                                        finish();
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
                    email.getText().clear();
                }
            }
        }
    }
}
