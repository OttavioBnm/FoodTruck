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
import com.buonomo.cfpt.foodtrucktracker.Models.Product;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceAccess {

    /**
     * Permet de récupérer la liste des food trucks
     * @param lat latitude de l'utilisateur
     * @param lon longitude de l'utilisateur
     * @return liste de food trucks
     */
    @GET("camions")
    Call<List<FoodTruck>> getFoodTruck(@Query("lat") double lat, @Query("lon") double lon);

    Retrofit retrofitTruck = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("produits")
    Call<List<Product>> getProducts();
    Retrofit retrofitProducts = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("fonctions.inc/connexion.php")
    Call<List<Owner>> getLogin();
    Retrofit retrofitLogin = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .client(Authentication.getCredentials())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("proprietaires/nouveau.php")
    @FormUrlEncoded
    Call createOwner(
            @Field("Nom") String name,
            @Field("Prenom") String firstname,
            @Field("Pseudo") String username,
            @Field("Courriel") String email,
            @Field("MotDePasse") String password
    );
    Retrofit retrofitCreateOwner = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}