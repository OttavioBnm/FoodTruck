/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Controleurs.Activitees
 * Classe       : Map.java
 * Description  : Controleur d'affichage de la carte et des détails du food truck
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments.DetailsFragment;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    // Champs
    private GoogleMap mMap;
    private FoodTruck foodTruck;
    private DetailsFragment detailsFragment;

    /**
     * Création de la vue d'affichage de la carte et des détails
     * @param savedInstanceState données de sauvegarde de la vue
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte);
        foodTruck = (FoodTruck) getIntent().getSerializableExtra("truck");
        this.configureAndShowDetailsFragment();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipule la carte quand elle est prête
     * Ce callback est appelé lorsque la carte est prête à l'utilisation
     * Cet endroit permet l'ajout de markers, bouger la caméra, etc...
     * Si les Google Play services sont pas installés l'utilisateur va être conduit à l'installation (se situe dans SupportMapFragment)
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ftPosition = new LatLng(foodTruck.getLatitude(), foodTruck.getLongitude());
        mMap.addMarker(new MarkerOptions().position(ftPosition).title(foodTruck.getNom()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ftPosition, 16.0f));
    }

    /**
     * Configure et affiche le fragment de détails des food trucks
     */
    private void configureAndShowDetailsFragment() {
        detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.map_activity_frame);

        if (detailsFragment == null){
            detailsFragment = new DetailsFragment();
            detailsFragment.setFoodTruck(foodTruck);
            getSupportFragmentManager().beginTransaction().add(R.id.map_activity_frame, detailsFragment).commit();
        }
    }
}
