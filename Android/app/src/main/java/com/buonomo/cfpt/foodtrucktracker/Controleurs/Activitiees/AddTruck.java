package com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.TextureView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Models.Product;
import com.buonomo.cfpt.foodtrucktracker.Outils.ProductsService;
import com.buonomo.cfpt.foodtrucktracker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTruck extends AppCompatActivity implements ProductsService.Callbacks{

    @BindView(R.id.tbxNomCamion)
    TextView truckName;
    @BindView(R.id.tbxAdresseCamion)
    TextView truckAddress;
    @BindView(R.id.tbxNPA)
    TextView truckPostalCode;
    @BindView(R.id.tbxLocalite)
    TextView truckCity;
    @BindView(R.id.tbxHeureDebut)
    TextView truckStartTime;
    @BindView(R.id.tbxHeureFin)
    TextView truckEndTime;
    @BindView(R.id.ratingNote)
    RatingBar truckEvaluation;
    @BindView(R.id.btnAjouterCamion)
    Button addTruckButton;
    @BindView(R.id.spinner_products)
    Spinner spinnerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truck);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.executeRequestProducts();
    }

    /**
     * Création de l'affichage du menu dans l'action bar de l'activitée
     * @param menu Menu à afficher
     * @return affiche le menu (oui/non)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void addNewFoodTruck(){
        /*FoodTruck f = new FoodTruck();
        f.setNom();
        f.setHeureDebut();
        f.setHeureFin();
        f.setJourSemaine();
        f.setEvaluation();
        f.setLatitude();
        f.setContact();
        f.setContact();
        f.setContact();
        f.setContact();
        f.setContact();*/
    }

    public void executeRequestProducts(){
        ProductsService.getProducts(this);
    }

    @Override
    public void onResponse(List<Product> products) {
        List<String> arrayProducts = new ArrayList<>();
        for (Product p: products) {
            arrayProducts.add(p.getNom());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayProducts);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(arrayAdapter);
    }

    @Override
    public void onFailure() {

    }
}
