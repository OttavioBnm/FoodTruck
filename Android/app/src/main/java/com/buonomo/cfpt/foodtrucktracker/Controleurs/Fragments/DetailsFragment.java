/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Controleurs.Fragments
 * Classe       : DetailsFragment.java
 * Description  : Controleur du fragment qui sera integré à l'activité Map, hérite de Fragment
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Outils.FoodTruckService;
import com.buonomo.cfpt.foodtrucktracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends android.support.v4.app.Fragment implements FoodTruckService.CallbackAddEvaluation {
    // Chmaps des vues xml
    @BindView(R.id.textview_detail_contact)
    TextView textViewContact;
    @BindView(R.id.textview_detail_produits)
    TextView textViewProduits;
    @BindView(R.id.textview_detail_horaire)
    TextView textViewHoraire;
    @BindView(R.id.ratingBar_detail_note)
    RatingBar ratingBarDetail;
    @BindView(R.id.imageView_foodTruck)
    ImageView imageFoodTruck;

    // Attributs
    private FoodTruck truck;
    private boolean ratingFlag = false;

    /**
     * Constructeur
     */
    public DetailsFragment() {

    }

    /**
     * Création du fragment
     * @param inflater fragment xml qu'il faut implémenter
     * @param container vue parente
     * @param savedInstanceState données de sauvegarde de la vue
     * @return la vue du fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this,view);
        this.configureInformations(Glide.with(this));
        return view;
    }

    /**
     * Assigne le food truck que le fragment doit gérer
     * @param foodTruck
     */
    public void setFoodTruck(FoodTruck foodTruck) {
        truck = foodTruck;
    }

    /**
     * Configuration du fragment de l'application avec les informations du food truck
     */
    private void configureInformations(RequestManager glide){
        glide.load("http://10.134.99.39/rest/img/" + truck.getImage()).apply(RequestOptions.centerCropTransform()).into(imageFoodTruck);
        if (truck.getContact() !=  null){
            textViewContact.setText(truck.getContact());
        }else {
            textViewContact.setText("Cette information n'est pas disponible");
        }
        if (truck.getLstProduits() != null){
            textViewProduits.setText(truck.getLstProduits());
        }else {
            textViewProduits.setText("Cette information n'est pas disponible");
        }
        textViewHoraire.setText(truck.getHeureDebut() + " - " + truck.getHeureFin());
        if (truck.getEvaluation() != null){
            ratingBarDetail.setRating(Float.parseFloat(truck.getEvaluation()));
        }else{
            ratingBarDetail.setRating(0.0f);
        }
        ratingBarDetail.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingFlag = true;
            }
        });
    }

    @Override
    public void onDestroy() {
        ratingFlag = false;
        super.onDestroy();
    }

    public void executeAddRatingWithRetrofit(int rate, int idFoodTruck){
        FoodTruckService.addEvaluation(this, rate, idFoodTruck);
    }
    public void giveRating(){
        if (ratingFlag){
            executeAddRatingWithRetrofit(Math.round(ratingBarDetail.getRating()), Integer.parseInt(truck.getIdFoodTruck()));
        }
    }

    @Override
    public void OnResponse(Void response) {
        Log.i("SUCCESS", "RATING ADDED");
    }

    @Override
    public void OnFailure() {
        Log.e("FAILED", "RATING NOT ADDED");
    }
}
