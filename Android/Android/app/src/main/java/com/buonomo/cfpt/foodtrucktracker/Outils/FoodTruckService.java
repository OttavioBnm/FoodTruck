/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Outils
 * Interface    : ServiceAccess.java
 * Description  : Interface d'accès au service web pour l'execution des requêtes HTTP
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Outils;

import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class FoodTruckService {

    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface Callbacks{
        void onResponse(List<FoodTruck> camions);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des food trucks
     * @param callback l'interface de callback de la classe
     * @param latitude latitude de l'utilisateur
     * @param longitude longitude de l'utilisateur
     */
    public static void getFoodTrucks(FoodTruckService.Callbacks callback, double latitude, double longitude){
        final WeakReference<FoodTruckService.Callbacks> callbacksWeakReference = new WeakReference<FoodTruckService.Callbacks>(callback);


        ServiceAccess serviceAccess = ServiceAccess.retrofitTruck.create(ServiceAccess.class);

        retrofit2.Call<List<FoodTruck>> call = serviceAccess.getFoodTruck(latitude, longitude);

        call.enqueue(new Callback<List<FoodTruck>>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<List<FoodTruck>> call, Response<List<FoodTruck>> response) {
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
            public void onFailure(retrofit2.Call<List<FoodTruck>> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                }
            }
        });
    }
}
