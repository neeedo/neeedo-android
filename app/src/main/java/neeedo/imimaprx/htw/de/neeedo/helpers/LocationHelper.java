package neeedo.imimaprx.htw.de.neeedo.helpers;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import neeedo.imimaprx.htw.de.neeedo.entities.Location;

public class LocationHelper {

    private LocationManager locationManager;
    private String locationProvider;
    private android.location.Location lastKnownLocation;
    private double locationLatitude;
    private double locationLongitude;
    private boolean locationAvailable;

    public LocationHelper(Activity activity) {

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), false); // GPS or network provider

        locationManager.requestLocationUpdates(locationProvider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {}

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        });

        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        try {
            locationLatitude = lastKnownLocation.getLatitude();
            locationLongitude = lastKnownLocation.getLongitude();
            locationAvailable = true;
        } catch(NullPointerException e) {
            //TODO when would that happen?
            //TODO show message that no data is available
            locationAvailable = false;
        }
    }

    public Location getLocation() {
        Location location = new Location(locationLatitude, locationLongitude);

        return location;
    }

    public boolean isLocationAvailable() {
        return locationAvailable;
    }
}
