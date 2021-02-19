package com.example.app_restaurant;

import com.example.app_restaurant.Model.User;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserUnitTest {
    @Test
    public void UserTestGeter() throws Exception {
        User user = new User("nisrine","1234","Client");
        String name= user.getName();
        String pass=user.getPassword();
        String type=user.getType();

        assertEquals(name, "nisrine");
        assertEquals(pass, "1234");
        assertEquals(type, "Client");
    }
    @Test
    public void UserTestSeter() throws Exception {
        User user = new User("nisrine","1234","Client");
        user.setName("mouad");
        user.setPassword("0000");
        user.setType("Entreprise");
        String name= user.getName();
        String pass=user.getPassword();
        String type=user.getType();

        assertEquals(name, "mouad");
        assertEquals(pass, "0000");
        assertEquals(type, "Entreprise");
    }
    @Test
    public void User() {
        try {
            User user = new User("nisrine","1234","Client");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}