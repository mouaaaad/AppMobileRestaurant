package com.example.app_restaurant;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.ModelClient.Client;
import com.example.app_restaurant.ModelClient.FavorieClient;
import com.example.app_restaurant.ModelClient.RestaurantClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.example.app_restaurant.ModelClient.Review;
import com.example.app_restaurant.ModelClient.UserInterface;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app_restaurant.Model.Commentaire;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRestaurant extends FragmentActivity implements OnMapReadyCallback {
    SharedPreferences preferences ;
    private double latitude ;
    private double longitude ;
    private float rate_total;
    private DatabaseReference table_favorie;
    private DatabaseReference table_restaurant;
    private GoogleMap mMap;
    private String login;
    private String nom_restaurant;
    private SharedPreferences sharedPreferences;
    ArrayList<Commentaire> commentaires;
    CommetaireAdapter adapter;
    @BindView(R.id.list_commentaire)
    ListView listCommentaire;
    @BindView(R.id.name_restaurant_detail)
    TextView textViewNom;
    @BindView(R.id.favorise_restaurant)
    ImageView imageViewFavorise;
    @BindView(R.id.adress_restaurant_detail)
    TextView adress_restaurant;
    @BindView(R.id.categorie_restaurant)
    TextView categorie_restaurant;
    @BindView(R.id.num_restaurant_detail)
    TextView num_restaurant;
    @BindView(R.id.img_restaurant_detail)
    ImageView img_restaurant_detail;
    @BindView(R.id.horaire_restaurant_detail)
    TextView textViewHoraire ;
    RestaurantInterface apiRestaurant;
    RestaurantInterface api;
    RestaurantInterface apiFavorie;
    UserInterface ApiUser;
     Client user ;
     RestaurantClient restaurantClient ;
    FavorieClient favorieClient ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("myRef", MODE_PRIVATE);
        login = sharedPreferences.getString("Login", null);
        Bundle extras = getIntent().getExtras();
        nom_restaurant = extras.getString("nom_restaurant");
        textViewNom.setText(nom_restaurant);

        //
        commentaires = new ArrayList<Commentaire>();
        apiRestaurant= ApiClient.getClient().create(RestaurantInterface.class);
        Call<RestaurantClient> call= apiRestaurant.getRestaurantByName(nom_restaurant);
        call.enqueue(new Callback<RestaurantClient>() {
            @Override
            public void onResponse(Call<RestaurantClient> call, Response<RestaurantClient> response) {
                RestaurantInterface apiReview = ApiClient.getClient().create(RestaurantInterface.class);
                Call<List<Review>> reviewCall= apiReview.getReview(response.body().getId());
                reviewCall.enqueue(new Callback<List<Review>>() {
                    @Override
                    public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                        List<Review> reviews=response.body();
                        for (Review review: reviews){
                            commentaires.add(new Commentaire("Commentaire", review.getComment(), review.getRate()));
                        }
                        adapter = new CommetaireAdapter(getApplicationContext(), R.layout.structure_commentaire, commentaires);
                        listCommentaire.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Review>> call, Throwable t) {

                    }
                });
                restaurantClient=response.body();
                String adress = restaurantClient.getCity().getCity();
                adress_restaurant.setText(adress);
//                String image = restaurantClient.getPicture().toString();
          //      Glide.with(DetailRestaurant.this).load(image).into(img_restaurant_detail);
                String num = restaurantClient.getPhone();
                num_restaurant.setText(num);
                String categorie = restaurantClient.getRestaurantCategory().getCategory();
                categorie_restaurant.setText(categorie);
                String ouverture =restaurantClient.getOpeningTime();
                String fermeture =restaurantClient.getClosingTime();
                String horaire=ouverture+"-"+fermeture;
                textViewHoraire.setText(horaire);
                latitude=restaurantClient.getLatitude();
                longitude=restaurantClient.getLongitude();
                //getClient
                ApiUser=  ApiClient.getClient().create(UserInterface.class);
                Log.d("Emaaail",login);
                Call<Client> clientCall= ApiUser.getClientByemail(login);
                clientCall.enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        Log.e("ClientR",response.body()+"");
                        user=response.body();
                        //favorie
                        apiFavorie= ApiClient.getClient().create(RestaurantInterface.class);
                        Call<FavorieClient> favorieClientCall= apiFavorie.getFavorie(user.getId(),restaurantClient.getId());
                        favorieClientCall.enqueue(new Callback<FavorieClient>() {
                            @Override
                            public void onResponse(Call<FavorieClient> call, Response<FavorieClient> response) {
                                if(response.body()!=null){
                                    favorieClient=response.body();
                                    imageViewFavorise.setImageResource(R.drawable.ic_favorite);
                                    imageViewFavorise.setTag("2");
                                }
                            }

                            @Override
                            public void onFailure(Call<FavorieClient> call, Throwable t) {

                            }
                        });
                        //
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {

                    }
                });
                //
            }

            @Override
            public void onFailure(Call<RestaurantClient> call, Throwable t) {
                    Log.d("restaurantN",t.getLocalizedMessage()+"");
            }
        });
        //

       // db_favorie = new data(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_favorie = database.getReference("Favorie");

     /*   String[] whereArgs = new String[]{
                String.valueOf(nom_restaurant)};

        SQLiteDatabase data = db_favorie.getReadableDatabase();
        //Cursor cursor =data.query("Restaurant",null,null,null,null,null,null);
        Cursor cursor = data.query("List_Favorie", null, "Restaurant_name=?", whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            imageViewFavorise.setImageResource(R.drawable.ic_favorite);
            imageViewFavorise.setTag("2");
        }*/


    /*    final DatabaseReference table_commentaire = database.getReference("Commentaire");

        commentaires = new ArrayList<Commentaire>();
        table_commentaire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.exists()) {

                    rate_total = 0;
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.child(nom_restaurant).getChildren()) {
                        i++;
                        String titre = snapshot.getKey();
                        String commentaire = snapshot.child("commentaire").getValue(String.class);
                        float rate = snapshot.child("rate").getValue(float.class);
                        rate_total += rate;
                        commentaires.add(new Commentaire(titre, commentaire, rate));
                    }
                    table_restaurant = database.getReference("Restaurant");
                    if (rate_total > 0)
                        table_restaurant.child(nom_restaurant).child("rate").setValue(rate_total / i);
                    adapter = new CommetaireAdapter(getApplicationContext(), R.layout.structure_commentaire, commentaires);
                    listCommentaire.setAdapter(adapter);

                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

   /*     final DatabaseReference table = database.getReference("Restaurant");
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String titre = snapshot.getKey();
                        if (titre.equals(nom_restaurant)) {
                            String adress = snapshot.child("adresse").getValue(String.class);
                            adress_restaurant.setText(adress);
                            String image = snapshot.child("image").getValue(String.class);
                            Glide.with(DetailRestaurant.this).load(image).into(img_restaurant_detail);
                            String num = snapshot.child("user").getValue(String.class);
                            num_restaurant.setText(num);
                            String categorie = snapshot.child("type").getValue(String.class);
                            categorie_restaurant.setText(categorie);
                            String ouverture =snapshot.child("ouverture").getValue(String.class);
                            String fermeture =snapshot.child("type").getValue(String.class);
                            String horaire=ouverture+"-"+fermeture;
                            textViewHoraire.setText(horaire);
                            latitude=snapshot.child("latitude").getValue(Double.class);
                            longitude=snapshot.child("longitude").getValue(Double.class);

                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(nom_restaurant));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
//favorie
    @OnClick(R.id.favorise_restaurant)
    public  void OnClickFavorise(){
        if (imageViewFavorise.getTag().toString().trim().equals("1")){
            imageViewFavorise.setImageResource(R.drawable.ic_favorite);
            imageViewFavorise.setTag("2");
            //db_favorie.insert(nom_restaurant);
            api= ApiClient.getClient().create(RestaurantInterface.class);
            FavorieClient favorieClient=new FavorieClient(user,restaurantClient);
            Log.d("fff",favorieClient+"");
            Call<FavorieClient> favorieClientCall=api.postFavorie(favorieClient);
            favorieClientCall.enqueue(new Callback<FavorieClient>() {
                @Override
                public void onResponse(Call<FavorieClient> call, Response<FavorieClient> response) {
                    Log.d("yyyy",response.body()+"");
                }

                @Override
                public void onFailure(Call<FavorieClient> call, Throwable t) {
                }
            });
        }
        else{
            imageViewFavorise.setImageResource(R.drawable.ic_favorite_white);
            imageViewFavorise.setTag("1");
            //db_favorie.delete(nom_restaurant);
            api= ApiClient.getClient().create(RestaurantInterface.class);
           // FavorieClient favorieClient=new FavorieClient(user,restaurantClient);
            Log.d("fff",favorieClient+"");
            Call<FavorieClient> favorieClientCall=api.deleteFavorie(user.getId(),restaurantClient.getId());
            favorieClientCall.enqueue(new Callback<FavorieClient>() {
                @Override
                public void onResponse(Call<FavorieClient> call, Response<FavorieClient> response) {
                    Log.d("delete",response.body()+"");
                }

                @Override
                public void onFailure(Call<FavorieClient> call, Throwable t) {

                }
            });
        }
    }
////////////////
    @OnClick(R.id.btnAjouter_commentaire)
     void OnClickBtn(){
        Intent ajout = new Intent(this, AjoutCommentaire.class);
        ajout.putExtra("nom_restaurant",nom_restaurant);
        startActivity(ajout);
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
    @OnClick(R.id.visite_menu)
    void OnClickMenu(){
        Intent RestaurantActivity = new Intent(this, VisiteMenu.class);
        RestaurantActivity.putExtra("nom_restaurant",nom_restaurant);
        startActivity(RestaurantActivity);
    }
}