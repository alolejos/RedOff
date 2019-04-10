package com.abcontenidos.www.redhost.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.Dbases.UserDao;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

public class CommerceFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    EditText name, mail, address, birthday;
    Spinner spinner;
    Button saveProfile;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File photoFile;
    User user;
    ProgressBar progressBar;
    RecyclerView imageProfile;

    public CommerceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commerce, container, false);

        saveProfile = view.findViewById(R.id.save_profile);
        name = view.findViewById(R.id.et_profile_name);
        mail = view.findViewById(R.id.et_profile_mail);
        address = view.findViewById(R.id.et_profile_address);
        birthday = view.findViewById(R.id.et_birthday);
        imageProfile = view.findViewById(R.id.image_profile);
        spinner = view.findViewById(R.id.et_gender);
        progressBar = view.findViewById(R.id.progressBar2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource (getActivity(),
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
        MyDbHelper helper = new MyDbHelper(getActivity(), "user");
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

        name.setText(user.getName());
        mail.setText(user.getMail());
        address.setText(user.getAddress());
        birthday.setText(inputTimeStamp);
        int spinnerPosition = adapter.getPosition(user.getGender());
        spinner.setSelection(spinnerPosition);
        //gender.setText(user.age);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_profile:
                dispatchTakePictureIntent();
                break;

            case R.id.save_profile:
                progressBar.setVisibility(View.VISIBLE);
                saveProfile.setEnabled(false);
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
                MyDbHelper helper = new MyDbHelper(getActivity(), "user");
                SQLiteDatabase db = helper.getWritableDatabase();
                UserDao userDao = new UserDao(db);

                // Carga del objeto user
                user.setName(name.getText().toString());
                user.setMail(mail.getText().toString());
                user.setAddress(address.getText().toString());
                user.setBirthday(birthdayString);
                user.setGender(spinner.getSelectedItem().toString());


                // Guardado en base de datos
                userDao.update(user);

                Gson gson = new Gson();
                final String json = gson.toJson(user);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url ="http://redoff.bithive.cloud/ws/profile";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.getString("data").equals("Ok")){
                                        showToastMessage("Guardado!");
                                        progressBar.setVisibility(View.GONE);
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
                break;

            case R.id.et_birthday:
                showDatePickerDialog(birthday);
                break;
        }

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
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private static String TimeStampConverter(final String inputFormat, String inputTimeStamp, final String outputFormat) throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat).parse(inputTimeStamp));
    }
    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MAG_JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile !=    null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Picasso.get().load(photoFile).resize(400, 400).centerCrop().into(imageProfile);
                }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(user.getCommerce_name().equals("null")){
            MenuItem comercio = menu.findItem(R.id.action_comercio);
            comercio.setVisible(false);
        }
        MenuItem item1 = menu.findItem(R.id.action_search);
        MenuItem item2 = menu.findItem(R.id.action_grid);
        item1.setVisible(false);
        item2.setVisible(false);
    }

}
