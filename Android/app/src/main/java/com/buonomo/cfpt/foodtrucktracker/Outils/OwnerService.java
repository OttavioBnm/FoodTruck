package com.buonomo.cfpt.foodtrucktracker.Outils;

import com.buonomo.cfpt.foodtrucktracker.Models.Owner;

import java.lang.ref.WeakReference;

import retrofit2.Callback;
import retrofit2.Response;

public class OwnerService {
    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface CallbacksAddOwner {
        void onResponse(Void owner);
        void onFailure();
    }

    /**
     * Permet d'ajouter un nouvel utilisateur à la base de données
     * @param callback l'interface de callback de la classe
     * @param name nom du nouveau propriétaire
     * @param firstname prénom du nouveau propriétaire
     * @param username pseudo du nouveau propriétaire
     * @param email adresse électronique du nouveau propriétaire
     * @param password mot de passe du nouveau propriétaire
     */
    public static void newOwner(CallbacksAddOwner callback, String name, String firstname, String username, String email, String password){
        final WeakReference<CallbacksAddOwner> callbacksWeakReference = new WeakReference<CallbacksAddOwner>(callback);

        ServiceAccess serviceAccess = ServiceAccess.retrofitCreateOwner.create(ServiceAccess.class);

        retrofit2.Call<Void> call = serviceAccess.createOwner(name, firstname, username, email, password);

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
                    t.printStackTrace();
                }
            }
        });
    }

    public interface CallbacksAppropriate {
        void onResponse(Void owner);
        void onFailure();
    }

    public static void appropriateFoodTruck(CallbacksAppropriate callback, int idProprietaire, int idFoodTruck, Owner o){
        final WeakReference<CallbacksAppropriate> callbacksWeakReference = new WeakReference<CallbacksAppropriate>(callback);


        ServiceAccess serviceAccess = ServiceAccess.retrofitAppropriateFoodTruck.create(ServiceAccess.class);
        Authentication.setHeaders(o.getCourriel(), o.getMotDePasse());
        retrofit2.Call<Void> call = serviceAccess.appropriateFoodTruck(idProprietaire, idFoodTruck);

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
                    t.printStackTrace();
                }
            }
        });
    }

    public interface CallbacksReturn {
        void onResponse(Void owner);
        void onFailure();
    }

    public static void returnFoodTruck(CallbacksReturn callback, int idProprietaire, int idFoodTruck, Owner o){
        final WeakReference<CallbacksReturn> callbacksWeakReference = new WeakReference<CallbacksReturn>(callback);


        ServiceAccess serviceAccess = ServiceAccess.retrofitReturnFoodTruck.create(ServiceAccess.class);
        Authentication.setHeaders(o.getCourriel(), o.getMotDePasse());
        retrofit2.Call<Void> call = serviceAccess.returnFoodTruck(idProprietaire, idFoodTruck);

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
                    t.printStackTrace();
                }
            }
        });
    }
}