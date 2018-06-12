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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<Void> createOwner(
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

    @POST("camions/nouveau.php")
    @FormUrlEncoded
    Call<String> createFoodTruck(
            @Field("nomFoodTruck") String name,
            @Field("latFoodTruck") double lat,
            @Field("lonFoodTruck") double lon,
            @Field("heureDebutFoodTruck") String startTime,
            @Field("heureFinFoodTruck") String endTime,
            @Field("jourSemaineFoodTruck") String weekDay,
            @Field("idProprietaireFoodTruck") int idOwner,
            @Field("contactFoodTruck") String contact,
            @Field("imageFoodTruck") String image,
            @Field("noteFoodTruck") int rating,
            @Field("idProduits") String ids
    );
    Retrofit retrofitCreateFoodTruck = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("camions/proprietaire.php")
    @FormUrlEncoded
    Call<List<FoodTruck>> getFoodTruckByOwner(@Field("idProprietaire") int idOwner);
    Retrofit retrofitTruckByOwner = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(Authentication.getCredentials())
            .build();

    @Multipart
    @POST("fonctions.inc/sauvegarderImage.php")
    Call<ResponseBody> postImage(
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name);
    Retrofit retrofitUploadImage = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("avis/nouveau.php")
    @FormUrlEncoded
    Call<Void> addNewEvaluation(
            @Field("note") int ratingPoints,
            @Field("idFoodTruck") int idFoodTruck);
    Retrofit retrofitAddEvaluation = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("camions/modifier.php")
    @FormUrlEncoded
    Call<Void> updateFoodTruckInfos(
            @Field("nomFoodTruck") String truckName,
            @Field("imageFoodTruck") String truckImage,
            @Field("contactFoodTruck") String truckContact,
            @Field("idFoodTruck") int idFoodTruck);
    Retrofit retrofitUpdateFoodTruckInfos = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET
    Call<List<String>> getLocationsFoodTruck(
            @Query("idFoodTruck") int idFoodTruck);
    Retrofit retrofitGetLocationsFoodTruck = new Retrofit.Builder()
            .baseUrl("http://10.134.99.39/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}