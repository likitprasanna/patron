package com.ngo_request.ngo_request.Firebase_FCM;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d("Token", token);
        SharedPreferences.Editor edit=getSharedPreferences("FCM",MODE_PRIVATE).edit();
        edit.putString("token", token);
        edit.commit();

    }
}
