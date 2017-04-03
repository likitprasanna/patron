package com.example.kiranhk.ngo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabasereference;
    public List<Post> postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabasereference = FirebaseDatabase.getInstance().getReference().child("Map");
        mDatabasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> objectMap = (HashMap<String, Object>)dataSnapshot.getValue();
                postList = new ArrayList<Post>();
                for(Object obj : objectMap.values()) {
                    if(obj instanceof Map) {
                        Map<String ,Object > mapobj = (Map<String, Object >) obj;
                        Post post = new Post();
                        post.setLatitude(mapobj.get("latitude").toString());
                        post.setLatitude(mapobj.get("longitude").toString());
                        postList.add(post);
                        Log.e("Latitude " ,mapobj.get("latitude").toString());
                        Log.e("Longitude " ,mapobj.get("longitude").toString());

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class Post {

        public String latitude;
        public String longitude;

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

}
