package com.example.kiranhk.donate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FIRST extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }
    public void login(View view)
    {
        Intent intent3 = new Intent(FIRST.this,Login1.class);
        startActivity(intent3);
    }
    public void donating(View view)
    {
        Intent intent2 = new Intent(FIRST.this,MapsActivity.class);
        startActivity(intent2);
    }
}
