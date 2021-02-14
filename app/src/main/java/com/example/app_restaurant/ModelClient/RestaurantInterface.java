package com.example.app_restaurant.ModelClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestaurantInterface {
    @GET("/restaurants")
    Call<List<RestaurantClient>> getRestaurantClients();

    @POST("/restaurants/new")
    Call<RestaurantClient> postRestaurant(@Body RestaurantClient restaurantClient );
}
