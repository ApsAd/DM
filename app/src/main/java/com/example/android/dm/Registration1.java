package com.example.android.dm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ADITYA on 1/20/2018.
 */
public class Registration1 extends AppCompatActivity {
    public static DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration1);
        final EditText name=(EditText)findViewById(R.id.Name);
        final EditText age=(EditText)findViewById(R.id.Age);
        final EditText address=(EditText)findViewById(R.id.Address);
        final EditText phoneno=(EditText)findViewById(R.id.Phoneno);
        final EditText gender=(EditText)findViewById(R.id.Gender);
        final CheckBox amuzementpark=(CheckBox)findViewById(R.id.amuzementpark);
        final CheckBox mall=(CheckBox)findViewById(R.id.mall);
        final CheckBox temple=(CheckBox)findViewById(R.id.temple);
        final CheckBox mosque=(CheckBox)findViewById(R.id.mosque);
        final CheckBox church=(CheckBox)findViewById(R.id.church);
        final CheckBox bakery=(CheckBox)findViewById(R.id.bakery);
        final CheckBox restaurant=(CheckBox)findViewById(R.id.restaurants);
        Button reg=(Button)findViewById(R.id.pref);
        Bundle b = getIntent().getExtras();
        final String email=b.getString("Username");
        final String password=b.getString("Password");
        final StringBuilder preferences1=new StringBuilder(" ");
        amuzementpark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)amuzementpark).isChecked()){ Log.d("Ps","hi"); preferences1.append("Amusement Parks,");}
            }
        });
        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)bakery).isChecked()){ Log.d("Ps","hi"); preferences1.append("Bakeries,");}
            }
        });
        church.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)church).isChecked()){ Log.d("Ps","hi"); preferences1.append("Churches,");}
            }
        });
        mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)mall).isChecked()){ Log.d("Ps","hi"); preferences1.append("Malls,");}
            }
        });
        mosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)mosque).isChecked()){ Log.d("Ps","hi"); preferences1.append("Mosques,");}
            }
        });
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)restaurant).isChecked()){ Log.d("Ps","hi"); preferences1.append("Restaurants,");}
            }
        });
        temple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)temple).isChecked()){ Log.d("Ps","hi"); preferences1.append("Temples,");
                    Log.d("Preferences1", preferences1.toString());
                }
            }
        });

        final String preferences=preferences1.toString();
        db = new DatabaseHelper(this);

        Log.d("Preferences",preferences1.toString());
       // db.deleteAllUsers();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final String url = "http://192.168.1.4:5000/insertUserData";
                    JSONObject userData = new JSONObject();
                    userData.put("name",name.getEditableText().toString());
                    userData.put("email",email.toString());
                    userData.put("password",password.toString());
                    userData.put("age",age.getEditableText().toString());
                    userData.put("gender",gender.getEditableText().toString());
                    userData.put("address",address.getEditableText().toString());
                    userData.put("phoneno",phoneno.getEditableText().toString());
                    userData.put("pref",preferences1.toString());

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, userData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Log.d("response", response.getString("user"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "error");
                        }
                    });
                    queue.add(req);
                }catch(Exception e){

                }

                //db.insertRecord(name.getEditableText().toString(),email.toString(),password.toString(),age.getEditableText().toString(),gender.getEditableText().toString(),address.getEditableText().toString(),phoneno.getEditableText().toString(),preferences1.toString());
                //Intent in=new Intent(getApplicationContext(), DisplayActivity.class);
                //startActivity(in);
            }
        });


    }
}
