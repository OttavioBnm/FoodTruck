package com.buonomo.cfpt.foodtrucktracker.Outils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.startActivity;

public class GpsUtilisateur {
    private static LocationListener locationListener;
    private static LocationManager locationManager;
    private static Location l = new Location("gps");

    public static Location gps(final Context c){
        locationManager = (LocationManager) c.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(c,intent, Bundle.EMPTY);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) c, new String[]{
                        Manifest.permission .ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return l;
            }
            else{
                locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
            }
        }
        return l;
    }

    public static void stopGPS(){
        locationManager.removeUpdates(locationListener);
    }
}
