package com.example.kiranhk.ngo;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Kiran H K on 02-Apr-17.
 */

public class ngo extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
