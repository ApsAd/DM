package com.example.android.dm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    float lat;
    float lon;
    String[] finpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle b = getIntent().getExtras();
        lat = (float) 13.0429;
        lon = (float) 80.2739;
        final String email=b.getString("email");
        final String password=b.getString("password");
        // Get the location manager
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location;
        /*try {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Log.d("Provider ", provider);
                onLocationChanged(location);
            } else {
                Log.d("Location not available", "yes");
                Log.d("Location not available", "yes");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }*/

        try {
            Log.d("whatsup", "yes");

            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            final String url = "http://192.168.1.4:5000/retrievePreferences";
            JSONObject userData = new JSONObject();
            userData.put("email",email);
            userData.put("password",password);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, userData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("hi","hi");
                    try {
                        // Log.d("response",response.get("status").toString());
                        String pref=response.getString("pref");
                        Log.d("preferences",pref);
                        String[] finpref=pref.split(",");
                        Log.d("finpref",finpref[0]+" "+finpref[1]+" "+finpref[2]);
                        Bundle bundle = new Bundle();
                        bundle.putString("preferences",pref);
                     /*   lat = (float) 13.0429;
                        lon = (float) 80.2739;*/
                        bundle.putFloat("curlat",lat);
                        bundle.putFloat("curlon",lon);
                        bundle.putString("email",email);
                        bundle.putString("password",password);
                        Intent myIntent = new Intent();
                        myIntent.setClass(getBaseContext(), DPP.class);
                        myIntent.putExtras(bundle);
                        startActivity(myIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", "Main Activity Error");
                }
            });
            queue.add(req);
        }catch(Exception e){

        }


    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (float) (location.getLatitude());
        lon = (float) (location.getLongitude());
        Log.d("Laatitude", String.valueOf(lat));
        Log.d("Longitude", String.valueOf(lon));
        Toast.makeText(this, " location! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}