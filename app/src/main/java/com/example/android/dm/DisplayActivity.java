package com.example.android.dm;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ADITYA on 1/20/2018.
 */
public class DisplayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        final TextView t = (TextView) findViewById(R.id.data);

        Cursor cursor = Registration1.db.getAllUsers();
        String ans = "";
        while (cursor.moveToNext()) {
            ans += cursor.getString(cursor.getColumnIndex("userid"));
            ans += "\n";
            ans += cursor.getString(cursor.getColumnIndex("name"));
            ans += "\n";
            ans += cursor.getString(cursor.getColumnIndex("email"));
            ans += "\n";
            ans += cursor.getString(cursor.getColumnIndex("age"));
            ans += "\n";
            ans += cursor.getString(cursor.getColumnIndex("gender"));
            ans += "\n";
            ans += cursor.getString(cursor.getColumnIndex("phonenumber"));
            ans += "\n";
            ans += cursor.getString(cursor.getColumnIndex("preference"));
            ans += "\n";
        }
        t.setText(ans);
        // Instantiate the RequestQueue.


// prepare the Request
       /* StringRequest req=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("our reponse", response);
                queue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error listener",error.toString());
                queue.stop();
            }
        });
        queue.add(req);*/
        try {
            final RequestQueue queue = Volley.newRequestQueue(this);
            final String url = "http://192.168.1.4:5000/";
            JSONObject userData = new JSONObject();
            userData.put("name", "aps");
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
    }
}
