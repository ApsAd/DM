package com.example.android.dm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ADITYA on 1/20/2018.
 */
public class Alg extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alg);
        Bundle bundle = this.getIntent().getExtras();
        HashMap<Double,List<String>> place = new HashMap<Double,List<String>>();
        if(bundle != null) {
            place = (HashMap<Double, List<String>>) bundle.getSerializable("Places");
        }
        for (Map.Entry<Double, List<String>> entry : place.entrySet()) {
            Double key = entry.getKey();
            List<String> values = entry.getValue();
            Log.d("Key = " , key.toString());
            Log.d("Values = " , values.toString());
            Bundle b=new Bundle();
            b.putSerializable("place",place);
            Intent i=new Intent(getApplicationContext(),Alg.class);
            i.putExtra("Places",b);
            startActivity(i);
        }
    }
}
