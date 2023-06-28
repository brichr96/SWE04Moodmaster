package com.example.moodmaster.feelingScale_MoodShow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.moodmaster.R;
import com.google.android.gms.common.Feature;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.model.Marker;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;



import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private TextView tvLocation;

    private MapView mapView;

    private static final int REQUEST_GOOGLE_MAPS = 1;

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        mapView = findViewById(R.id.map);
//        mapView.setMultiTouchControls(true);
//        mapView.getController().setZoom(15.0);

//        TextView textView = findViewById(R.id.textView);

//        setAnimation(textView);

//        tvLocation = findViewById(R.id.tv_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    updateLocationUI(location);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update location every 5 seconds

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
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void updateLocationUI(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String text = "Latitude: " + latitude + "\nLongitude: " + longitude;
//            tvLocation.setText(text);

            GeoPoint startLocation = new GeoPoint(latitude, longitude);

//            double destLat = 47.089620;
//            double destLong = 15.408941;
//
//            GeoPoint destLocation = new GeoPoint(destLat, destLong);

//            displayLocationOnMap(startLocation);

            LatLng start = new LatLng(latitude, longitude);
            LatLng destination = getLatLngOffset(start);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start the desired activity after the delay
                    openGoogleMaps(latitude, longitude, destination.latitude, destination.longitude);
                    Toast.makeText(MapsActivity.this, R.string.location_loaded, Toast.LENGTH_SHORT).show();
                }
            }, 2500);

//            openGoogleMaps(latitude, longitude, destination.latitude, destination.longitude);

//            drawRoute(startLocation, destLocation);


        } else {
            Toast.makeText(this, R.string.no_location, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayLocationOnMap(GeoPoint location) {
//        GeoPoint location = new GeoPoint(latitude, longitude);

        // Create a marker at the specified location
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        mapView.getOverlays().add(marker);

        // Center the map on the location
        mapView.getController().setCenter(location);
        mapView.invalidate();
    }

    public void openGoogleMaps(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
//        double startLatitude = 37.7749; // Start location latitude
//        double startLongitude = -122.4194; // Start location longitude
//        double endLatitude = 34.0522; // End location latitude
//        double endLongitude = -118.2437; // End location longitude

        // Create a Uri with the start and end locations
        String uri = "http://maps.google.com/maps?dir_mode=walking&saddr=" + startLatitude + "," + startLongitude +
                "&daddr=" + endLatitude + "," + endLongitude;

        // Create an Intent with the Uri
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

        // Set the package to Google Maps
        intent.setPackage("com.google.android.apps.maps");

        startActivityForResult(intent, REQUEST_GOOGLE_MAPS);

        // Start the activity
//        startActivity(intent);



//        Intent intentBack = new Intent(MapsActivity.this, Overview.class);
//        startActivity(intentBack);
    }

    public static LatLng getLatLngOffset(LatLng startingLatLng) {
        double radius = 6371; // Earth's radius in kilometers
        double distance = 1.0; // Distance in kilometers

        Random random = new Random();
        double bearing = random.nextDouble() * 360; // Random bearing angle

        double startLat = Math.toRadians(startingLatLng.latitude);
        double startLng = Math.toRadians(startingLatLng.longitude);
        double bearingRad = Math.toRadians(bearing);

        // Calculate the new latitude
        double newLat = Math.asin(Math.sin(startLat) * Math.cos(distance / radius) +
                Math.cos(startLat) * Math.sin(distance / radius) * Math.cos(bearingRad));

        // Calculate the new longitude
        double newLng = startLng + Math.atan2(Math.sin(bearingRad) * Math.sin(distance / radius) * Math.cos(startLat),
                Math.cos(distance / radius) - Math.sin(startLat) * Math.sin(newLat));

        // Convert back to degrees
        newLat = Math.toDegrees(newLat);
        newLng = Math.toDegrees(newLng);

        return new LatLng(newLat, newLng);
    }

    private void setAnimation(TextView textview){
        Animation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        textview.startAnimation(scaleAnimation);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GOOGLE_MAPS) {
            // Handle the result and go back to the previous activity
            finish();
        }
    }

    private void drawRoute(GeoPoint startPoint, GeoPoint endPoint) {
        List<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);

        Polyline routeLine = new Polyline();
        routeLine.setColor(Color.BLUE);
        routeLine.setWidth(5f);
        routeLine.setPoints(waypoints);

        mapView.getOverlayManager().add(routeLine);
        mapView.invalidate();

        // Zoom and center the map on the route
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(startPoint);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, R.string.location_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

