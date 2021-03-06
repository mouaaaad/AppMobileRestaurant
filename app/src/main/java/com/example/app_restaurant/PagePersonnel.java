package com.example.app_restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.ModelClient.Client;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.example.app_restaurant.ModelClient.Review;
import com.example.app_restaurant.ModelClient.UserInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagePersonnel extends AppCompatActivity {
    SharedPreferences preferences ;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    @BindView(R.id.mon_client)
    TextView textViewNom ;
    @BindView(R.id.phone_client)
    TextView textViewPhone;
    @BindView(R.id.ville_client)
    TextView textViewVille ;
    @BindView(R.id.adress_client)
    TextView textViewAdress ;
    @BindView(R.id.email_client)
    TextView textViewEmail ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_personnel);
        ButterKnife.bind(this);

        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.pagePersonnel);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorises:
                        startActivity(new Intent(getApplicationContext(),Favorises.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.pagePersonnel:
                        return true ;

                    case R.id.actualite:
                        startActivity(new Intent(getApplicationContext(),Actualite.class));
                        overridePendingTransition(0,0);
                        return true ;

                    case R.id.homeClient:
                        startActivity(new Intent(getApplicationContext(),HomeClient.class));
                        overridePendingTransition(0,0);
                        return true ;
                }
                return false;
            }
        });

        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
       // final DatabaseReference table_user = database.getReference("User");
        String email =sharedPreferences.getString("Login",null);
        UserInterface apiClient = ApiClient.getClient().create(UserInterface.class);
        Call<Client> clientCall= apiClient.getClientByemail(email);
        clientCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Client client=response.body();
               textViewNom.setText("Bienvenue "+client.getUsername());
               if(client.getEmail()!=null&&!client.getEmail().equals(""))
                   textViewEmail.setText(client.getEmail());
               if(client.getAddress()!=null&&!client.getAddress().equals(""))
                   textViewAdress.setText(client.getAddress());
               if(client.getCity()!=null&&!client.getCity().equals(""))
                   textViewVille.setText(client.getCity());
               if(client.getTel()!=null&&!client.getTel().equals(""))
                   textViewPhone.setText(client.getTel());
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });


        /*table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null && dataSnapshot.exists()) {

                        textViewPhone.setText(phone);
                        textViewNom.setText("Bienvenue "+ dataSnapshot.child(phone).child("name").getValue(String.class));
                        if(dataSnapshot.child(phone).child("ville").exists()&&!dataSnapshot.child(phone).child("ville").getValue(String.class).equals("")){
                            textViewVille.setText(dataSnapshot.child(phone).child("ville").getValue(String.class));}
                        if(dataSnapshot.child(phone).child("detail").exists()&&!dataSnapshot.child(phone).child("detail").getValue(String.class).equals("")){
                             textViewDetail.setText(dataSnapshot.child(phone).child("detail").getValue(String.class));
                        }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });*/


    }
    @OnClick(R.id.btn_modifier)
    public void modifie(){
        Intent modifie = new Intent(this, PagePersonnelModification.class);
        startActivity(modifie);
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