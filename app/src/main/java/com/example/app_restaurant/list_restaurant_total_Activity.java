package com.example.app_restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.Restaurant;
import com.example.app_restaurant.ModelClient.RestaurantClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class list_restaurant_total_Activity extends AppCompatActivity {
    SharedPreferences preferences ;
    ArrayList<Restaurant> restaurants;
    RestaurantAdapter adapter ;
    String num;
    private StorageReference mStorageRef;
    SwipeMenuListView listRestaurantTotal;
    SwipeMenuCreator creator;
    RestaurantInterface api;
    RestaurantInterface apiDelete;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurant_total_);
        listRestaurantTotal=(SwipeMenuListView)findViewById(R.id.list_restaurant_total);
        ButterKnife.bind(this);
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                openItem.setWidth((90));
                // set item title

                // set item title fontsize
                openItem.setTitleSize(18);
                openItem.setIcon(R.drawable.ic_delete_foreground);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_update_foreground);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listRestaurantTotal.setMenuCreator(creator);
        Bundle extras=getIntent().getExtras();
        num=new String(extras.getString("user"));

       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_restaurant = database.getReference("Restaurant");
        mStorageRef= FirebaseStorage.getInstance().getReference();*/
        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);
        restaurants =new ArrayList<Restaurant>();
        api= ApiClient.getClient().create(RestaurantInterface.class);
        Call<List<RestaurantClient>> call= api.getRestaurantByEmail(sharedPreferences.getString("Login","null"));
        call.enqueue(new Callback<List<RestaurantClient>>() {
            @Override
            public void onResponse(Call<List<RestaurantClient>> call, Response<List<RestaurantClient>> response) {
                Log.e("tttttt",response.body()+"");
                List<RestaurantClient> restaurantts=response.body();
                for (RestaurantClient restaurant : restaurantts){
                    restaurants.add(new Restaurant(restaurant.getName(),restaurant.getCity().getCity(),restaurant.getRestaurantCategory().getCategory(),restaurant.getManager().getUsername(),"",0));
                }
                adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                listRestaurantTotal.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<RestaurantClient>> call, Throwable t) {
                Log.e("rrrrr",t.getLocalizedMessage()+"");
            }
        });
        /*table_restaurant.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null && dataSnapshot.exists()) {


                    //progressDialog.dismiss();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if( snapshot.child("user").getValue(String.class).equals(num))
                        {
                            String nom =snapshot.getKey();
                            String adresse = snapshot.child("adresse").getValue(String.class);
                            String user = snapshot.child("user").getValue(String.class);
                            String type = snapshot.child("type").getValue(String.class);
                            String image = snapshot.child("image").getValue(String.class);
                            float rate =snapshot.child("rate").getValue(float.class);
                            restaurants.add(new Restaurant(nom,adresse,type,user,image,rate));
                        }

                    }
                    String size =restaurants.size()+"";
                    Log.d("size 2",size);
                    adapter=new RestaurantAdapter(getApplicationContext(), R.layout.activity_list_restaurant_total_,restaurants);
                    listRestaurantTotal.setAdapter(adapter);

                }
                else{
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("test","non");
            }        });*/

        listRestaurantTotal.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override

            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Restaurant restaurant ;
                        restaurant=restaurants.get(position);
                        //FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //DatabaseReference table_res = database.getReference("Restaurant");
                        //table_res.child(restaurant.getNom()).removeValue();
                        apiDelete= ApiClient.getClient().create(RestaurantInterface.class);
                        Call<Void> voidCall= api.deleteRestaurant(restaurant.getNom());
                        voidCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        restaurants.clear();
                        //restaurants.remove(restaurant);
                        adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                        listRestaurantTotal.setAdapter(adapter);
                        break;
                }

                return false;
            }
        });


    }

    @OnItemClick(R.id.list_restaurant_total)
    public void onListCategorieItemClick(int position){
        Restaurant restaurant ;
        restaurant=restaurants.get(position);
        Log.d("restaurant",restaurant.getNom());
        Intent home = new Intent(list_restaurant_total_Activity.this, ListeMenuActivity.class);
        //Intent home = new Intent(SignIn.this, AddRestaurantActivity.class);
        home.putExtra("restaurant",restaurant.getNom());
        startActivity(home);
    }
    @OnClick(R.id.nouveau_restaurant)
    public void onClick(){

        Intent home = new Intent(list_restaurant_total_Activity.this, AddRestaurantActivity.class);
        home.putExtra("user",num);
        startActivity(home);
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