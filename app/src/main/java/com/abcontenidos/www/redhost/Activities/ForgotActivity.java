package com.abcontenidos.www.redhost.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abcontenidos.www.redhost.R;
import com.android.volley.DefaultRetryPolicy;
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

        email = findViewById(R.id.forgotEmail);

        button = findViewById(R.id.forgotButton);

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

            if (mail.equals("")) {
                showToastMessage("El campo está vacío, ingrese una dirección de correo");
                email.getText().clear();
            }else {
                Matcher matcher = pattern.matcher(mail);
                if (matcher.matches()) {
                    button.setEnabled(false);
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url ="http://redoff.bithive.cloud/ws/forgot";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("registrando", response);
                                    if (response.equals("Ok")) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this).create();
                                        alertDialog.setCanceledOnTouchOutside(false);
                                        //alertDialog.onTouchEvent();
                                        alertDialog.setTitle("Contrasena enviada");
                                        alertDialog.setMessage("Ingrese a su cuenta de correo le hemos enviado una contrasena temporal");
                                        alertDialog.setIcon(R.drawable.ic_launcher_background);

                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                        alertDialog.show();
                                    } else {
                                        AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this).create();
                                        alertDialog.setTitle("El usuario/correo no esta registrado");
                                        alertDialog.setMessage("Use la opcion de registro. Gracias");
                                        alertDialog.setIcon(R.drawable.ic_launcher_background);

                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                        alertDialog.show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showToastMessage("That didn't work! -- " + error.toString());
                        }
                    }) {
                        @Override
                        public byte[] getBody() {
                            String correo;
                            correo = email.getText().toString();
                            correo = "{\"mail\": \""+correo+"\"}";
                            return correo.getBytes();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);
                }else{
                    showToastMessage("Ingrese una dirección de correo válida. Por ejemplo: nombre@dominio.com");
                    email.getText().clear();
                }
            }
        }
    }
}
