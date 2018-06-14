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
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class FoodTruckService {

    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface CallbacksLstFoodTrucks {
        void onResponse(List<FoodTruck> camions);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des food trucks
     * @param callback l'interface de callback de la classe
     * @param latitude latitude de l'utilisateur
     * @param longitude longitude de l'utilisateur
     */
    public static void getFoodTrucks(CallbacksLstFoodTrucks callback, double latitude, double longitude){
        final WeakReference<CallbacksLstFoodTrucks> callbacksWeakReference = new WeakReference<CallbacksLstFoodTrucks>(callback);


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

    public interface CallbacksCreateFoodTruck{
        void onResponse(String ok);
        void onFailure();
    }

    /**
     * Exécute la requête http pour l'ajout de food truck
     * en l'interfaçant avec l'accès au service de rétrofit
     * @param callbacksCreateFoodTruck
     * @param name Nom du food truck
     * @param lat  Latitude du food truck
     * @param lon Longitude du food truck
     * @param startTime Heure de début de vente du food trucks
     * @param endTime Heure de fin de vente du food trucks
     * @param weekDay Jour de la vente
     * @param idOwner Id du propriétaire
     * @param contact Contact du food truck
     * @param image Image du food truck
     * @param rating Note du food truck
     * @param ids Identifiants des produits
     */
    public static void createFoodTruck(CallbacksCreateFoodTruck callbacksCreateFoodTruck,
                                       String name,
                                       double lat,
                                       double lon,
                                       String startTime,
                                       String endTime,
                                       String weekDay,
                                       int idOwner,
                                       String contact,
                                       String image,
                                       int rating,
                                       String ids){
        final WeakReference<CallbacksCreateFoodTruck> callbacksWeakReference = new WeakReference<CallbacksCreateFoodTruck>(callbacksCreateFoodTruck);
        ServiceAccess serviceAccess = ServiceAccess.retrofitCreateFoodTruck.create(ServiceAccess.class);
        retrofit2.Call<String> call = serviceAccess.createFoodTruck(name,lat, lon,startTime, endTime, weekDay, idOwner, contact, image, rating, ids);
        call.enqueue(new Callback<String>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
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
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                    t.printStackTrace();
                }
            }
        });
    }

    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface CallbacksOwnerFoodTrucks {
        void onResponse(List<FoodTruck> camions);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des food trucks
     * @param callback l'interface de callback de la classe
     */
    public static void getFoodTruckByOwner(CallbacksOwnerFoodTrucks callback, int idProprietaire, String email, String password){
        final WeakReference<CallbacksOwnerFoodTrucks> callbacksWeakReference = new WeakReference<CallbacksOwnerFoodTrucks>(callback);


        ServiceAccess serviceAccess = ServiceAccess.retrofitTruckByOwner.create(ServiceAccess.class);

        Authentication.setHeaders(email, password);
        retrofit2.Call<List<FoodTruck>> call = serviceAccess.getFoodTruckByOwner(idProprietaire);

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

    public interface CallbackAddEvaluation{
        void OnResponse(Void response);
        void OnFailure();
    }

    public static void addEvaluation(CallbackAddEvaluation callbacksCreateFoodTruck, int aEvaluetion, int idFoodTruck){
        final WeakReference<CallbackAddEvaluation> callbacksWeakReference = new WeakReference<CallbackAddEvaluation>(callbacksCreateFoodTruck);


        ServiceAccess serviceAccess = ServiceAccess.retrofitAddEvaluation.create(ServiceAccess.class);

        retrofit2.Call<Void> call = serviceAccess.addNewEvaluation(aEvaluetion, idFoodTruck);

        call.enqueue(new Callback<Void>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().OnResponse(response.body());
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
                    callbacksWeakReference.get().OnFailure();
                    System.out.println(t.getMessage());
                }
            }
        });
    }

    public interface CallbackUpdateFoodTruck{
        void OnResponse(Void response);
        void OnFailure();
    }

    public static void updateFoodTruckInfos(CallbackUpdateFoodTruck callbacksCreateFoodTruck, String truckName, String truckImage, String truckContact, int idFoodTruck, Owner o){
        final WeakReference<CallbackUpdateFoodTruck> callbacksWeakReference = new WeakReference<CallbackUpdateFoodTruck>(callbacksCreateFoodTruck);


        ServiceAccess serviceAccess = ServiceAccess.retrofitUpdateFoodTruckInfos.create(ServiceAccess.class);
        Authentication.setHeaders(o.getCourriel(), o.getMotDePasse());
        retrofit2.Call<Void> call = serviceAccess.updateFoodTruckInfos(truckName, truckImage, truckContact, idFoodTruck);

        call.enqueue(new Callback<Void>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().OnResponse(response.body());
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
                    callbacksWeakReference.get().OnFailure();
                    System.out.println(t.getMessage());
                }
            }
        });
    }

    public interface CallbackRemoveFoodTruck{
        void OnResponse(Void response);
        void OnFailure();
    }

    public static void removeFoodTruckInfos(CallbackRemoveFoodTruck callbacksCreateFoodTruck, int idFoodTruck, String image, Owner o){
        final WeakReference<CallbackRemoveFoodTruck> callbacksWeakReference = new WeakReference<CallbackRemoveFoodTruck>(callbacksCreateFoodTruck);


        ServiceAccess serviceAccess = ServiceAccess.retrofitRemoveFoodTruck.create(ServiceAccess.class);

        Authentication.setHeaders(o.getCourriel(), o.getMotDePasse());
        retrofit2.Call<Void> call = serviceAccess.removeFoodTruck(idFoodTruck, image);

        call.enqueue(new Callback<Void>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
                int code = response.code();
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().OnResponse(response.body());
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
                    callbacksWeakReference.get().OnFailure();
                    System.out.println(t.getMessage());
                }
            }
        });
    }
}
