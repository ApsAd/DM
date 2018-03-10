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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Result extends AppCompatActivity {
    static int k = 0;
    HashMap<String, float[]> route1 = new HashMap<String, float[]>();
    HashMap<String, float[]> route2 = new HashMap<String, float[]>();
    HashMap<String, float[]> route3 = new HashMap<String, float[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final String[] ListOfCF = new String[2];
        Bundle b = getIntent().getExtras();
        ArrayList<ArrayList<String>> listOfLists = (ArrayList<ArrayList<String>>) b.getSerializable("listOfLists");
        //Log.d("route 1",listOfLists.get(0)+" "+listOfLists.get(5)+" "+listOfLists.get(10));
        String[] route11;



        for (int i = 0; i < 2; i++) {
            for (int j =i; j <= i + 10; j += 5) {
                //route11 = listOfLists.get(j).split(",");
                float[] routelatln = new float[2];
                routelatln[0] = Float.parseFloat(listOfLists.get(j).get(0));
                routelatln[1] = Float.parseFloat(listOfLists.get(j).get(1));
                Log.d("mylatln",routelatln[0]+" "+routelatln[1]);
                if (i == 0) {
                    route1.put(listOfLists.get(j).get(3), routelatln);
                } else {
                    route2.put(listOfLists.get(j).get(3), routelatln);
                }
            }
        }
        Set routeset1 = route1.keySet();
        for (Iterator in = routeset1.iterator(); in.hasNext(); ) {
            String key = (String) in.next();
            float[] value = route1.get(key);
            Log.d("Route1:", key + "lat " + value[0]+"long "+value[1]);
        }
        Set routeset2 = route2.keySet();
        for (Iterator in = routeset2.iterator(); in.hasNext(); ) {
            String key = (String) in.next();
            float[] value = route2.get(key);
            Log.d("Route2:", key + "lat " + value[0]+"long "+value[1]);
        }

        try {
            Log.d("Inside try123","hello");
            // final String[] final_routename = {""};
            //final String[] final_routeloc = {""};
            String email=b.getString("email");
            Log.d("Result email",email);
            final String password=b.getString("password");
            Log.d("Result pass",password);
            final float lat=b.getFloat("curlat");
            final float lon=b.getFloat("curlon");
            final String curlat = Float.toString(lat); //to be replaced by gps code
            final String curlon = Float.toString(lon);
            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            final String url = "http://192.168.1.4:5000/clusteringCollaborative";

            JSONObject userData = new JSONObject();

            userData.put("email",email);
            userData.put("password",password);
            userData.put("curlat",lat);
            userData.put("curlon",lon);

            final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, userData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {



                    try {
                        Log.d("response from server: ", (String) response.get(String.valueOf("finalroutename")));
                        ListOfCF[0] = (String) response.get(String.valueOf("finalroutename"));
                        ListOfCF[1] = (String) response.get(String.valueOf("finalrouteloc"));
                        Log.d("response from server: ", (String) response.get(String.valueOf("finalrouteloc")));
                        String[] cfroutename=ListOfCF[0].split(",");
                        String[] cfroutelatlon=ListOfCF[1].split(",");

                        Log.d("Length of name", String.valueOf(cfroutename.length));

                        Log.d("Length of lat", String.valueOf(cfroutelatlon.length));

                        for(int i = 0; i<cfroutename.length; i++){
                            float[] cflatln = new float[2];
                            Log.d("inside loop i,k",i+""+k);
                            cflatln[0]= Float.parseFloat(cfroutelatlon[k]);
                            cflatln[1]= Float.parseFloat(cfroutelatlon[k+1]);
                            k=k+2;
                            route3.put(cfroutename[i],cflatln);
                        }
                        if (k==cfroutelatlon.length) k=0;
                        Set routeset3 = route3.keySet();
                        for (Iterator in = routeset3.iterator(); in.hasNext(); ) {
                            String key = (String) in.next();
                            float[] value = route3.get(key);
                            Log.d("Route3:", key + "lat " + value[0]+"long "+value[1]);
                        }

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("route1",route1);
                        bundle.putSerializable("route2",route2);
                        bundle.putSerializable("route3",route3);
                        Intent intent=new Intent(getApplicationContext(),Routes.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Inside Response:", "error");
                }
            });
            queue.add(req);
        }catch(Exception e){

        }




    }
}

