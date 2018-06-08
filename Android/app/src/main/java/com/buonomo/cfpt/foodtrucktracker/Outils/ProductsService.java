package com.buonomo.cfpt.foodtrucktracker.Outils;

import com.buonomo.cfpt.foodtrucktracker.Models.Product;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class ProductsService {

    /**
     * Interface d'appel pour la recupération de la liste des produits
     */
    public interface Callbacks{
        void onResponse(List<Product> products);
        void onFailure();
    }

    /**
     * Permet de récupérer la liste des produits
     * @param callback l'interface de callback de la classe
     */
    public static void getProducts(ProductsService.Callbacks callback){
        final WeakReference<ProductsService.Callbacks> callbacksWeakReference = new WeakReference<ProductsService.Callbacks>(callback);

        ServiceAccess serviceAccess = ServiceAccess.retrofitProducts.create(ServiceAccess.class);

        retrofit2.Call<List<Product>> call = serviceAccess.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            /**
             * Réponse positive du service web
             * @param call requête de la fonction de récupération de données
             * @param response réponse donnée par le service
             */
            @Override
            public void onResponse(retrofit2.Call<List<Product>> call, Response<List<Product>> response) {
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
            public void onFailure(retrofit2.Call<List<Product>> call, Throwable t) {
                if (callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().onFailure();
                }
            }
        });
    }
}