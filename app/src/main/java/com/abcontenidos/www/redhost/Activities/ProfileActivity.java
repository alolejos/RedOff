package com.abcontenidos.www.redhost.Activities;


// bottomNavigationView.setVisibility(View.GONE);

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.abcontenidos.www.redhost.DatePickerFragment;
import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar myToolbar;
    EditText name, mail, address, birthday;
    Spinner spinner;
    ImageView imageProfile;
    Button saveProfile;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File photoFile;
    Boolean flag_take_picture = false;
    User user;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Seteo de la Toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar_profile);
        bottomNavigationView = findViewById(R.id.my_toolbar_botom);
        setSupportActionBar(myToolbar);


        // Seteo de las Views
        saveProfile = findViewById(R.id.save_profile);
        name = findViewById(R.id.et_profile_name);
        mail = findViewById(R.id.et_profile_mail);
        address = findViewById(R.id.et_profile_address);
        birthday = findViewById(R.id.et_birthday);
        imageProfile = findViewById(R.id.image_profile);
        spinner = findViewById(R.id.et_gender);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource (this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        imageProfile.setOnClickListener(this);
        saveProfile.setOnClickListener(this);
        birthday.setOnClickListener(this);

        spinner.setOnItemSelectedListener(this);

        // Carga los datos del usuario de la Base de dato a un User
        MyDbHelper helper = new MyDbHelper(this, "user");
        SQLiteDatabase db = helper.getWritableDatabase();
        UserDao userDao = new UserDao(db);
        user = userDao.get();

        // formatear la fecha
        String inputTimeStamp = user.getBirthday();
        try {
            final String inputFormat = "yyyy-MM-dd";
            final String outputFormat = "dd/MM/yyyy";
            inputTimeStamp = TimeStampConverter(inputFormat, inputTimeStamp,
                    outputFormat);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Pase de datos a los objetos visuales
        byte[] decodedString = Base64.decode(user.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageProfile.setImageBitmap(decodedByte);
        name.setText(user.getName());
        mail.setText(user.getMail());
        address.setText(user.getAddress());
        birthday.setText(inputTimeStamp);
        int spinnerPosition = adapter.getPosition(user.getGender());
        spinner.setSelection(spinnerPosition);
        //gender.setText(user.age);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        // Inflate and initialize the bottom menu
        Menu bottomMenu = bottomNavigationView.getMenu();
        Log.d("tamaño", "= "+bottomMenu.size());
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
                Intent i = new Intent(this, TermsActivity.class);
                startActivity(i);
                break;
            case R.id.action_acerca:
                showToastMessage("Red Off es una aplicacion bla bla bla bla \n Desarrollado por Abcontenidos.com");
                break;
            case R.id.action_botom_main:
                break;
            case R.id.action_botom_categories:
                i = new Intent(this, CategoryActivity.class);
                startActivity(i);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_profile:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
                break;

            case R.id.save_profile:

                String birthdayString = null;
                // formatear la fecha
                try {
                    final String inputFormat = "dd/MM/yyyy";
                    final String outputFormat = "yyyy-MM-dd";
                    birthdayString = TimeStampConverter(inputFormat, birthday.getText().toString(),
                            outputFormat);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Base de datos
                MyDbHelper helper = new MyDbHelper(this, "user");
                SQLiteDatabase db = helper.getWritableDatabase();
                UserDao userDao = new UserDao(db);

                // Carga del objeto user
                user.setName(name.getText().toString());
                user.setMail(mail.getText().toString());
                user.setAddress(address.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setGender(spinner.getSelectedItem().toString());
                Bitmap bm = ((BitmapDrawable)imageProfile.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] encodedString = byteArrayOutputStream.toByteArray();
                String toBase64 = Base64.encodeToString(encodedString, Base64.DEFAULT);
                user.setImage(toBase64);
                // Guardado en base de datos
                userDao.update(user);

                Gson gson = new Gson();
                final String json = gson.toJson(user);
                RequestQueue queue = Volley.newRequestQueue(this);
                String url ="http://redoff.bithive.cloud/ws/profile";
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
                break;

            case R.id.et_birthday:
                showDatePickerDialog(birthday);
                break;
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MAG_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            //imageProfile.setImageBitmap(mImageBitmap);
            //imageProfile.setImageURI(Uri.parse(mCurrentPhotoPath));
            //Picasso.get().load(mCurrentPhotoPath).into(imageProfile);
            flag_take_picture = true;
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageProfile.getWidth();
        int targetH = imageProfile.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /* Ignored for PNGs */, blob);
        byte[] arrayImagen = blob.toByteArray();

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(arrayImagen, 0, arrayImagen.length);
        imageProfile.setImageBitmap(bitmap);
    }

    private String getImage() {
        // Get the dimensions of the View
        int targetW = imageProfile.getWidth();
        int targetH = imageProfile.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /* Ignored for PNGs */, blob);
        String image = blob.toString();


        return image;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setGender(spinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        user.setGender("No dice");
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private static String TimeStampConverter(final String inputFormat, String inputTimeStamp, final String outputFormat) throws ParseException {
            return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat).parse(inputTimeStamp));
    }
}

