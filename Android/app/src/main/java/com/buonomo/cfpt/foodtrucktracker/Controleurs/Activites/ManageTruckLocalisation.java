package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.Outils.LocationService;
import com.buonomo.cfpt.foodtrucktracker.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageTruckLocalisation extends AppCompatActivity implements LocationListener, LocationService.CallbacksUpdateLocation {

    @BindView(R.id.manage_location_address)
    TextView manageAddress;
    @BindView(R.id.manage_location_npa)
    TextView manageNpa;
    @BindView(R.id.manage_location_city)
    TextView manageCity;

    private String location;
    private String[] locationComponents;
    private List<Address> a;
    private LocationManager lm;
    private Geocoder g;
    private LatLng latLng;
    private FoodTruck foodTruck;
    private Owner owner;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_truck_localisation);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        location = getIntent().getExtras().getString("selectedLocation");
        foodTruck = (FoodTruck) getIntent().getExtras().getSerializable("truck");
        owner = (Owner) getIntent().getExtras().getSerializable("owner");
        locationComponents = location.split(",");
        String[] address = getAddress(Double.parseDouble(locationComponents[0]), Double.parseDouble(locationComponents[1]));
        manageAddress.setText(address[0]);
        getSupportActionBar().setTitle(address[0]);
        String[] npaCity = address[1].split(" ");
        manageCity.setText(npaCity[2]);
        manageNpa.setText(npaCity[1]);

    }

    /**
     * Récupération de la position de l'utilisateur
     * @param view vue
     */
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(View view) {
        final Handler h = new Handler();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        a = new ArrayList<>();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (latLng != null){
                    h.removeCallbacks(this);
                    g = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        a = g.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String[] adresse = new String[3];
                        adresse = a.get(0).getAddressLine(0).split(",");
                        String[] postalCodeCity = adresse[1].split(" ");
                        manageAddress.setText(adresse[0]);
                        manageNpa.setText(postalCodeCity[1]);
                        manageCity.setText(postalCodeCity[2]);
                        lm.removeUpdates(ManageTruckLocalisation.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    h.postDelayed(this, 100);
                }
            }
        };
        runnable.run();
    }

    /**
     * Converti une adresse en latitude longitude
     * @param addressTruck Adresse à convertir
     * @return une latitude longitude
     */
    private LatLng convertAddress(String addressTruck){
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            address = coder.getFromLocationName(addressTruck, 1);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return resLatLng;
    }

    public void onClickExecuteLocationUpdate(View v){
        this.executeUpdateLocationWithRetrofit();
    }

    public void executeUpdateLocationWithRetrofit(){
        LatLng l = convertAddress(manageAddress.getText()+", "+ manageNpa+", "+ manageCity);
        LocationService.updateLocation(this, l.latitude,l.longitude, foodTruck.getIdFoodTruck(),locationComponents[4], owner);
    }

    /**
     * Récupère l'adresse postale depuis une latitude et une longitude
     * @return Adresse postale du food truck
     */
    public String[] getAddress(double latitude, double longitude){
        Geocoder g = new Geocoder(MainActivity.getContext(), Locale.getDefault());
        try {
            List<Address> a = g.getFromLocation(latitude, longitude, 1);
            String[] adresse = a.get(0).getAddressLine(0).split(",");
            return adresse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResponse(Void locations) {
        OwnerTruck.setOwner(owner);
        finish();
        Log.e("OK ", "UPDATED LOCATION");
    }

    @Override
    public void onFailure() {
        Log.e("ERROR ", "NOT UPDATED LOCATION");
    }
}
