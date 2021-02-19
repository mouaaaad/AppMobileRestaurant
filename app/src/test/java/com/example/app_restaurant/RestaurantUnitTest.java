package com.example.app_restaurant;
import com.example.app_restaurant.Model.Restaurant;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
public class RestaurantUnitTest {

        @Test
        public void restaurant()
        {
            try {
                Restaurant restaurant=new Restaurant("restaurant1","adresse1", "type1","user1","image1", (float) 5.0);
                Restaurant restaurant1=new Restaurant("adresse2","type2","user2","image2",(float)2.0,new Date());
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }

        @Test
        public void restaurantTestGeter()
        {
            Restaurant restaurant=new Restaurant("restaurant1","adresse1", "type1","user1","image1", 5);
            Restaurant restaurant1=new Restaurant("adresse2","type2","user2","image2",2,new Date());
            String nom=restaurant.getNom();
            String adresse1=restaurant.getAdresse();
            String adresse2=restaurant1.getAdresse();
            String type1 = restaurant.getType();
            String type2=restaurant1.getType();
            String user1=restaurant.getUser();
            String user2=restaurant1.getUser();
            String image1=restaurant.getImage();
            String image2=restaurant1.getImage();
            Float rate=restaurant.getRate();
            Float rate2=restaurant1.getRate();
            assertEquals(nom, "restaurant1");
            assertEquals(adresse1, "adresse1");
            assertEquals(adresse2, "adresse2");
            assertEquals(type1, "type1");
            assertEquals(type2, "type2");
            assertEquals(user1, "user1");
            assertEquals(user2, "user2");
            assertEquals(image1, "image1");
            assertEquals(image2, "image2");
            assertEquals(rate.toString(), "5.0");
            assertEquals(rate2.toString(), "2.0");

        }

    @Test
    public void restaurantTestSeter()
    {
        Restaurant restaurant=new Restaurant("restaurant1","adresse1", "type1","user1","image1", 5);
        Restaurant restaurant1=new Restaurant("adresse2","type2","user2","image2",2,new Date());
        restaurant.setNom("restaurant0");
        restaurant.setAdresse("adresse0");
        restaurant1.setAdresse("adresse3");
        restaurant.setType("type0");
        restaurant1.setType("type3");
        restaurant.setUser("user0");
        restaurant1.setUser("user3");
        restaurant.setImage("image0");
        restaurant1.setImage("image3");
        restaurant.setRate(4);
        restaurant1.setRate(1);
        String nom=restaurant.getNom();
        String adresse1=restaurant.getAdresse();
        String adresse2=restaurant1.getAdresse();
        String type1 = restaurant.getType();
        String type2=restaurant1.getType();
        String user1=restaurant.getUser();
        String user2=restaurant1.getUser();
        String image1=restaurant.getImage();
        String image2=restaurant1.getImage();
        Float rate=restaurant.getRate();
        Float rate2=restaurant1.getRate();
        assertEquals(nom, "restaurant0");
        assertEquals(adresse1, "adresse0");
        assertEquals(adresse2, "adresse3");
        assertEquals(type1, "type0");
        assertEquals(type2, "type3");
        assertEquals(user1, "user0");
        assertEquals(user2, "user3");
        assertEquals(image1, "image0");
        assertEquals(image2, "image3");
        assertEquals(rate.toString(), "4.0");
        assertEquals(rate2.toString(), "1.0");

    }

}
