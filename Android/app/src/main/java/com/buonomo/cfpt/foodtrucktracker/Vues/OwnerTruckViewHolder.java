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

public class OwnerTruckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Vues xml
    @BindView(R.id.titre)
    TextView textViewTitre;
    @BindView(R.id.fragment_distance)
    TextView textViewDistance;
    @BindView(R.id.imageCamion)
    ImageView imageView;
    @BindView(R.id.remove_image)
    ImageView remove;

    // Champ avec référence faible
    private WeakReference<OwnerTruckAdapter.Listener> callbackWeakRef;

    /**
     * Constructeur
     * @param itemView
     */
    public OwnerTruckViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Met à jour toutes les vues avec les informations du food truck
     * @param camion food truck de la liste
     * @param glide RequestManager pour la gestion de la requête de l'image
     * @param callback appel de la méthode pour le clic d'un élément
     */
    public void updateWithTruck(FoodTruck camion, RequestManager glide, OwnerTruckAdapter.Listener callback) {
        this.textViewTitre.setText(camion.getNom());
        this.textViewDistance.setText("");
        this.remove.setOnClickListener(this);
        glide.load("http://10.134.99.39/rest/img/" + camion.getImage()).apply(RequestOptions.circleCropTransform()).into(imageView);
        this.callbackWeakRef = new WeakReference<OwnerTruckAdapter.Listener>(callback);
    }

    /**
     * Clic sur la vue à supprimer
     * @param v vue cliquée
     */
    @Override
    public void onClick(View v) {
        OwnerTruckAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) {
            callback.onClickDeleteButton(getAdapterPosition());
        }
    }
}
