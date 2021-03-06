package layout;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.dm.R;
import com.example.android.dm.Rating;
import com.example.android.dm.Routes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class tab_fragment_1 extends Fragment implements View.OnClickListener, OnMapReadyCallback, DirectionCallback {

    private Button btnRequestDirection;
    private Button insert;
    private GoogleMap googleMap;
    HashMap<String, float[]> route1;
    private String serverKey = "AIzaSyD2YT_mb5cQbtK67fW_zeYgEPSjawVC2TM";
    private LatLng park = new LatLng(41.8838111, -87.6657851);
    private LatLng shopping = new LatLng(41.8766061, -87.6556908);
    private LatLng dinner = new LatLng(41.8909056, -87.6467561);
    private LatLng gallery = new LatLng(41.9007082, -87.6488802);
    LatLng[] x=new LatLng[3];
    String[] keys=new String[3];
    String email,routename="",routelatlon="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      route1= (HashMap<String, float[]>)getArguments().getSerializable("route1");
        email=getArguments().getString("email");
        Set routeset1 = route1.keySet();
        int i=0,j=0;
        for (Iterator in = routeset1.iterator(); in.hasNext(); ) {
            String key = (String) in.next();
            keys[j++]=key;
            float[] value = route1.get(key);
            x[i++]=new LatLng(value[0],value[1]);
            Log.d("Route1pageradapter:", key + "lat " + value[0]+"long "+value[1]);
            routelatlon+=value[0]+","+value[1]+",";
            routename+=key+",";
        }
        for(i=0;i<3;i++){
            Log.d("latlngarr",x[i].toString());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);
        btnRequestDirection = (Button) rootView.findViewById(R.id.btn_request_direction);
        insert=(Button)rootView.findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    Log.d("myemail",email);
                    Log.d("myroutename",routename);
                    Log.d("myroutelatln",routelatlon);
                    try {
                    final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    final String url = "http://192.168.1.4:5000/updateRoutes";

                    JSONObject userData = new JSONObject();
                    userData.put("name","dummy");
                    userData.put("email",email);
                    userData.put("routename",routename);
                    userData.put("routelatlon",routelatlon);

                    final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, userData, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getActivity().getApplicationContext(),"Updated!Thank you:)",Toast.LENGTH_SHORT).show();
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Inside Response:", "error");
                        }
                    });
                    queue.add(req);
                    Bundle b=new Bundle();
                        b.putString("email",email);
                        Intent i=new Intent(getActivity().getApplicationContext(), Rating.class);
                        i.putExtras(b);
                        startActivity(i);
                }catch(Exception e){

                }
            }
        });
        btnRequestDirection.setOnClickListener((View.OnClickListener) this);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            int legCount = route.getLegList().size();
            for (int index = 0; index < legCount; index++) {
                Leg leg = route.getLegList().get(index);
                googleMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination()).title(keys[index]));
                if (index == legCount - 1) {
                    googleMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination()).title(keys[2]));
                }
                List<Step> stepList = leg.getStepList();
                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(getActivity().getApplicationContext(), stepList, 5, Color.RED, 3, Color.BLUE);
                for (PolylineOptions polylineOption : polylineOptionList) {
                    googleMap.addPolyline(polylineOption);
                }
            }
            setCameraWithCoordinationBounds(route);
            btnRequestDirection.setVisibility(View.GONE);
        }

    }
    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
            Toast.makeText(getActivity().getApplicationContext(),"Hello done",Toast.LENGTH_SHORT).show();
        }
    }
    public void requestDirection() {
        Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
        GoogleDirection.withServerKey(serverKey)
                .from(x[0])
                .and(x[1])
                //.and(dinner)
                .to(x[2])
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
