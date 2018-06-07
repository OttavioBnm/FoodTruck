/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Vues
 * Classe       : FoodTruckViewHolder.java
 * Description  : Place les information des food trucks dans les vues xml, hérite de RecyclerView.ViewHolder et implémente les clics sur les vues
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Vues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.R;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodTruckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Vues xml
    @BindView(R.id.titre)
    TextView textViewTitre;
    @BindView(R.id.fragment_distance)
    TextView textViewDistance;
    @BindView(R.id.imageCamion)
    ImageView imageView;

    // Champ avec référence faible
    private WeakReference<FoodTruckAdapter.Listener> callbackWeakRef;

    /**
     * Constructeur
     * @param itemView
     */
    public FoodTruckViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Met à jour toutes les vues avec les informations du food truck
     * @param camion food truck de la liste
     * @param glide RequestManager pour la gestion de la requête de l'image
     * @param callback appel de la méthode pour le clic d'un élément
     */
    public void updateWithTruck(FoodTruck camion, RequestManager glide, FoodTruckAdapter.Listener callback) {
        this.textViewTitre.setText(camion.getNom());
        this.textViewDistance.setText("Position : " + camion.getAdressePostale());
        glide.load("http://le-petitchou.com/wp-content/uploads/2015/04/c_IMG_8902-copie.jpg").apply(RequestOptions.circleCropTransform()).into(imageView);
        this.callbackWeakRef = new WeakReference<FoodTruckAdapter.Listener>(callback);
    }

    /**
     * Clic sur la vue à supprimer
     * @param v vue cliquée
     */
    @Override
    public void onClick(View v) {
        FoodTruckAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) {
            callback.onClickDeleteButton(getAdapterPosition());
        }
    }
}
