package com.abcontenidos.www.redhost;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar myToolbar;
    EditText name, mail, pass, address, age, birthday;
    Spinner spinner;
    ImageView imageProfile;
    Button saveProfile;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File photoFile;
    Boolean flag_take_picture = false;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Seteo de la Toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar_profile);
        setSupportActionBar(myToolbar);

        // Seteo de las Views
        name = findViewById(R.id.et_profile_name);
        mail = findViewById(R.id.et_profile_mail);
        pass = findViewById(R.id.et_profile_pass);
        address = findViewById(R.id.et_profile_address);
        age = findViewById(R.id.et_age);
        birthday = findViewById(R.id.et_birthday);
        imageProfile = findViewById(R.id.image_profile);
        saveProfile = findViewById(R.id.save_profile);
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

        // Pase de datos a los objetos visuales
        name.setText(user.name);
        mail.setText(user.mail);
        pass.setText(user.token);
        address.setText(user.address);
        age.setText(user.age);
        int spinnerPosition = adapter.getPosition(user.gender);
        spinner.setSelection(spinnerPosition);

        //gender.setText(user.age);

        byte[] decodedString = Base64.decode(user.image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        imageProfile.setImageBitmap(decodedByte);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
                MyDbHelper helper = new MyDbHelper(this, "user");
                SQLiteDatabase db = helper.getWritableDatabase();
                UserDao userDao = new UserDao(db);

                user.setName(name.getText().toString());
                user.setMail(mail.getText().toString());
                user.setAddress(address.getText().toString());
                user.setAge(age.getText().toString());
                if (flag_take_picture) {
                    user.setImage(getImage());
                }
                userDao.update(user);
                finish();
                break;

            case R.id.et_birthday:
                showDatePickerDialog();
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

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                String selectedDate = day + "/" + (month+1) + "/" + year;
                birthday.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}

