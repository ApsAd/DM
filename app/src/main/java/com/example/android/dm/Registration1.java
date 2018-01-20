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
        CheckBox amuzementpark=(CheckBox)findViewById(R.id.amuzementpark);
        CheckBox mall=(CheckBox)findViewById(R.id.mall);
        CheckBox temple=(CheckBox)findViewById(R.id.temple);
        CheckBox mosque=(CheckBox)findViewById(R.id.mosque);
        CheckBox church=(CheckBox)findViewById(R.id.church);
        CheckBox bakery=(CheckBox)findViewById(R.id.bakery);
        CheckBox restaurant=(CheckBox)findViewById(R.id.restaurants);
        Button reg=(Button)findViewById(R.id.pref);
        Bundle b = getIntent().getExtras();
        final String email=b.getString("Username");
        final String password=b.getString("Password");
        String preferences1="";

        if(amuzementpark.isChecked()){ Log.d("Ps","hi"); preferences1+="Amusement Parks,";}
        if(bakery.isChecked()) preferences1+="Bakeries,";
        if(church.isChecked()) preferences1+="Churches,";
        if(mall.isChecked()) preferences1+="Malls,";
        if(mosque.isChecked()) preferences1+="Mosques,";
        if(restaurant.isChecked()) preferences1+="Restaurants,";
        if(temple.isChecked()) preferences1+="Temples,";
        final String preferences=preferences1;
        db = new DatabaseHelper(this);
        Log.d("Name",name.toString());
        Log.d("Preferences",preferences);
      
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertRecord(name.getEditableText().toString(),email.toString(),password.toString(),age.getEditableText().toString(),gender.getEditableText().toString(),address.getEditableText().toString(),phoneno.getEditableText().toString(),preferences);
                Intent in=new Intent(getApplicationContext(), DisplayActivity.class);
                startActivity(in);
            }
        });


    }
}
