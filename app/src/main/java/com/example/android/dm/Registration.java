package com.example.android.dm;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
public class Registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        final EditText UserName=(EditText)findViewById(R.id.UserName);
        final EditText Password=(EditText)findViewById(R.id.Password);
        Button Login=(Button)findViewById(R.id.Register);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final String url = "http://192.168.1.3:5000/login";
                    JSONObject userData = new JSONObject();
                    userData.put("email",UserName.getEditableText().toString());
                    userData.put("password",Password.getEditableText().toString());

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, userData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                               // Log.d("response",response.get("status").toString());
                                if(response.getBoolean("status")== true){
                                    Intent myIntent = new Intent();
                                    myIntent.setClass(getBaseContext(), MainActivity.class);
                                    startActivity(myIntent);
                                }
                                else{
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Username", UserName.getEditableText().toString());
                                    bundle.putString("Password", Password.getEditableText().toString());

                                    Intent myIntent = new Intent();
                                    myIntent.setClass(getBaseContext(), Registration1.class);
                                    myIntent.putExtras(bundle);
                                    startActivity(myIntent);
                                }


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


            }
        });

        //Add the bundle into myIntent for referencing variables

     }
    }
