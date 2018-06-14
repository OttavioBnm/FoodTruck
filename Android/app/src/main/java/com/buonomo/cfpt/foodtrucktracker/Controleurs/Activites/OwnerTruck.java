package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments.OwnerFragment;
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.R;

public class OwnerTruck extends AppCompatActivity {

    // Attributs de la classe
    private OwnerFragment ownerFragment;
    private static Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_truck);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null){
            owner = (Owner) getIntent().getExtras().getSerializable("owner");
        }
        getSupportActionBar().setTitle("Mes Food Trucks");
        this.configureAndShowOwnerFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * Configuration du frgment OwnerFragment a integrer dans OwnerTruck
     */
    private void configureAndShowOwnerFragment() {
        ownerFragment = (OwnerFragment) getSupportFragmentManager().findFragmentById(R.id.activity_owner_frame_layout);

        if (ownerFragment == null){
            ownerFragment = new OwnerFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_owner_frame_layout, ownerFragment).commit();

        }
    }

    /**
     *
     * @return
     */
    public static Owner getOwner(){
        return owner;
    }

    public static void setOwner(Owner o){
        owner = o;
    }
}
