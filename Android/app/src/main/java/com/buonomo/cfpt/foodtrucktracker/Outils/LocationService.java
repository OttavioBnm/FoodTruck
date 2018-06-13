package com.buonomo.cfpt.foodtrucktracker.Outils;

import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.Models.Product;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class LocationService {
    /**
     * Interface d'appel pour la recupération de la liste des produits
     */
    public interface CallbacksGetLocations {
        void onResponse(List<String> locations);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des produits
     * @param callback l'interface de callback de la classe
     */
    public static void getLocationsFoodTruck(LocationService.CallbacksGetLocations callback, int idFoodTruck){
        final WeakReference<LocationService.CallbacksGetLocations> callbacksWeakReference = new WeakReference<LocationService.CallbacksGetLocations>(callback);

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

    public interface CallbacksUpdateLocation{
        void onResponse(Void locations);
        void onFailure();
    }

    public static void updateLocation(LocationService.CallbacksUpdateLocation callback, double lat, double lon, int idFoodTruck, String day, Owner o){
        final WeakReference<LocationService.CallbacksUpdateLocation> callbacksWeakReference = new WeakReference<LocationService.CallbacksUpdateLocation>(callback);

        ServiceAccess serviceAccess = ServiceAccess.retrofitUpdateLocation.create(ServiceAccess.class);
        Authentication.setHeaders(o.getCourriel(), o.getMotDePasse());
        retrofit2.Call<Void> call = serviceAccess.updateLocationInfos(lat, lon, idFoodTruck, day);

        call.enqueue(new Callback<Void>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
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
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                }
            }
        });
    }
}
