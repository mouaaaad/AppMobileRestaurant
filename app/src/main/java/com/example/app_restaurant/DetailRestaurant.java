package com.example.app_restaurant;

import com.example.app_restaurant.dataBase.data;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailRestaurant extends FragmentActivity implements OnMapReadyCallback {
    SharedPreferences preferences ;
    data db_favorie;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        ButterKnife.bind(this);
        db_favorie = new data(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        nom_restaurant = extras.getString("nom_restaurant");
        textViewNom.setText(nom_restaurant);

        sharedPreferences = getSharedPreferences("myRef", MODE_PRIVATE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_favorie = database.getReference("Favorie");
        login = sharedPreferences.getString("Login", null);

        String[] whereArgs = new String[]{
                String.valueOf(nom_restaurant)};

        SQLiteDatabase data = db_favorie.getReadableDatabase();
        //Cursor cursor =data.query("Restaurant",null,null,null,null,null,null);
        Cursor cursor = data.query("List_Favorie", null, "Restaurant_name=?", whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            imageViewFavorise.setImageResource(R.drawable.ic_favorite);
            imageViewFavorise.setTag("2");
        }


        final DatabaseReference table_commentaire = database.getReference("Commentaire");

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
        });

        final DatabaseReference table = database.getReference("Restaurant");
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
        });
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

    @OnClick(R.id.favorise_restaurant)
    public  void OnClickFavorise(){
        if (imageViewFavorise.getTag().toString().trim().equals("1")){
            imageViewFavorise.setImageResource(R.drawable.ic_favorite);
            imageViewFavorise.setTag("2");
            db_favorie.insert(nom_restaurant);
        }
        else{
            imageViewFavorise.setImageResource(R.drawable.ic_favorite_white);
            imageViewFavorise.setTag("1");
            db_favorie.delete(nom_restaurant);
        }

    }
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
}