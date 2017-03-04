package com.example.kiranhk.donate;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.content.Intent;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    int i;
    private GoogleMap mMap;
    LatLng loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void zoom(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, -18));

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        LatLng loc = new LatLng(13.3438506, 77.1174518);
         mMap.addMarker(new MarkerOptions()
                .position(loc)
                .title("Avishkar NGO")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin))
                );

       /* LatLng loc1 = new LatLng(12.92783,77.63631);
        mMap1.addMarker(new MarkerOptions()
                        .position(loc)
                        .title("Quest Alliance")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin))
        );
        LatLng loc2 = new LatLng(12.95765,77.71339);
        mMap2.addMarker(new MarkerOptions()
                        .position(loc)
                        .title("Karunashraya")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin))
        );*/

        /*LatLng loc1 = new LatLng(12.975787, 77.126008);
        mMap.addPolyline(new PolylineOptions().add(
                loc,

                loc1
                )
                        .width(10)
                        .color(Color.BLUE)
        );*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

   /*@Override
    public void onLocationChanged( Location location) {
        //

        //locationManger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);

        ArrayList<HashMap<String, String>> arl = (ArrayList<HashMap<String, String>>)
                getIntent().getSerializableExtra("arrayList");


        if (location != null) {
            double latitude = location.getLatitude();
            double langitude = location.getLongitude();


            myPosition = new LatLng(latitude, langitude);
            CameraPosition position = new CameraPosition.Builder().
                    target(myPosition).zoom(17).bearing(19).tilt(30).build();
            //_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

            _googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            _googleMap.addMarker(new
                    MarkerOptions().position(myPosition).title("start"));
        }

    }*/
}
        
