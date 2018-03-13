package com.example.android.dm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Rating extends Activity {

    private RatingBar ratingBar1,ratingBar2,ratingBar3;
    private TextView txtRatingValue;
    private Button btnSubmit1,btnSubmit2,btnSubmit3,confirm;
    int qn1,qn2,qn3;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Bundle b=getIntent().getExtras();
        email=b.getString("email");
        Log.d("email in rating",email);

        addListenerOnRatingBar();
        addListenerOnButton();

    }

    public void addListenerOnRatingBar() {

        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
        ratingBar2.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
        ratingBar3.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });

    }

    public void addListenerOnButton() {

        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
        btnSubmit1 = (Button) findViewById(R.id.btnSubmit1);
        btnSubmit2 = (Button) findViewById(R.id.btnSubmit2);
        btnSubmit3 = (Button) findViewById(R.id.btnSubmit3);
        confirm = (Button) findViewById(R.id.confirm);

        //if click on me, then display the current rating value.
        btnSubmit1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                qn1=(int)ratingBar1.getRating();
                Toast.makeText(Rating.this,
                        String.valueOf(ratingBar1.getRating()),
                        Toast.LENGTH_SHORT).show();





            }

        });
        btnSubmit2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                qn2=(int)ratingBar2.getRating();
                Toast.makeText(Rating.this,
                        String.valueOf(ratingBar2.getRating()),
                        Toast.LENGTH_SHORT).show();

            }

        });
        btnSubmit3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                qn3=(int)ratingBar3.getRating();
                Toast.makeText(Rating.this,
                        String.valueOf(ratingBar3.getRating()),
                        Toast.LENGTH_SHORT).show();

            }

        });
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final String url = "http://192.168.1.4:5000/insertRating";

                    JSONObject userData = new JSONObject();

                    userData.put("email",email);
                    userData.put("qn1",qn1);
                    userData.put("qn2",qn2);
                    userData.put("qn3",qn3);


                    final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, userData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(),"Thank you:)",Toast.LENGTH_SHORT).show();
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
        });

    }
}