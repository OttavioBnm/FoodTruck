package com.buonomo.cfpt.foodtrucktracker.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner implements Serializable
{
    @SerializedName("idProprietaire")
    @Expose
    private String idProprietaire;
    @SerializedName("Nom")
    @Expose
    private String nom;
    @SerializedName("Prenom")
    @Expose
    private String prenom;
    @SerializedName("Pseudo")
    @Expose
    private String pseudo;
    @SerializedName("Courriel")
    @Expose
    private String courriel;
    @SerializedName("MotDePasse")
    @Expose
    private String motDePasse;

    public String getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(String idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

}
