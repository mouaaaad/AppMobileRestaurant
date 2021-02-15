package com.example.app_restaurant.ModelClient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantClient {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("picture")
    @Expose
    private Object picture;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("openingTime")
    @Expose
    private String openingTime;
    @SerializedName("closingTime")
    @Expose
    private String closingTime;
    @SerializedName("restaurantCategory")
    @Expose
    private RestaurantCategory restaurantCategory;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("manager")
    @Expose
    private Manager manager;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public RestaurantCategory getRestaurantCategory() {
        return restaurantCategory;
    }

    public void setRestaurantCategory(RestaurantCategory restaurantCategory) {
        this.restaurantCategory = restaurantCategory;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "RestaurantClient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture=" + picture +
                ", phone='" + phone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", city=" + city +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", restaurantCategory=" + restaurantCategory +
                ", manager=" + manager +
                '}';
    }

    public RestaurantClient(String name, Object picture, String phone, Double latitude, Double longitude, City city, String openingTime, String closingTime, RestaurantCategory restaurantCategory, String createdAt, Manager manager) {
        this.name = name;
        this.picture = picture;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.restaurantCategory = restaurantCategory;
        this.createdAt = createdAt;
        this.manager = manager;
    }
}

