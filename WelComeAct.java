package com.ngo_owner.ngo_owner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class WelComeAct extends AppCompatActivity {

    private Button find;
    private EditText name,phno;
    private String lat,lon;
    private boolean stat=false;

    public  boolean hasPermissions(Context context, String... permissions) {
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
        super.onCreate(savedInstanceState);

        if (!hasPermissions(WelComeAct.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(WelComeAct.this, PERMISSIONS, PERMISSION_ALL);
        }



        setContentView(R.layout.activity_wel_come);



        SharedPreferences get = getSharedPreferences("IS_FIRST", MODE_PRIVATE);
        if (get.getBoolean("status", false)) {
            startActivity(new Intent(WelComeAct.this, MainActivity.class));
            finish();
        }

        name = (EditText) findViewById(R.id.ed_name);
        phno = (EditText) findViewById(R.id.ed_phno);

        find = (Button) findViewById(R.id.find_rec);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final GPSTracker gps = new GPSTracker(WelComeAct.this);


                if (!stat) {
                    if (gps.canGetLocation()) {
                        lat=gps.getLatitude()+"";
                        lon=gps.getLongitude()+"";
                        stat=true;
                        Toast.makeText(WelComeAct.this,"Press again to enter the application",Toast.LENGTH_LONG).show();
                    }else{
                        stat=false;
                        gps.showSettingsAlert();

                    }
                } else {



                    if (gps.canGetLocation()) {
                        String names = name.getText().toString();
                        String phnos = phno.getText().toString();

                        if (names.trim().equals("") || phnos.trim().equals("")) {
                            Toast.makeText(WelComeAct.this, "Please enter your name", Toast.LENGTH_LONG).show();
                        } else {
                            SharedPreferences.Editor put = getSharedPreferences("IS_FIRST", MODE_PRIVATE).edit();
                            put.putBoolean("status", true);
                            put.putString("name", names);
                            put.putString("phno", phnos);
                            put.putString("lat", lat);
                            put.putString("longi", lon);

                            put.commit();
                            FirebaseMessaging.getInstance().subscribeToTopic("test");
                            FirebaseInstanceId.getInstance().getToken();


                            RequestQueue queue = Volley.newRequestQueue(WelComeAct.this);

                            StringRequest jsObjRequest = new StringRequest(Request.Method.POST, "http://freelance.gear.host/ngo_project/ngo_upload_info.php", new Response.Listener<String>() {


                                @Override
                                public void onResponse(String response) {
                                    Log.d("RESPONSE", response);



                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();

                                    final String android_id = Settings.Secure.getString(getContentResolver(),
                                            Settings.Secure.ANDROID_ID);

                                    SharedPreferences edit=getSharedPreferences("FCM", MODE_PRIVATE);
                                    String token=
                                    edit.getString("token", "");
                                    SharedPreferences put = getSharedPreferences("IS_FIRST", MODE_PRIVATE);
                                    Log.d(put.getString("name", ""),put.getString("phno", ""));
                                    params.put("fcm_id", token);
                                    params.put("device_id", android_id);
                                    params.put("ngo_name",put.getString("name", ""));
                                    params.put("contact_number", put.getString("phno", ""));
                                    params.put("latitude", put.getString("lat", ""));
                                    params.put("longitude", put.getString("longi", ""));

                                    return params;
                                }

                            };
                            queue.add(jsObjRequest);
                            startActivity(new Intent(WelComeAct.this, MainActivity.class));
                            finish();
                        }
                    }else{
                        gps.showSettingsAlert();

                    }
                }

            }
        });

    }
}
