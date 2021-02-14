package com.example.app_restaurant.Model;

public class User {
    private String Name;
    private String Password;
    private String Email;
    private String type ;

    public User(){

    }

    public User(String name, String password,String type) {
        Name = name;
        Password = password;
        this.type=type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String phone) {
        Email = phone;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name)
    {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password){
        Password = password;
    }
}