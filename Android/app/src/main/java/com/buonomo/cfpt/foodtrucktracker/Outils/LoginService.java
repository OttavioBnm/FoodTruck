package com.buonomo.cfpt.foodtrucktracker.Outils;

import com.buonomo.cfpt.foodtrucktracker.Models.FoodTruck;
import com.buonomo.cfpt.foodtrucktracker.Models.Owner;
import com.buonomo.cfpt.foodtrucktracker.Models.Product;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {
    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface Callbacks{
        void onResponse(List<Owner> owner);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des food trucks
     * @param callback l'interface de callback de la classe
     */
    public static void tryLogin(LoginService.Callbacks callback, String email, String password){
        final WeakReference<LoginService.Callbacks> callbacksWeakReference = new WeakReference<LoginService.Callbacks>(callback);

        ServiceAccess serviceAccess = ServiceAccess.retrofitLogin.create(ServiceAccess.class);

        Authentication.setHeaders(email, password);
        retrofit2.Call<List<Owner>> call = serviceAccess.getLogin();

        call.enqueue(new Callback<List<Owner>>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<List<Owner>> call, Response<List<Owner>> response) {
                if (response.code() == 403){
                    if (callbacksWeakReference.get() != null){
                        callbacksWeakReference.get().onFailure();
                    }
                } else {
                    callbacksWeakReference.get().onResponse(response.body());
                }
            }

            /**
             * Réponse négative du service web
             * @param call requête de la fonction de récupération de données
             * @param t réponse jetable
             */
            @Override
            public void onFailure(retrofit2.Call<List<Owner>> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                }
            }
        });
    }
}
