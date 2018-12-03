package com.abcontenidos.www.redhost;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar myToolbar;
    EditText name, mail, pass, repass, address, age, birthday;
    Spinner spinner;
    ImageView imageProfile;
    Button buttonRegister;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File photoFile;
    Boolean flag_take_picture = false;
    User user;
    TextInputLayout tilName, tilMail, tilPass, tilRePass, tilAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myToolbar = findViewById(R.id.my_toolbar_register);
        setSupportActionBar(myToolbar);

        name = findViewById(R.id.et_register_fullname);
        mail = findViewById(R.id.et_register_mail);
        pass = findViewById(R.id.et_register_pass);
        repass = findViewById(R.id.et_register_repass);
        address = findViewById(R.id.et_register_address);
        age = findViewById(R.id.et_register_age);
        birthday = findViewById(R.id.et_register_birthday);
        imageProfile = findViewById(R.id.image_register);
        buttonRegister = findViewById(R.id.save_register);
        spinner = findViewById(R.id.et_register_gender);

        tilName = findViewById(R.id.tilName);
        tilMail = findViewById(R.id.tilMail);
        tilPass = findViewById(R.id.tilPass);
        tilRePass = findViewById(R.id.tilRePass);
        tilAddress = findViewById(R.id.tilAddress);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource (this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        imageProfile.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        birthday.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_register:
                // PRIMERO VALIDAR LOS DATOS
                Log.d("entra en boton", "boton");
                validarDatos();
                // SEGUNDO LO ENVIAMOS AL WS PARA SU INGRESO AL SISTEMA

                // TERCERO, TOMAMOS LA RESPUESTA DEL WS Y LO GARDAMOS EN LA BASE DE DATOS
                /*MyDbHelper helper = new MyDbHelper(this, "user");
                SQLiteDatabase db = helper.getWritableDatabase();
                UserDao userDao = new UserDao(db);

                user.setName(name.getText().toString());
                user.setMail(name.getText().toString());
                user.setAddress(address.getText().toString());
                user.setAge(age.getText().toString());
                if (flag_take_picture) {
                    //user.setImage(getImage());
                }
                userDao.save(user);
                */

                //finish();
                break;

            case R.id.et_register_birthday:
                showDatePickerDialog(birthday);
                break;

            case R.id.image_register:
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

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
            tilName.setError("Nombre inválido");
            return false;
        } else {
            tilName.setError(null);
        }

        return true;
    }

    private boolean esPassValido(String repass) {
        if (!repass.equals(tilPass.getEditText().getText().toString())) {
            tilRePass.setError("Las contraseñas no coinciden");
            return false;
        } else {
            tilRePass.setError(null);
        }

        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilMail.setError("Correo electrónico inválido");
            return false;
        } else {
            tilMail.setError(null);
        }

        return true;
    }

    private void validarDatos() {
        String nombre = tilName.getEditText().getText().toString();
        String pass = tilRePass.getEditText().getText().toString();
        String correo = tilMail.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esPassValido(pass);
        boolean c = esCorreoValido(correo);

        if (a && b && c) {
            // OK, se pasa a la siguiente acción
            Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Todo mal... hay que ingresar todo de vuelta", Toast.LENGTH_LONG).show();
        }

    }


}
