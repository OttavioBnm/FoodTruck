/**
 * Projet       : Food Truck Tracker (application Android)
 * Package      : Models
 * Classe       : FoodTruck.java
 * Description  : Classe modélisant un food truck, cette classe peut être sérialisée
 * Auteur       : Ottavio Buonomo
 * Date         : 07.06.2018
 * Version      : 1.0
 */

package com.buonomo.cfpt.foodtrucktracker.Models;

import android.location.Address;
import android.location.Geocoder;

import com.buonomo.cfpt.foodtrucktracker.Controleurs.Activites.MainActivity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class FoodTruck implements Serializable {

    // Champs
    @SerializedName("idFoodTruck")
    @Expose
    private int idFoodTruck;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("evaluation")
    @Expose
    private String evaluation;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("heureDebut")
    @Expose
    private String heureDebut;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("heureFin")
    @Expose
    private String heureFin;
    @SerializedName("jourSemaine")
    @Expose
    private String jourSemaine;
    @SerializedName("lstProduits")
    @Expose
    private String lstProduits;
    @SerializedName("contact")
    @Expose
    private String contact;

    // Propriétées
    public int getIdFoodTruck() {
        return idFoodTruck;
    }

    public void setIdFoodTruck(int idFoodTruck) {
        this.idFoodTruck = idFoodTruck;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getJourSemaine() {
        return jourSemaine;
    }

    public void setJourSemaine(String jourSemaine) {
        this.jourSemaine = jourSemaine;
    }

    public String getLstProduits() {
        return lstProduits;
    }

    public void setLstProduits(String lstProduits) {
        this.lstProduits = lstProduits;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @SerializedName("idProprietaire")
    @Expose
    private int idProprietaire;

    public int getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(int idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    // Méthodes

    /**
     * Récupère l'adresse postale depuis une latitude et une longitude
     * @return Adresse postale du food truck
     */
    public String getAdressePostale(){
        Geocoder g = new Geocoder(MainActivity.getContext(), Locale.getDefault());
        try {
            List<Address> a = g.getFromLocation(this.getLatitude(), this.getLongitude(), 1);
            String[] adresse = a.get(0).getAddressLine(0).split(",");
            return adresse[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
