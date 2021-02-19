package com.example.app_restaurant;

import com.example.app_restaurant.Model.menu;
import org.junit.Test;

import static org.junit.Assert.*;
public class MenuUnitTest {
    @Test
    public void MenuTestGeter() throws Exception {
     menu Menu=new  menu("Plat1","80","photo1","detail1","Primos");
     String nom=Menu.getNom();
     String prix=Menu.getPrix();
     String photo=Menu.getPhoto();
     String detail=Menu.getDetail();
     String restaurant=Menu.getRestaurant();
        assertEquals(nom, "Plat1");
        assertEquals(prix, "80");
        assertEquals(photo, "photo1");
        assertEquals(detail, "detail1");
        assertEquals(restaurant, "Primos");
    }
    @Test
    public void MenuTestSeter() throws Exception {
        menu Menu=new  menu("Plat1","80","photo1","detail1","Primos");
        Menu.setNom("Plat2");
        Menu.setPrix("70");
        Menu.setPhoto("photo2");
        Menu.setDetail("detail2");
        Menu.setRestaurant("Metros Pizza");



        String nom=Menu.getNom();
        String prix=Menu.getPrix();
        String photo=Menu.getPhoto();
        String detail=Menu.getDetail();
        String restaurant=Menu.getRestaurant();
        assertEquals(nom, "Plat2");
        assertEquals(prix, "70");
        assertEquals(photo, "photo2");
        assertEquals(detail, "detail2");
        assertEquals(restaurant, "Metros Pizza");
    }
    @Test
    public void Menu() {
        try {
            menu Menu=new  menu("Plat1","80","photo1","detail1","Primos");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
