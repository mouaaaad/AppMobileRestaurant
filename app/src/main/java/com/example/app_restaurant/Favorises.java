package com.example.app_restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.Restaurant;

import com.example.app_restaurant.ModelClient.Client;
import com.example.app_restaurant.ModelClient.FavorieClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.example.app_restaurant.ModelClient.UserInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favorises extends AppCompatActivity {
    SharedPreferences preferences ;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    @BindView(R.id.list_restaurant_favorie)
    ListView listRestaurant ;
    ArrayList<Restaurant> restaurants;
    RestaurantAdapter adapter ;
    RestaurantInterface apiFavorie;
    UserInterface ApiUser;
    Client user ;
    private String login;
    Float rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorises);
        ButterKnife.bind(this);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.favorises);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeClient:
                        startActivity(new Intent(getApplicationContext(),HomeClient.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.favorises:
                        return true ;

                    case R.id.actualite:
                        startActivity(new Intent(getApplicationContext(),Actualite.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.pagePersonnel:
                        startActivity(new Intent(getApplicationContext(),PagePersonnel.class));
                        overridePendingTransition(0,0);
                        return true ;
                }
                return false;
            }
        });
        restaurants =new ArrayList<Restaurant>();
        sharedPreferences = getSharedPreferences("myRef", MODE_PRIVATE);
        login = sharedPreferences.getString("Login", null);
        ApiUser=  ApiClient.getClient().create(UserInterface.class);
        Call<Client> clientCall= ApiUser.getClientByemail(login);
        clientCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                user=response.body();
                apiFavorie= ApiClient.getClient().create(RestaurantInterface.class);
                Call<List<FavorieClient>> favorieClientCall= apiFavorie.getFavorieClient(user.getId());
                favorieClientCall.enqueue(new Callback<List<FavorieClient>>() {
                    @Override
                    public void onResponse(Call<List<FavorieClient>> call, Response<List<FavorieClient>> response) {
                        List<FavorieClient> restaurantts=response.body();
                        Log.d("restaurantFF",response.body()+"");
                        if(restaurantts!=null)
                        for (FavorieClient restaurant : restaurantts){
                            RestaurantInterface apiRate = ApiClient.getClient().create(RestaurantInterface.class);
                            Call<Float> doubleCall= apiRate.getRate(restaurant.getId());
                            doubleCall.enqueue(new Callback<Float>() {
                                @Override
                                public void onResponse(Call<Float> call, Response<Float> response) {
                                    rate=response.body();
                                    restaurants.add(new Restaurant(restaurant.getRestaurant().getName(),restaurant.getRestaurant().getCity().getCity(),restaurant.getRestaurant().getRestaurantCategory().getCategory(),restaurant.getRestaurant().getManager().getUsername(),restaurant.getRestaurant().getPicture().toString(),rate));
                                    adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                                    listRestaurant.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(Call<Float> call, Throwable t) {
                                    restaurants.add(new Restaurant(restaurant.getRestaurant().getName(),restaurant.getRestaurant().getCity().getCity(),restaurant.getRestaurant().getRestaurantCategory().getCategory(),restaurant.getRestaurant().getManager().getUsername(),restaurant.getRestaurant().getPicture().toString(),0));
                                    adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                                    listRestaurant.setAdapter(adapter);
                                }
                            });
                        }
                        adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                        listRestaurant.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<FavorieClient>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });


/*
        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);;
        String phone =sharedPreferences.getString("Login",null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_restaurant = database.getReference("Restaurant");
        db_favorie=new data(this);
        restaurants =new ArrayList<Restaurant>();
        table_restaurant.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null && dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String[] whereArgs = new String[] {
                                String.valueOf(snapshot.getKey())};
                        SQLiteDatabase data = db_favorie.getReadableDatabase();
                        Cursor cursor =data.query("List_Favorie",null,"Restaurant_name=?",whereArgs,null,null,null);
                        if(cursor.moveToFirst()){
                        String nom =snapshot.getKey();
                        String adresse = snapshot.child("adresse").getValue(String.class);
                        String user = snapshot.child("user").getValue(String.class);
                        String type = snapshot.child("type").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        float rate =snapshot.child("rate").getValue(float.class);
                        restaurants.add(new Restaurant(nom,adresse,type,user,image,rate));
                        }
                    }
                    adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                    listRestaurant.setAdapter(adapter);

                }
                else{
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }        });*/

}
    @OnItemClick(R.id.list_restaurant_favorie)
    public void  onListRestaurantFavorieItemClicked(int position) {
        Restaurant restaurant ;
        restaurant=restaurants.get(position);
        String nom_restaurant=restaurant.getNom();
        Intent RestaurantActivity = new Intent(this, DetailRestaurant.class);
        RestaurantActivity.putExtra("nom_restaurant",nom_restaurant);
        startActivity(RestaurantActivity);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sign_out :
            {

                preferences=getSharedPreferences("myRef",MODE_PRIVATE);
                preferences.edit().remove("username").commit();
                preferences.edit().remove("password").commit();

                Toast.makeText(this, "Déconnecté", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}