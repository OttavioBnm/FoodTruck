package com.buonomo.cfpt.foodtrucktracker.Vues;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.R;

import java.util.List;

public class OwnerTruckAdapter extends RecyclerView.Adapter<OwnerTruckViewHolder> {
    private final OwnerTruckAdapter.Listener callback;

    private List<FoodTruck> camions;
    private RequestManager glide;

    /**
     * Constructeur
     * @param callback
     * @param foodTrucks liste des food trucks reçus par le service web
     * @param glide RequestManager qui permet la requête pour les images
     */
    public OwnerTruckAdapter(OwnerTruckAdapter.Listener callback, List<FoodTruck> foodTrucks, RequestManager glide) {
        this.callback = callback;
        this.camions = foodTrucks;
        this.glide = glide;
    }

    /**
     * Création du view holder
     * @param parent groupe de vues parentes de l'adapter
     * @param viewType type de la vue
     * @return View à afficher dans la recycler view
     */
    @Override
    public OwnerTruckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new OwnerTruckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OwnerTruckViewHolder holder, int position) {
        holder.updateWithTruck(this.camions.get(position), this.glide, this.callback);
    }

    /**
     * Compte les food trucks retournés par le service web
     * @return nombre de food truck dans la liste
     */
    @Override
    public int getItemCount() {
        return this.camions.size();
    }

    /**
     * Récupère un seul food truck dans la recycler view
     * @param position position de l'élément selectionné
     * @return food truck sélectionné
     */
    public FoodTruck getFoodTruck(int position){
        return this.camions.get(position);
    }

    /**
     * Interface pour la suppression d'un camion
     */
    public interface Listener {
        void onClickDeleteButton(int position);
    }
}
