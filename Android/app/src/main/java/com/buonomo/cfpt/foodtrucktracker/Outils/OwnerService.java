package com.buonomo.cfpt.foodtrucktracker.Outils;

import android.util.Log;

import com.buonomo.cfpt.foodtrucktracker.Models.Owner;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class OwnerService {
    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface Callbacks{
        void onResponse(Owner owner);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des food trucks
     * @param callback l'interface de callback de la classe
     *
     */
    public static void newOwner(OwnerService.Callbacks callback, String nom, String prenom, String pseudo, String email, String password){
        final WeakReference<OwnerService.Callbacks> callbacksWeakReference = new WeakReference<OwnerService.Callbacks>(callback);


        ServiceAccess serviceAccess = ServiceAccess.retrofitCreateOwner.create(ServiceAccess.class);

        retrofit2.Call<Owner> call = serviceAccess.createOwner(nom, prenom, pseudo, email, password);

        call.enqueue(new Callback<Owner>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<Owner> call, Response<Owner> response) {
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
            public void onFailure(retrofit2.Call<Owner> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                    System.out.print(t.getMessage());
                }
            }
        });
    }
}