package com.example.app_restaurant.Model;

public class menu {
    private String nom;
    private String prix;
    private String photo;
    private String detail;
    private String restaurant;

    public String getRestaurant() {
        return restaurant;
    }

    public String getNom() {
        return nom;
    }

    public String getPrix() {
        return prix;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDetail() {
        return detail;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public menu(String nom, String prix, String photo, String detail, String restaurant) {
        this.nom = nom;
        this.prix = prix;
        this.photo = photo;
        this.detail = detail;
        this.restaurant=restaurant;
    }
}
