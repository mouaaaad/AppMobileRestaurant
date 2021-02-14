package com.example.app_restaurant.Model;

import java.util.Date;

public class Restaurant {
    private String nom;
    private String adresse;
    private String type;
    private String user;
    private String image;
    private Date  date;
    private float rate ;
    private String ouverture;
    private String fermeture;
    private Double latitude;
    private Double longitude;

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getOuverture() {
        return ouverture;
    }

    public String getFermeture() {
        return fermeture;
    }

    public String getUser() {
        return user;
    }

    public String getImage() {
        return image;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setOuverture(String ouverture) {
        this.ouverture = ouverture;
    }

    public void setFermeture(String fermeture) {
        this.fermeture = fermeture;
    }

    public Restaurant(String nom, String adresse, String type, String user, String image, float rate) {
        this.nom=nom;
        this.adresse = adresse;
        this.type = type;
        this.user = user;
        this.image = image;
        this.rate=rate;
    }
    public Restaurant(String adresse, String type, String user, String image, float rate, Date date, String ouverture, String fermeture) {
        this.nom=nom;
        this.date=date;
        this.adresse = adresse;
        this.type = type;
        this.user = user;
        this.image = image;
        this.rate=rate;
        this.ouverture=ouverture;
        this.fermeture=fermeture;
    }
    public Restaurant(String adresse, String type, String user, String image, float rate, Date date, String ouverture, String fermeture,double latitude,double longitude) {
        this.nom=nom;
        this.date=date;
        this.adresse = adresse;
        this.type = type;
        this.user = user;
        this.image = image;
        this.rate=rate;
        this.ouverture=ouverture;
        this.fermeture=fermeture;
        this.latitude=latitude;
        this.longitude=longitude;   }

    public Restaurant(String adresse, String type, String user, String image, float rate, Date date) {
        this.adresse = adresse;
        this.type = type;
        this.user = user;
        this.image = image;
        this.rate=rate;
        this.date=date;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRate() {
        return rate;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getType() {
        return type;
    }
    public Date getDate() {
        return date;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
