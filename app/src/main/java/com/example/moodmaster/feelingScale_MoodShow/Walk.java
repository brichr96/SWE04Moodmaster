package com.example.moodmaster.feelingScale_MoodShow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.moodmaster.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class Walk extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));

                //random location 1 km away
                LatLng randomLoc = getRandomLocation(currentLoc, 1000);

                //draw route on map
                drawRoute(currentLoc, randomLoc);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
    }

    private LatLng getRandomLocation(LatLng currentLoc, int radius){

        double randomRadius = Math.random() * radius;
        double randomAngle = Math.random() * 2 * Math.PI;
        double dx = Math.random() * Math.cos(randomAngle);
        double dy = Math.random() * Math.sin(randomAngle);
        double randomLat = currentLoc.latitude + dx / 111300;
        double randomLong = currentLoc.longitude + dy / (111300 * Math.cos(currentLoc.latitude));

        return new LatLng(randomLat, randomLong);
    }

    private void drawRoute(LatLng start, LatLng end){

        //only straight line
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(start)
                .add(end)
                .width(5)
                .color(Color.RED);
        map.addPolyline(polylineOptions);
    }



}