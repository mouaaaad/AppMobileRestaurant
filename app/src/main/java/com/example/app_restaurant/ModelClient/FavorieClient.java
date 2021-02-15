package com.example.app_restaurant.ModelClient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavorieClient {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("client")
    @Expose
    private Client client;
    @SerializedName("restaurant")
    @Expose
    private RestaurantClient restaurant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public FavorieClient(Client client, RestaurantClient restaurant) {
        this.client = client;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "FavorieClient{" +
                "id=" + id +
                ", client=" + client +
                ", restaurant=" + restaurant +
                '}';
    }
}