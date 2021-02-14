package com.example.app_restaurant.Model;

public class Commentaire {
    private  String titre ;
    private String commentaire ;
    private String user;
    private float rate ;

    public Commentaire(String commentaire,float rate) {
        this.commentaire = commentaire;
        this.rate=rate;
    }

    public Commentaire(String titre, String commentaire,float rate) {
        this.titre = titre;
        this.commentaire = commentaire;
        this.rate=rate;
    }

    public Commentaire(String titre, String commentaire, String user) {
        this.titre = titre;
        this.commentaire = commentaire;
        this.user = user;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRate() {
        return rate;
    }

    public String getTitre() {
        return titre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getUser() {
        return user;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
