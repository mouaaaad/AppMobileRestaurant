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

import com.example.app_restaurant.Model.Restaurant;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Actualite extends AppCompatActivity {
    SharedPreferences preferences ;
    BottomNavigationView bottomNavigationView;


    @BindView(R.id.list_restaurant_actualite)
    ListView listRestaurant ;

    ArrayList<Restaurant> restaurants;

    RestaurantAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualite);
        ButterKnife.bind(this);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.actualite);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeClient:
                        startActivity(new Intent(getApplicationContext(),HomeClient.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.actualite:
                        return true ;

                    case R.id.favorises:
                        startActivity(new Intent(getApplicationContext(),Favorises.class));
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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_restaurant = database.getReference("Restaurant");

        restaurants =new ArrayList<Restaurant>();
        table_restaurant.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null && dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int month=new Date().getMonth();
                        int year=new Date().getYear();
                        if(snapshot.child("date").child("month").getValue(int.class)==month&&snapshot.child("date").child("year").getValue(int.class)==year){
                        String nom =snapshot.getKey();
                        String adresse = snapshot.child("adresse").getValue(String.class);
                        String user = snapshot.child("user").getValue(String.class);
                        String type = snapshot.child("type").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        float rate=snapshot.child("rate").getValue(float.class);
                        restaurants.add(new Restaurant(nom,adresse,type,user,image,rate));}

                    }
                    String size =restaurants.size()+"";
                    Log.d("size 2",size);
                    adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                    listRestaurant.setAdapter(adapter);

                }
                else{
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("test","non");
            }        });




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