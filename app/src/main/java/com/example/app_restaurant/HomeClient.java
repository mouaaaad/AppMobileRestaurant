package com.example.app_restaurant;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.Restaurant;

import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;

import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_restaurant.ModelClient.RestaurantClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClient extends AppCompatActivity implements OnItemSelectedListener {
    SharedPreferences preferences ;
    Spinner spinner ;
    String filter ;
    String url ="";
    ImageView imageViewT;
    @BindView(R.id.list_restaurant)
    ListView listRestaurant ;
    BottomNavigationView bottomNavigationView;
    NavController navController;
    ArrayList<Restaurant> restaurants;
    ArrayList<Restaurant> restaurantSearch;
    RestaurantAdapter adapter ;
    SearchView searchView ;
    RestaurantInterface api;
    private  StorageReference mStorageRef;
    Float rate;
    List<RestaurantClient> restaurantts ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);
        ButterKnife.bind(this);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.homeClient);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorises:
                        startActivity(new Intent(getApplicationContext(),Favorises.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.homeClient:
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
        ///

        ///
        spinner=(Spinner)findViewById(R.id.filtre);
        ArrayAdapter<CharSequence> adapterFilter=ArrayAdapter.createFromResource(this,R.array.filters, android.R.layout.simple_spinner_item);
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterFilter);
        spinner.setOnItemSelectedListener(this);
        //
        restaurants =new ArrayList<Restaurant>();
        api=ApiClient.getClient().create(RestaurantInterface.class);
        Call<List<RestaurantClient>> call= api.getRestaurantClients();
        call.enqueue(new Callback<List<RestaurantClient>>() {
            @Override
            public void onResponse(Call<List<RestaurantClient>> call, Response<List<RestaurantClient>> response) {
                Log.e("tttttt",response.body()+"");
                 restaurantts=response.body();
                for (RestaurantClient restaurant : restaurantts){
                    RestaurantInterface apiRate = ApiClient.getClient().create(RestaurantInterface.class);
                    Call<Float> doubleCall= apiRate.getRate(restaurant.getId());
                    doubleCall.enqueue(new Callback<Float>() {
                        @Override
                        public void onResponse(Call<Float> call, Response<Float> response) {
                            rate=response.body();
                            restaurants.add(new Restaurant(restaurant.getName(),restaurant.getCity().getCity(),restaurant.getRestaurantCategory().getCategory(),restaurant.getManager().getUsername(),restaurant.getPicture().toString(),rate));
                            adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                            listRestaurant.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<Float> call, Throwable t) {
                            Log.d("Nooon",rate+"");
                            restaurants.add(new Restaurant(restaurant.getName(),restaurant.getCity().getCity(),restaurant.getRestaurantCategory().getCategory(),restaurant.getManager().getUsername(),restaurant.getPicture().toString(),0));
                            adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurants);
                            listRestaurant.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<RestaurantClient>> call, Throwable t) {
                Log.e("rrrrr",t.getLocalizedMessage()+"");
            }
        });
        ///


/*        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_restaurant = database.getReference("Restaurant");
        mStorageRef= FirebaseStorage.getInstance().getReference();
        restaurants =new ArrayList<Restaurant>();
        table_restaurant.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null && dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String nom =snapshot.getKey();
                        String adresse = snapshot.child("adresse").getValue(String.class);
                        String user = snapshot.child("user").getValue(String.class);
                        String type = snapshot.child("type").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        float rate =snapshot.child("rate").getValue(float.class);
                        restaurants.add(new Restaurant(nom,adresse,type,user,image,rate));
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
            }        });
*/
        searchView = (SearchView) findViewById(R.id.search);
        restaurantSearch=new ArrayList<Restaurant>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                restaurantSearch=new ArrayList<Restaurant>();
                for (Restaurant restaurant : restaurants) {
                    if (filter.equals("Nom")) {
                        if (restaurant.getNom().equals(query)) {
                            restaurantSearch.add(restaurant);
                        }
                    } else if (filter.equals("Categorie")) {
                        if (restaurant.getType().equals(query)) {
                            restaurantSearch.add(restaurant);
                        }
                    }
                    else {
                        if (restaurant.getAdresse().equals(query)) {
                            restaurantSearch.add(restaurant);
                        }
                    }
                }
                adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurantSearch);
                listRestaurant.setAdapter(adapter);
                //((RestaurantAdapter)listRestaurant.getAdapter()).update(restaurantSearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //adapter.getFilter().filter(newText);
                if(query.equals("")) {
                    adapter = new RestaurantAdapter(getApplicationContext(), R.layout.structure_restaurant, restaurants);
                    listRestaurant.setAdapter(adapter);
                }
                else {
                    restaurantSearch=new ArrayList<Restaurant>();
                    for (Restaurant restaurant : restaurants) {
                        if (filter.equals("Nom")) {
                            if (restaurant.getNom().toLowerCase().contains(query.toLowerCase())) {
                                restaurantSearch.add(restaurant);
                            }
                        } else if (filter.equals("Categorie")) {
                            if (restaurant.getType().toLowerCase().contains(query.toLowerCase())) {
                                restaurantSearch.add(restaurant);
                            }
                        }
                        else {
                            if (restaurant.getAdresse().toLowerCase().contains(query.toLowerCase())) {
                                restaurantSearch.add(restaurant);
                            }
                        }
                    }
                    adapter=new RestaurantAdapter(getApplicationContext(),R.layout.structure_restaurant,restaurantSearch);
                    listRestaurant.setAdapter(adapter);
                }
                return false;
            }
        });
    }


    @OnItemClick(R.id.list_restaurant)
    public void onListCategorieItemClick(int position){
        Restaurant restaurant ;
        restaurant=restaurants.get(position);

    }

    public String afficherImage(String image){
        StorageReference islandRef = mStorageRef.child("images/"+image);
        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               url=uri.toString();
            }

        });

        //StorageReference monImage =mStorageRef.child(image);

      //  StorageReference monImage =mStorageRef.child(image);
      // FirebaseStorage storage = FirebaseStorage.getInstance();

      //  StorageReference monImage =FirebaseStorage.getInstance().getReference(image);
        //StorageReference url=storage.getReferenceFromUrl(monImage.toString()).child(image);
         //Log.d("tttttttttt",url.toString());
        //StorageReference url=storage.getReference(monImage.);
      // Glide.with(this).load(monImage.getRoot()).into(imageViewT) ;
      /* File localFile =null;
        try{
            localFile = File.createTempFile("images","jpeg");
        }
        catch(IOException e){
            e.printStackTrace();
        }

        monImage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                monImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TAG", "onSuccess: ");
                        Picasso.get().load(uri).into(imageViewT);

                    }
                });
            }
        });*/

          return url ;
        }

        @OnItemClick(R.id.list_restaurant)
        public void  onListRestaurantItemClicked(int position) {
            Restaurant restaurant ;
            restaurant=restaurants.get(position);
            String nom_restaurant=restaurant.getNom();
            Intent RestaurantActivity = new Intent(this, DetailRestaurant.class);
            RestaurantActivity.putExtra("nom_restaurant",nom_restaurant);
            startActivity(RestaurantActivity);

        }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        filter=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

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