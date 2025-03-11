package com.agrigrow.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class for retrieving device location and converting to addresses
 */
public class LocationHelper {

    public interface LocationCallback {
        void onLocationReceived(Location location);
    }

    public interface AddressCallback {
        void onAddressReceived(String address);
    }

    private final Context context;
    private final LocationManager locationManager;
    private final ExecutorService executor;

    public LocationHelper(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Get current device location
     */
    public void getCurrentLocation(LocationCallback callback) {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, return null location
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
            callback.onLocationReceived(null);
            return;
        }

        // Try to get last known location first for immediate response
        Location lastKnownLocation = null;
        
        try {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If we have a recent location, return it immediately
        if (lastKnownLocation != null && System.currentTimeMillis() - lastKnownLocation.getTime() < 5 * 60 * 1000) {
            callback.onLocationReceived(lastKnownLocation);
            return;
        }

        // Request a location update
        try {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    callback.onLocationReceived(location);
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    callback.onLocationReceived(null);
                    locationManager.removeUpdates(this);
                }
            };

            // Request a single location update
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, 
                    locationListener, Looper.getMainLooper());

            // Set a timeout of 10 seconds to get location
            new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                locationManager.removeUpdates(locationListener);
                if (lastKnownLocation != null) {
                    callback.onLocationReceived(lastKnownLocation);
                } else {
                    callback.onLocationReceived(null);
                }
            }, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onLocationReceived(null);
        }
    }

    /**
     * Get address from location using Geocoder
     */
    public void getAddressFromLocation(Location location, AddressCallback callback) {
        executor.execute(() -> {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);

                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    StringBuilder sb = new StringBuilder();

                    // Get key components of address
                    if (address.getSubThoroughfare() != null) {
                        sb.append(address.getSubThoroughfare()).append(" ");
                    }
                    if (address.getThoroughfare() != null) {
                        sb.append(address.getThoroughfare()).append(", ");
                    }
                    if (address.getLocality() != null) {
                        sb.append(address.getLocality());
                    } else if (address.getSubAdminArea() != null) {
                        sb.append(address.getSubAdminArea());
                    }

                    // Return address on main thread
                    new android.os.Handler(Looper.getMainLooper()).post(() -> 
                            callback.onAddressReceived(sb.toString()));
                } else {
                    new android.os.Handler(Looper.getMainLooper()).post(() -> 
                            callback.onAddressReceived(null));
                }
            } catch (IOException e) {
                e.printStackTrace();
                new android.os.Handler(Looper.getMainLooper()).post(() -> 
                        callback.onAddressReceived(null));
            }
        });
    }
}