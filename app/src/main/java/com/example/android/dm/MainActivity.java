package com.example.android.dm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference mydb = db.child("dataset/Amusement Parks/");
        final String curlat="13.05307";
        final String curlon="80.19310";
        ValueEventListener x=new ValueEventListener() {
            final HashMap<Double,List<String>> place = new HashMap<Double,List<String>>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    count++;
                    if(count==20) break;
                    final List<String> detail = new ArrayList<String>();
                    final Double[] dist ={0.0} ;
                    final int[] flag1={0};


                    //Log.d("cnt", Long.toString( dataSnapshot.getChildrenCount()));
                    String key = child.getKey().toString();
                    //Log.d("key",key);
                    String[] key1 = key.split("=");
                    // Log.d("key1", key1[0]);
                    String key2 = "";
                    for (int i = 0; i < key1[0].length(); i++) {
                        if (key1[0].charAt(i) != '{')
                            key2 += key1[0].charAt(i);
                    }
                    //Log.d("key2", key2);
                    DatabaseReference db1 = mydb.child(key2 + "/result/geometry/location");
                    DatabaseReference db2 = mydb.child(key2 + "/result/rating");
                    DatabaseReference db3 = mydb.child(key2 + "/result/name");
                    db1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String loc = dataSnapshot.getValue().toString();
                            String[] latlon=loc.split(",");
                            String[] lat1=latlon[0].split("=");
                            String[] lon1=latlon[1].split("=");
                            String lat=lat1[1];
                            String lon2=lon1[1];
                            String lon="";
                            for (int i = 0; i < lon1[1].length(); i++) {
                                if (lon1[1].charAt(i) != '}')
                                    lon+= lon1[1].charAt(i);
                            }


                            //    Log.d("latlon", lat+""+lon);
                            dist[0]= haversineformula(lat,lon,curlat,curlon);
                            if(dist[0]<15){
                                flag1[0] =1;
                                detail.add(lat);
                                detail.add(lon);
                                // Log.d("dist",dist[0].toString());
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

                                //  Log.d("Rating ", rat);
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

                                // Log.d("keys",dist[0].toString());
                                // Log.d("valuess",detail.toString());
                                place.put(dist[0],detail);
                               /* Log.d("hi", String.valueOf(place.size()));
                                for (Map.Entry<Double, List<String>> entry : place.entrySet()) {
                                    Double key = entry.getKey();
                                    List<String> values = entry.getValue();
                                    Log.d("Key = " , key.toString());
                                    Log.d("Values = " , values.toString());
                                }*/
                                //Log.d("Name ", name);
                            }
                            flag1[0]=0;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    db.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("hi2", String.valueOf(place.size()));
                            for (Map.Entry<Double, List<String>> entry : place.entrySet()) {
                                Double key = entry.getKey();
                                List<String> values = entry.getValue();
                                Log.d("Key = " , key.toString());
                                Log.d("Values = " , values.toString());

                            }
                            Bundle b=new Bundle();
                            b.putSerializable("place",place);
                            Intent i=new Intent(getApplicationContext(),Alg.class);
                            i.putExtra("Places",b);
                            startActivity(i);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }




            private Double haversineformula(String lat, String lon, String curlat, String curlon) {
                final int R = 6371; // Radious of the earth
                Double lat1 = Double.parseDouble(curlat);
                Double lon1 = Double.parseDouble(curlon);
                Double lat2 = Double.parseDouble(lat);
                Double lon2 = Double.parseDouble(lon);
                Double latDistance = toRad(lat2-lat1);
                Double lonDistance = toRad(lon2-lon1);
                Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                Double distance = R * c;

                return distance;
            }
            private Double toRad(Double value) {
                return value * Math.PI / 180;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        };
        mydb.addValueEventListener(x);

       // mydb.removeEventListener(x);



    }


}