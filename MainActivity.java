package com.ngo_owner.ngo_owner;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    GoogleMap mGoogleMap;
    private TextView try_again;
    double longi,lat;
    private Button request,not_avail;
    private View view;

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

    private AlertDialog dd;

    public void dsms(){
        dd.dismiss();
    }
    @Override
    public void onBackPressed() {
        SharedPreferences stat = getSharedPreferences("Status", MODE_PRIVATE);
        Log.d("Status", stat.getBoolean("stat", false) + "");
        if (!stat.getBoolean("stat", false)) {
            finish();
        }else {
            dd = new AlertDialog.Builder(this)
                    .setTitle("Confirm Action")
                    .setMessage("Are you sure? you want to exit the application, By clicking yes you are discarding the request")
                    .setNegativeButton(R.string.Exit, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            dsms();
                        }
                    })
                    .setPositiveButton(R.string.GET, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferences.Editor stat = getSharedPreferences("Status", MODE_PRIVATE).edit();
                            stat.putBoolean("stat", false);
                            stat.commit();
                            finish();
                        }
                    }).create();


            dd.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dd.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    dd.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

                }
            });
            dd.show();
        }
    }

    double la,lo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final GPSTracker gps = new GPSTracker(this);




        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        } else {
            setContentView(R.layout.activity_main);



            try_again = (TextView) findViewById(R.id.try_again);
            try_again.setVisibility(View.GONE);
            request = (Button) findViewById(R.id.request);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPD();
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                    StringRequest jsObjRequest = new StringRequest(Request.Method.POST, "http://freelance.gear.host/ngo_project/user_notification.php", new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE", response);
                            request.setVisibility(View.GONE);
                            try_again.setVisibility(View.GONE);

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

                            final String android_id = Settings.Secure.getString(getContentResolver(),
                                    Settings.Secure.ANDROID_ID);

                            params.put("user_fcm_token", getIntent().getExtras().getString("token"));
                            params.put("device_id", android_id);

                            return params;
                        }

                    };
                    queue.add(jsObjRequest);
                }
            });

            not_avail = (Button) findViewById(R.id.not_avail);
            not_avail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            view = (View) findViewById(R.id.map);


            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
            if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            } else {

                SharedPreferences stat = getSharedPreferences("Status", MODE_PRIVATE);
                Log.d("Status",stat.getBoolean("stat", false)+"");
                if (!stat.getBoolean("stat", false)) {
                    try_again.setVisibility(View.VISIBLE);
                    request.setVisibility(View.GONE);
                    not_avail.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);

                } else {


                    if (gps.canGetLocation()) {
                        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mGoogleMap = fragment.getMap();
                        mGoogleMap.setMyLocationEnabled(true);

                        /*lat = gps.getLatitude();
                        longi = gps.getLongitude();

                        LatLng location6 = new LatLng(lat, longi);


                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location6));*/

                        if(getIntent().hasExtra("lat")) {
                             la = Double.parseDouble(getIntent().getExtras().getString("lat"));
                             lo = Double.parseDouble(getIntent().getExtras().getString("longi"));
                        }

                        LatLng location = new LatLng(la, lo);
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(location);

                        markerOptions.title("Request Location");

                        mGoogleMap.addMarker(markerOptions);



                    } else {

                        gps.showSettingsAlert();

                    }
                }
            }

        }
    }


    private ProgressDialog progressDialog;
    private void showPD() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
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
