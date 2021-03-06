package com.example.android.dm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class DPP extends AppCompatActivity {
    ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
    float lat;
    float lon;
    String email,password;
    String curlat,curlon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpp);

        Bundle b = getIntent().getExtras();
        final String preference=b.getString("preferences");
        lat=b.getFloat("curlat");
        lon=b.getFloat("curlon");
        curlat = Float.toString(lat); //to be replaced by gps code
        curlon = Float.toString(lon);
        email=b.getString("email");
        password=b.getString("password");
        Log.d("pref,lat,lon",preference+" "+curlat+" "+curlon);
        String[] pref=preference.split(",");
        int i;
        Log.d("final prefernces",pref.toString());
        for (i = 0; i < pref.length; i++) {
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference mydb = db.child("dataset/" + pref[i] + "/");

            Log.d("curlat",curlat);
            Log.d("curlon",curlon);
            final int finalI = i;
            final int finalI1 = i;
            final int finalI2 = i;
            ValueEventListener x = new ValueEventListener() {
                final TreeMap<Double, List<String>> place = new TreeMap<Double, List<String>>();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = 0;

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        count++;
                        if (count == 20) break;
                        final List<String> detail = new ArrayList<String>();
                        final Double[] dist = {0.0};
                        final int[] flag1 = {0};
                        Log.d("Inside ondatachange",child.toString());
                        String key = child.getKey().toString();
                        String[] key1 = key.split("=");
                        String key2 = "";
                        for (int i = 0; i < key1[0].length(); i++) {
                            if (key1[0].charAt(i) != '{')
                                key2 += key1[0].charAt(i); //retrieving the unique key of each data element
                        }
                        DatabaseReference db1 = mydb.child(key2 + "/result/geometry/location");
                        DatabaseReference db2 = mydb.child(key2 + "/result/rating");
                        DatabaseReference db3 = mydb.child(key2 + "/result/name");
                        db1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String loc = dataSnapshot.getValue().toString();
                                //retrieving latitude and longitude
                                String[] latlon = loc.split(",");
                                String[] lat1 = latlon[0].split("=");
                                String[] lon1 = latlon[1].split("=");
                                String lat = lat1[1];
                                String lon2 = lon1[1];
                                String lon = "";
                                for (int i = 0; i < lon1[1].length(); i++) {
                                    if (lon1[1].charAt(i) != '}')
                                        lon += lon1[1].charAt(i);
                                }
                                dist[0] = haversineformula(lat, lon, curlat, curlon); //calculating distance
                                if (dist[0] < 65) {
                                    flag1[0] = 1;
                                    detail.add(lat);
                                    detail.add(lon);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        db2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String rat;
                                if (dataSnapshot.exists()) {
                                    rat = dataSnapshot.getValue().toString();
                                } else {
                                    rat = "0";
                                }
                                if (flag1[0] == 1) {
                                    detail.add(rat);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        db3.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String name;
                                if (dataSnapshot.exists()) {
                                    name = dataSnapshot.getValue().toString();
                                } else {
                                    name = "Unknown";
                                }
                                if (flag1[0] == 1) {
                                    detail.add(name);
                                    place.put(dist[0], detail); //adding the place details and distance into map
                                }
                                flag1[0] = 0;

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Log.d("hi2", String.valueOf(place.size())+pref[finalI1]);
                            for (Map.Entry<Double, List<String>> entry : place.entrySet()) {
                                Double key = entry.getKey();
                                List<String> values = entry.getValue();
                                //Log.d("Key = ", key.toString());
                                //Log.d("Values = ", values.toString());

                            }
                            int count = 0;
                            TreeMap<Double, List<String>> target = new TreeMap<Double, List<String>>();
                            for (Map.Entry<Double, List<String>> entry:place.entrySet()) {
                                if (count >= 5) break;

                                target.put(entry.getKey(), entry.getValue());
                                count++;
                            }

                            sort(target, finalI2);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }




                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            };
            mydb.addValueEventListener(x);

        }
        //  Log.d("Final list",listOfLists.toString());
       /*
*/

    }
    private Double haversineformula(String lat, String lon, String curlat, String curlon) {
        final int R = 6371; // Radious of the earth
        Double lat1 = Double.parseDouble(curlat);
        Double lon1 = Double.parseDouble(curlon);
        Double lat2 = Double.parseDouble(lat);
        Double lon2 = Double.parseDouble(lon);
        Double latDistance = toRad(lat2 - lat1);
        Double lonDistance = toRad(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c;

        return distance;
    }

    private Double toRad(Double value) {
        return value * Math.PI / 180;
    }
    private void sort(TreeMap<Double, List<String>> place,int i) {
        SortedSet<Map.Entry<Double, List<String>>> sortedEntries = new TreeSet<Map.Entry<Double, List<String>>>(
                new Comparator<Map.Entry<Double, List<String>>>() {
                    @Override
                    public int compare(Map.Entry<Double, List<String>> t1, Map.Entry<Double, List<String>> t2) {
                        List<String> values1 = t1.getValue();
                        List<String> values2= t2.getValue();
                        //int res =values1.get(2).compareTo(values2.get(2));
                        if(Float.parseFloat(values1.get(2))>Float.parseFloat(values2.get(2)))
                            return -1;
                        else
                            return 1;

                    }

                }
        );
        sortedEntries.addAll(place.entrySet());

        for (Map.Entry<Double, List<String>> sortedEntry : sortedEntries) {
            Double key = sortedEntry.getKey();
            ArrayList<String> values = (ArrayList<String>) sortedEntry.getValue();
            listOfLists.add(values);
            Log.d("SortedKey = ", key.toString());
            Log.d("SortedValues = ", values.toString());
        }
        Log.d("Listoflist ouside if",listOfLists.toString()+"i: "+i);
        if(listOfLists.size()==15) {
            Log.d("Listoflist", listOfLists.toString());
            Bundle bundle=new Bundle();
            bundle.putFloat("curlat",lat);
            bundle.putFloat("curlon",lon);
            bundle.putString("email",email);
            bundle.putString("password",password);
            bundle.putSerializable("listOfLists",listOfLists);
            Intent myIntent = new Intent();
            Log.d("Email in dpp",email);
            myIntent.setClass(getBaseContext(),Result.class);
            myIntent.putExtras(bundle);
            startActivity(myIntent);

        }


    }
}
