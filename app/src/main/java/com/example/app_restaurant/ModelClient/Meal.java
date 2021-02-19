package com.example.app_restaurant.ModelClient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meal {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("meal")
    @Expose
    private String meal;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("restaurant")
    @Expose
    private RestaurantClient restaurant;
    @SerializedName("mealCategory")
    @Expose
    private MealCategory mealCategory;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Meal(String meal, Double price, String detail,String photo, RestaurantClient restaurant, MealCategory mealCategory) {
        this.meal = meal;
        this.price = price;
        this.detail = detail;
        this.restaurant = restaurant;
        this.mealCategory = mealCategory;
        this.photo=photo;
    }

    public RestaurantClient getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantClient restaurant) {
        this.restaurant = restaurant;
    }

    public MealCategory getMealCategory() {
        return mealCategory;
    }

    public void setMealCategory(MealCategory mealCategory) {
        this.mealCategory = mealCategory;
    }

}