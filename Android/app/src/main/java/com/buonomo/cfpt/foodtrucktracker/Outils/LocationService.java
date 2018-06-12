package com.buonomo.cfpt.foodtrucktracker.Outils;

import com.buonomo.cfpt.foodtrucktracker.Models.Product;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class LocationService {
    /**
     * Interface d'appel pour la recupération de la liste des produits
     */
    public interface Callbacks{
        void onResponse(List<String> locations);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des produits
     * @param callback l'interface de callback de la classe
     */
    public static void getLocationsFoodTruck(LocationService.Callbacks callback, int idFoodTruck){
        final WeakReference<LocationService.Callbacks> callbacksWeakReference = new WeakReference<LocationService.Callbacks>(callback);

        ServiceAccess serviceAccess = ServiceAccess.retrofitGetLocationsFoodTruck.create(ServiceAccess.class);

        retrofit2.Call<List<String>> call = serviceAccess.getLocationsFoodTruck(idFoodTruck);

        call.enqueue(new Callback<List<String>>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<List<String>> call, Response<List<String>> response) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onResponse(response.body());
                }
            }

            /**
             * Réponse négative du service web
             * @param call requête de la fonction de récupération de données
             * @param t réponse jetable
             */
            @Override
            public void onFailure(retrofit2.Call<List<String>> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                }
            }
        });
    }
}
