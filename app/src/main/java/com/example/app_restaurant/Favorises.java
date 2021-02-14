package com.example.app_restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_restaurant.Model.Restaurant;

import com.example.app_restaurant.dataBase.data;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class Favorises extends AppCompatActivity {
    SharedPreferences preferences ;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    @BindView(R.id.list_restaurant_favorie)
    ListView listRestaurant ;
    ArrayList<Restaurant> restaurants;
    RestaurantAdapter adapter ;
    data db_favorie ;
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
            }        });

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