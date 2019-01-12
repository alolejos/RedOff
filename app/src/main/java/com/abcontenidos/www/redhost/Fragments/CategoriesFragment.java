package com.abcontenidos.www.redhost.Fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Dbases.UserDao;
import com.abcontenidos.www.redhost.Objets.Category;
import com.abcontenidos.www.redhost.Dbases.CategoryDao;
import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.MyRecyclerViewAdapterCategories;
import com.abcontenidos.www.redhost.R;
import com.abcontenidos.www.redhost.Objets.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class CategoriesFragment extends Fragment implements View.OnClickListener, MyRecyclerViewAdapterCategories.ItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MyRecyclerViewAdapterCategories adapter;
    Button categorySave;
    Intent i;
    BottomNavigationView bottomNavigationView;
    ArrayList<Category> listado;
    MyDbHelper helper;
    User user;
    Switch selected;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categorySave = view.findViewById(R.id.button_category_save);

        categorySave.setOnClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_category);

        helper = new MyDbHelper(getActivity(), "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);
        listado = new ArrayList<>(categoryDao.getall());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));
        adapter = new MyRecyclerViewAdapterCategories(getActivity(), listado);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        MyDbHelper helperUser = new MyDbHelper(getActivity(), "user");
        SQLiteDatabase db1 = helperUser.getWritableDatabase();
        UserDao userDao = new UserDao(db1);
        user = userDao.get();
        return view;
    }

    @Override
    public void onClick(View v) {
        MyDbHelper helper = new MyDbHelper(getActivity(), "categories");
        SQLiteDatabase db = helper.getWritableDatabase();
        CategoryDao categoryDao = new CategoryDao(db);
        for (int tr = 0; tr<listado.size(); tr++){
            categoryDao.update(listado.get(tr));
        }

        final String json = new Gson().toJson(listado);

        String url ="http://redoff.bithive.cloud/ws/categories_save";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

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
    }


    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("algo", "item click");
    }
}
