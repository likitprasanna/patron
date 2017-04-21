package com.ngo_request.ngo_request;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    GoogleMap mGoogleMap;
    double mLatitude[];
    double mLongitude[];
    double lat, longi;
    private ProgressDialog progressDialog;
    private TextView try_again;
    private Button request;
    private TextView noof;
    private List<ListItems> mContentItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    double mLatitude2, mLongitude2;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }
        return true;
    }

    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noof = (TextView) findViewById(R.id.noof);

        try_again = (TextView) findViewById(R.id.try_again);

        if (try_again != null) {
            try_again.setVisibility(View.GONE);
        }
        request = (Button) findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showPD();

                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                    StringRequest jsObjRequest = new StringRequest(Request.Method.POST, "http://freelance.gear.host/ngo_project/ngo_notification.php", new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE", response);


                            SharedPreferences.Editor put = getSharedPreferences("REQ", MODE_PRIVATE).edit();
                            put.putBoolean("stat", false);
                            put.putBoolean("status", true);

                            put.putString("name", "");
                            put.putString("phno", "");
                            put.commit();

                            hidePD();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePD();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            SharedPreferences edit = getSharedPreferences("FCM", MODE_PRIVATE);
                            String token = edit.getString("token", "");

                            params.put("user_latitude", lat + "");
                            params.put("user_longitude", longi + "");
                            params.put("user_fcm_token", token);

                            return params;
                        }

                    };
                    queue.add(jsObjRequest);
                }

        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerAdapter(this, mContentItems);
        mRecyclerView.setAdapter(adapter);



        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {

            GPSTracker gps = new GPSTracker(this);

            if (gps.canGetLocation()) {
                SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mGoogleMap = fragment.getMap();
                mGoogleMap.setMyLocationEnabled(true);


                lat = gps.getLatitude();
                longi = gps.getLongitude();

                LatLng location = new LatLng(lat, longi);


                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                RequestQueue queue = Volley.newRequestQueue(this);

                StringRequest jsObjRequest = new StringRequest(Request.Method.POST, "http://freelance.gear.host/ngo_project/ngo_nearby.php", new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);

                        try {


                            Log.d("Response", response);
                            JSONObject jObj = new JSONObject(response);
                            JSONArray feedArray = jObj.getJSONArray("feed");


                            mLatitude = new double[feedArray.length()];
                            mLongitude = new double[feedArray.length()];
                            if (feedArray.length() == 0) {
                                try_again.setVisibility(View.VISIBLE);

                            }

                            noof.setText("No. of NGO's : " + feedArray.length());
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                mLatitude[i] = Double.parseDouble(feedObj.getString("latitude"));
                                mLongitude[i] = Double.parseDouble(feedObj.getString("longitude"));

                                ListItems item = new ListItems();
                                item.setName(feedObj.getString("ngo_name"));
                                mContentItems.add(item);
                                MarkerOptions markerOptions = new MarkerOptions();
                                double lat = mLatitude[i];

                                double lng = mLongitude[i];

                                LatLng latLng = new LatLng(lat, lng);

                                markerOptions.position(latLng);

                                markerOptions.title(feedObj.getString("ngo_name"));

                                mGoogleMap.addMarker(markerOptions);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_latitude", lat + "");
                        params.put("user_longitude", longi + "");

                        return params;
                    }

                };
                queue.add(jsObjRequest);

            } else {

                gps.showSettingsAlert();

            }
        }
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                LatLng location = new LatLng(mLatitude[position], mLongitude[position]);


                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void showPD() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Requesting NGO, Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    private void hidePD() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
