package com.example.app_restaurant.ModelClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterface {
    @GET("/user")
    Call<UserClient> getUserByemail(@Query("email") String email);

    @GET("/manager")
    Call<Manager> getManagerByemail(@Query("email") String email);

    @GET("/client")
    Call<Client> getClientByemail(@Query("email") String email);

    @GET("/user/")
    Call<UserClient> getUserByusername(@Query("username") String username);

    @POST("/clients/new")
    Call<UserClient> postClients(@Body UserClient userClient );

    @POST("/managers/new")
    Call<UserClient> postManagers(@Body UserClient userClient );

    @PUT("/clients/edit")
    Call<Client> update(@Body Client client);
}
