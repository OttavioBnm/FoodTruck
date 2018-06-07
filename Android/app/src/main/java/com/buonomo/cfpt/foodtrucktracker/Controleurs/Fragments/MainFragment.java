/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Controleurs.Fragments
 * Classe       : MainFragment.java
 * Description  : Controleur du fragment qui sera integré a la MainActivity
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Controleurs.Fragments;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees.Map;
import com.buonomo.cfpt.foodtrucktracker.Controleurs.Activitiees.MainActivity;
import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Outils.GpsUtilisateur;
import com.buonomo.cfpt.foodtrucktracker.Outils.ItemClickSupport;
import com.buonomo.cfpt.foodtrucktracker.Outils.FoodTruckService;
import com.buonomo.cfpt.foodtrucktracker.R;
import com.buonomo.cfpt.foodtrucktracker.Vues.FoodTruckAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements FoodTruckAdapter.Listener, FoodTruckService.Callbacks{

    // Éléments de la vue
    @BindView(R.id.mainRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipe) SwipeRefreshLayout swipeRefreshLayout;

    // Champs
    private List<FoodTruck> camions;
    private FoodTruckAdapter adapter;

    /**
     * Constructeeur
     */
    public MainFragment() {

    }

    /**
     * Création du fragment
     * @param inflater fragment xml à integrer
     * @param container vue parente
     * @param savedInstanceState données de sauvegarde de la vue
     * @return View qui sera integrée à MainActivity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        this.configureRecyclerView();
        this.configureSwipeRefreshLayout();
        this.configureOnClickRecyclerView();
        this.executeHttpRequestWithRetrofit();
        return view;
    }

    /**
     * Execute la requête pour la récupération des food trucks
     */
    private void executeHttpRequestWithRetrofit() {
        final Location l = GpsUtilisateur.gps(MainActivity.getContext());
        FoodTruckService.getFoodTrucks(MainFragment.this, l.getLatitude(), l.getLongitude());
    }

    /**
     * Configuration du "glisser pour mettre à jour"
     */
    private void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }

    /**
     * Insertion des food trucks dans la recycler view
     */
    private void configureRecyclerView() {
        this.camions = new ArrayList<>();
        this.adapter = new FoodTruckAdapter(this, this.camions, Glide.with(this));
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Configuration du clic sur un élément de la recycler view
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.item).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FoodTruck camion = adapter.getFoodTruck(position);
                Intent i = new Intent(MainActivity.getContext(), Map.class);
                i.putExtra("truck", camion);
                startActivity(i);
            }
        });
    }

    /**
     * Mise à jour de l'interface utilisateur
     * @param camions liste des camions à afficher
     */
    private void updateUI(List<FoodTruck> camions){
        this.camions.clear();
        this.camions.addAll(camions);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);

        this.recyclerView.setLayoutAnimation(controller);
        this.recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Action lors du clic sur le bouton de suppression d'un camion
     * @param position position du camion à supprimer
     */
    @Override
    public void onClickDeleteButton(int position) {
        FoodTruck camion = adapter.getFoodTruck(position);
        Toast.makeText(getContext(), "Supprimer : " + camion.getNom(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Réponse positive du service web et misa a jour de la vue
     * @param camions nouvelle liste des camions reçue par l'application
     */
    @Override
    public void onResponse(List<FoodTruck> camions) {
        updateUI(camions);
    }

    /**
     * Réponse négative du service web, une erreur s'est produite
     */
    @Override
    public void onFailure() {
        Log.e("ERROR ", "Service Error");
    }
}
