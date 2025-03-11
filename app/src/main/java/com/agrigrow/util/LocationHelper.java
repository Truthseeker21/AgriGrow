package com.agrigrow.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Helper class for handling location-related functionality.
 */
public class LocationHelper {

    private static final String TAG = "LocationHelper";
    private static final long UPDATE_INTERVAL = 10 * 60 * 1000; // 10 minutes
    private static final long FASTEST_INTERVAL = 5 * 60 * 1000; // 5 minutes

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    /**
     * Interface for location callback.
     */
    public interface LocationCallback {
        void onLocationResult(Location location, String address);
        void onError(String message);
    }

    /**
     * Constructor initializing location services.
     */
    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * Get the last known location.
     */
    public void getLastLocation(final LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            callback.onError("Location permission not granted");
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String address = getAddressFromLocation(location);
                            callback.onLocationResult(location, address);
                        } else {
                            // Last location might be null if the device hasn't determined location yet
                            // Request location updates
                            requestLocationUpdates(callback);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting last location", e);
                        callback.onError("Failed to get location: " + e.getMessage());
                    }
                });
    }

    /**
     * Start location updates.
     */
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted");
            return;
        }

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallback = new com.google.android.gms.location.LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Log.d(TAG, "Location update: " + location.getLatitude() + ", " + location.getLongitude());
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * Stop location updates.
     */
    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    /**
     * Request location updates specifically for a callback.
     */
    private void requestLocationUpdates(final LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            callback.onError("Location permission not granted");
            return;
        }

        LocationRequest tempLocationRequest = new LocationRequest();
        tempLocationRequest.setNumUpdates(1);
        tempLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        com.google.android.gms.location.LocationCallback tempLocationCallback = new com.google.android.gms.location.LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    callback.onError("Failed to get location update");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        String address = getAddressFromLocation(location);
                        callback.onLocationResult(location, address);
                        return;
                    }
                }
                callback.onError("Location update was null");
            }
        };

        fusedLocationClient.requestLocationUpdates(tempLocationRequest, tempLocationCallback, Looper.getMainLooper());
    }

    /**
     * Convert a location to a human-readable address.
     */
    private String getAddressFromLocation(Location location) {
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    StringBuilder sb = new StringBuilder();
                    
                    // Get city and state (or equivalent)
                    String city = address.getLocality();
                    String state = address.getAdminArea();
                    String country = address.getCountryName();
                    
                    if (city != null) {
                        sb.append(city);
                    }
                    
                    if (state != null) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(state);
                    }
                    
                    if (country != null && (city == null || state == null)) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(country);
                    }
                    
                    return sb.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error getting address from location", e);
            }
        }
        
        // If geocoder failed or is not present, return coordinates
        return String.format(Locale.getDefault(), "%.2f, %.2f", 
                location.getLatitude(), location.getLongitude());
    }
}
