package com.example.app_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.menu;
import com.example.app_restaurant.ModelClient.Meal;
import com.example.app_restaurant.ModelClient.RestaurantInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisiteMenu extends AppCompatActivity {
    String res;
    RestaurantInterface apiMenu;
    ArrayList<menu> menus;
    MenuAdapter adapter ;
    SwipeMenuListView listMenuTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visite_menu);
        ButterKnife.bind(this);
        listMenuTotal=(SwipeMenuListView)findViewById(R.id.list_menus);

        Bundle extras = getIntent().getExtras();
        String nom_restaurant = extras.getString("nom_restaurant");
        menus =new ArrayList<menu>();
        apiMenu= ApiClient.getClient().create(RestaurantInterface.class);
        Call<List<Meal>> call= apiMenu.getMeal(nom_restaurant);
        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                List<Meal> meals =response.body();

                for(Meal meal : meals){
                    Log.d("menuus",response.body()+"");
                    menus.add(new menu(meal.getMeal(),meal.getPrice()+"Dh",meal.getPhoto(),meal.getDetail(),meal.getRestaurant().getName()));
                }

                adapter=new MenuAdapter(getApplicationContext(), R.layout.activity_liste_menu,menus);
                listMenuTotal.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                Log.d("ttttt",t.getLocalizedMessage());
            }
        });

    }
}