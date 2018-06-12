package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments.MainFragment;
import com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments.OwnerFragment;
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.R;

public class OwnerTruck extends AppCompatActivity {

    // Attributs de la classe
    private OwnerFragment ownerFragment;
    private static Context instance;
    private Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_truck);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mes Food Trucks");
        this.configureAndShowOwnerFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * Configuration du frgment MainFragment a integrer dans la MainActivity
     */
    private void configureAndShowOwnerFragment() {
        ownerFragment = (OwnerFragment) getSupportFragmentManager().findFragmentById(R.id.activity_owner_frame_layout);

        if (ownerFragment == null){
            ownerFragment = new OwnerFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_owner_frame_layout, ownerFragment).commit();

        }
    }
}
