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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RatingBar;

import com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments.DetailsFragment;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.Outils.FoodTruckService;
import com.buonomo.cfpt.foodtrucktracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    // Champs
    private GoogleMap mMap;
    private FoodTruck foodTruck;
    private DetailsFragment detailsFragment;
    private Owner owner;
    @BindView(R.id.fab_add_to_my_trucks)
    FloatingActionButton addToMyTrucks;

    /**
     * Création de l'affichage du menu dans l'action bar de l'activitée
     * @param menu Menu à afficher
     * @return affiche le menu (oui/non)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null){
            inflater.inflate(R.menu.dropdown_menu, menu);
        }
        else{
            owner = (Owner) getIntent().getExtras().getSerializable("owner");
            inflater.inflate(R.menu.owner_menu, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        detailsFragment.giveRating();
        finish();
        super.onBackPressed();
    }

    /**
     * Création de la vue d'affichage de la carte et des détails
     * @param savedInstanceState données de sauvegarde de la vue
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        foodTruck = (FoodTruck) getIntent().getSerializableExtra("truck");
        owner = (Owner) intent.getExtras().getSerializable("owner");
        if (owner!= null){
            addToMyTrucks.setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setTitle(foodTruck.getNom());
        this.configureAndShowDetailsFragment();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Retour à la page précédente et termine l'activitée
     * @return vrai si le retour est effectué
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.detailsFragment.giveRating();
        finish();
        return super.onSupportNavigateUp();
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
