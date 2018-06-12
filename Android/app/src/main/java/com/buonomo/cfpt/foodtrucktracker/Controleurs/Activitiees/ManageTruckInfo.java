package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Outils.FoodTruckService;
import com.buonomo.cfpt.foodtrucktracker.Outils.LocationService;
import com.buonomo.cfpt.foodtrucktracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageTruckInfo extends AppCompatActivity implements FoodTruckService.CallbackUpdateFoodTruck, LocationService.Callbacks{

    @BindView(R.id.tbxNomCamionModifier)
    TextView truckName;
    @BindView(R.id.tbxContactModifier)
    TextView truckContact;
    @BindView(R.id.imgCamionAModifier)
    ImageView truckImage;
    @BindView(R.id.spinner_locations)
    Spinner truckLocationsSpinner;

    private FoodTruck foodTruck;
    private RequestManager glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_truck_info);
        ButterKnife.bind(this);
        glide = Glide.with(this);
        foodTruck = (FoodTruck) getIntent().getSerializableExtra("truck");
        truckContact.setText(foodTruck.getContact());
        truckName.setText(foodTruck.getNom());
        this.executeGetLocationWithRetrofit();
        glide.load("http://10.134.99.39/rest/img/" + foodTruck.getImage()).apply(RequestOptions.centerCropTransform()).into(truckImage);
    }

    public void executeUpdateTruckWithRetrofit(){
        FoodTruckService.updateFoodTruckInfos(this, truckName.getText().toString(), "",truckContact.getText().toString(), Integer.parseInt(foodTruck.getIdFoodTruck()));
    }

    public void executeGetLocationWithRetrofit(){
        LocationService.getLocationsFoodTruck(this, Integer.parseInt(foodTruck.getIdFoodTruck()));
    }

    public void onClickUpdateTruck(View v){
        this.executeUpdateTruckWithRetrofit();
    }

    @Override
    public void OnResponse(Void response) {
        Log.e("OK ", "UPDATED");
    }

    @Override
    public void OnFailure() {
        Log.e("ERROR ", "NOT UPDATED");
    }

    @Override
    public void onResponse(List<String> locations) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locations);
        truckLocationsSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void onFailure() {
        Log.e("ERROR ", "NOT UPDATED");
    }
}
