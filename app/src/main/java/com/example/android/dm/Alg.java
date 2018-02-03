package com.example.android.dm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Created by ADITYA on 1/20/2018.
 */
public class Alg extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alg);
        Bundle bundle = this.getIntent().getExtras();
       // HashMap<Double,List<String>> place = new HashMap<Double,List<String>>();
        SortedSet<Map.Entry<Double,List<String>>> sortedEntries;
        if(bundle != null) {
           sortedEntries= (SortedSet<Map.Entry<Double, List<String>>>) bundle.getSerializable("sorted");
            for (Map.Entry<Double, List<String>> sortedEntry : sortedEntries) {
                Double key = sortedEntry.getKey();
                List<String> values = sortedEntry.getValue();
                Log.d("SortedKey = ", key.toString());
                Log.d("SortedValues = ", values.toString());
            }
        }
        /*for (Map.Entry<Double, List<String>> entry : place.entrySet()) {
            Double key = entry.getKey();
            List<String> values = entry.getValue();
            Log.d("Key = " , key.toString());
            Log.d("Values = " , values.toString());
            Bundle b=new Bundle();
            b.putSerializable("place",place);
            Intent i=new Intent(getApplicationContext(),Alg.class);
            i.putExtra("Places",b);
            startActivity(i);
            //comment
        }*/

    }
}
