package com.example.app_restaurant.ModelClient;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    @Expose
    private
    float id;
    @SerializedName("rate")
    @Expose
    private float rate;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("client")
    @Expose
    private Client client;
    @SerializedName("restaurant")
    @Expose
    private RestaurantClient restaurant;

    public float getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public RestaurantClient getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantClient restaurant) {
        this.restaurant = restaurant;
    }

    public Review(float rate, String comment, String createdAt, Client client, RestaurantClient restaurant) {
        this.rate = rate;
        this.comment = comment;
        this.createdAt = createdAt;
        this.client = client;
        this.restaurant = restaurant;
    }
}