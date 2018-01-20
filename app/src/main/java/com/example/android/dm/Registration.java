package com.example.android.dm;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;



/**
 * Created by ADITYA on 1/20/2018.
 */
public class Registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        EditText UserName=(EditText)findViewById(R.id.UserName);
        EditText Password=(EditText)findViewById(R.id.Password);
        Button Login=(Button)findViewById(R.id.Register);
        Bundle bundle = new Bundle();
        bundle.putString("Username", UserName.toString());
        bundle.putString("Password", Password.toString());
        Intent myIntent = new Intent();
        myIntent.setClass(getBaseContext(), Registration1.class);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
        //Add the bundle into myIntent for referencing variables

     }
    }
