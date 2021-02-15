package com.example.app_restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.User;
import com.example.app_restaurant.ModelClient.Client;
import com.example.app_restaurant.ModelClient.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagePersonnelModification extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SharedPreferences preferences ;
    SharedPreferences sharedPreferences;
    DatabaseReference table_user ;
    String email ;
    Spinner spinner ;
    String ville ;
    @BindView(R.id.nom_modification)
    TextView textViewNom ;
    @BindView(R.id.email_modification)
    TextView textViewEmail ;
    @BindView(R.id.adress_modification)
    TextView textViewAdress ;
    @BindView(R.id.tele_modification)
    TextView textViewTele ;
    Client client ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_personnel_modification);
        ButterKnife.bind(this);

        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);
        email =sharedPreferences.getString("Login",null);
        spinner=(Spinner)findViewById(R.id.ville);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.villes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

      /*  FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewNom.setText(dataSnapshot.child(phone).child("name").getValue(String.class));
                if(dataSnapshot.child(phone).child("detail").exists()&&!dataSnapshot.child(phone).child("detail").getValue(String.class).equals("")){
                textViewDetail.setText(dataSnapshot.child(phone).child("detail").getValue(String.class));
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        UserInterface apiClient = ApiClient.getClient().create(UserInterface.class);
        Call<Client> clientCall= apiClient.getClientByemail(email);
        clientCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                client=response.body();
                textViewNom.setText(client.getUsername());

                if(client.getEmail()!=null||!client.getEmail().equals(""))
                    textViewEmail.setText(client.getEmail());
                if(client.getAddress()!=null||!client.getAddress().equals(""))
                    textViewAdress.setText(client.getAddress());
                if(client.getTel()!=null||!client.getTel().equals(""))
                    textViewTele.setText(client.getTel());
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        ville=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @OnClick(R.id.btnSave)
    public void modification(){
        client.setUsername(textViewNom.getText().toString());
        client.setCity(ville);
        if(!textViewEmail.getText().toString().equals(""))
            client.setEmail(textViewEmail.getText().toString());
        if(!textViewAdress.getText().toString().equals(""))
            client.setAddress(textViewAdress.getText().toString());
        if(!textViewTele.getText().toString().equals(""))
            client.setTel(textViewTele.getText().toString());
        UserInterface apiClientUpdate = ApiClient.getClient().create(UserInterface.class);
        Call<Client> clientCall= apiClientUpdate.update(client);
        clientCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });
        /*table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(phone).exists()){
                    table_user.child(phone).child("name").setValue(textViewNom.getText().toString());
                    table_user.child(phone).child("detail").setValue(textViewDetail.getText().toString());
                    if(!ville.equals("ville"))
                        table_user.child(phone).child("ville").setValue(ville);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        Intent modifie = new Intent(this, PagePersonnel.class);
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