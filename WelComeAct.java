package com.ngo_request.ngo_request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class WelComeAct extends AppCompatActivity {

    private Button find;
    private EditText name;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

        SharedPreferences get = getSharedPreferences("IS_FIRST", MODE_PRIVATE);
        if (get.getBoolean("status", false)) {
            startActivity(new Intent(WelComeAct.this, MainActivity.class));
            finish();
        }

        name = (EditText) findViewById(R.id.ed_name);
        find = (Button) findViewById(R.id.find_rec);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(WelComeAct.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(WelComeAct.this, PERMISSIONS, PERMISSION_ALL);
                } else {
                    String names = name.getText().toString();
                    if (names.trim().equals("")) {
                        Toast.makeText(WelComeAct.this, "Please enter your name", Toast.LENGTH_LONG).show();
                    } else {
                        SharedPreferences.Editor put = getSharedPreferences("IS_FIRST", MODE_PRIVATE).edit();
                        put.putBoolean("status", true);
                        put.putString("name", names);
                        put.commit();
                        startActivity(new Intent(WelComeAct.this, MainActivity.class));
                        finish();
                    }
                }

            }
        });

    }
}
