package com.example.app_restaurant.ModelClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestaurantInterface {
    @GET("/restaurants")
    Call<List<RestaurantClient>> getRestaurantClients();
    @GET("/restaurants/actualite")
    Call<List<RestaurantClient>> getActualite();

    @GET("/restaurant/{email}")
    Call<List<RestaurantClient>> getRestaurantByEmail(@Path("email") String email);

    @GET("/restaurant/name/{name}")
    Call<RestaurantClient> getRestaurantByName(@Path("name") String name);

    @GET("/restaurantCategorie/{categorie}")
    Call<RestaurantCategory> getCategotie(@Path("categorie") String categorie);

    @GET("/favoriteRestaurant/{id_c}/{id_r}")
    Call<FavorieClient> getFavorie(@Path("id_c") Integer id_c,@Path("id_r") Integer id_r);

    @GET("/favoriteRestaurant/{id_c}")
    Call<List<FavorieClient>> getFavorieClient(@Path("id_c") Integer id_c);

    @GET("/citie/{city}")
    Call<City> getCity(@Path("city") String city);
    /*@GET("/rates/{id}/rate")
    Call Double getRate(@Path("id") int id);*/

    @POST("/restaurants/new")
    Call<RestaurantClient> postRestaurant(@Body RestaurantClient restaurantClient );

    @POST("/meals/new")
    Call<Meal> postMeal(@Body Meal meal );

    @POST("/reviews/new")
    Call<Review> postReview(@Body Review review );

    @POST("/favoriteRestaurants/new")
    Call<FavorieClient> postFavorie(@Body FavorieClient favorieClient );

    @DELETE("/favoriteRestaurants/delete/{id_c}/{id_r}")
    Call<FavorieClient> deleteFavorie(@Path("id_c") Integer id_c,@Path("id_r") Integer id_r );

    @DELETE("/restaurants/delete/{name}")
    Call<Void> deleteRestaurant(@Path("name") String name );

    @DELETE("/meals/delete/{meal}")
    Call<Void> deleteMeal(@Path("meal") String meal );

    //menu
    @GET("/meal/{name}")
    Call<List<Meal>> getMeal(@Path("name") String name);

    @GET("/review/{id}")
    Call<List<Review>> getReview(@Path("id") Integer id);
    @GET("reviews/{id}/rate")
    Call<Float> getRate(@Path("id") Integer id);

}
