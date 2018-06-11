package com.buonomo.cfpt.foodtrucktracker.Outils;

import android.util.Log;

import com.buonomo.cfpt.foodtrucktracker.Models.Owner;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerService {
    /**
     * Interface d'appel pour la recupération de la liste de food truck
     */
    public interface Callbacks{
        void onResponse();
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des food trucks
     * @param callback l'interface de callback de la classe
     */
    public static void newOwner(String name, String firstname, String username, String email, String password, OwnerService.Callbacks callback){
        final WeakReference<OwnerService.Callbacks> callbacksWeakReference = new WeakReference<OwnerService.Callbacks>(callback);

        ServiceAccess serviceAccess = ServiceAccess.retrofitCreateOwner.create(ServiceAccess.class);

        retrofit2.Call call = serviceAccess.createOwner(name, firstname, username, email, password);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("Owner", "Success");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Owner", "Failure");

            }
        });
    }
}