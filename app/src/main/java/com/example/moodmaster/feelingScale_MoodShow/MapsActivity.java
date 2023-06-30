package com.example.moodmaster.feelingScale_MoodShow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.moodmaster.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import java.util.Random;

public class MapsActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int REQUEST_GOOGLE_MAPS = 1;

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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


    /**
     * Requests the ACCESS_FINE_LOCATION permission if it has not been granted already.
     * If the permission is granted, it starts location updates.
     * If the permission is not granted, it requests the permission from the user.
     */
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

    /**
     * Starts receiving location updates using the fused location provider.
     * It creates a LocationRequest with high accuracy and a 5-second interval for updates.
     * If the necessary location permissions are not granted, the method returns early.
     * Otherwise, it requests location updates from the fused location client using the
     * created LocationRequest and the specified location callback.
     */
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update location every 5 seconds

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    /**
     * Stops receiving location updates by removing the location callback from the fused location client.
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Updates the UI based on the provided location information.
     *
     * @param location The location object containing latitude and longitude coordinates.
     */
    private void updateLocationUI(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng start = new LatLng(latitude, longitude);
            LatLng destination = getLatLngOffset(start);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openGoogleMaps(latitude, longitude, destination.latitude, destination.longitude);
                    Toast.makeText(MapsActivity.this, R.string.location_loaded, Toast.LENGTH_SHORT).show();
                }
            }, 2500);
        }
        else {
            Toast.makeText(this, R.string.no_location, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens Google Maps with directions from the starting location to the destination location.
     *
     * @param startLatitude  The latitude of the starting location.
     * @param startLongitude The longitude of the starting location.
     * @param endLatitude    The latitude of the destination location.
     * @param endLongitude   The longitude of the destination location.
     */
    public void openGoogleMaps(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {

        String uri = "http://maps.google.com/maps?dir_mode=walking&saddr=" + startLatitude + "," + startLongitude + "&daddr=" + endLatitude + "," + endLongitude;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

        intent.setPackage("com.google.android.apps.maps");

        startActivityForResult(intent, REQUEST_GOOGLE_MAPS);
    }

    /**
     * Calculates a new LatLng position by applying a random offset to the given starting LatLng position.
     *
     * @param startingLatLng The starting LatLng position.
     * @return A new LatLng position with a random offset applied.
     */
    public static LatLng getLatLngOffset(LatLng startingLatLng) {
        double radius = 6371;
        double distance = 1.0;

        Random random = new Random();
        double bearing = random.nextDouble() * 360;

        double startLat = Math.toRadians(startingLatLng.latitude);
        double startLng = Math.toRadians(startingLatLng.longitude);
        double bearingRad = Math.toRadians(bearing);

        double newLat = Math.asin(Math.sin(startLat) * Math.cos(distance / radius) +
                Math.cos(startLat) * Math.sin(distance / radius) * Math.cos(bearingRad));

        double newLng = startLng + Math.atan2(Math.sin(bearingRad) * Math.sin(distance / radius) * Math.cos(startLat),
                Math.cos(distance / radius) - Math.sin(startLat) * Math.sin(newLat));

        newLat = Math.toDegrees(newLat);
        newLng = Math.toDegrees(newLng);

        return new LatLng(newLat, newLng);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GOOGLE_MAPS) {
            finish();
        }
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

