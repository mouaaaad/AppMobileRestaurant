package com.example.app_restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_restaurant.Model.Commentaire;
import com.example.app_restaurant.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AjoutCommentaire extends AppCompatActivity {
    SharedPreferences preferences ;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.commentaire)
    TextView textViewCommentaire ;
    @BindView(R.id.titre_comentaire)
    TextView textViewTitre ;
    float rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_commentaire);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btnEnvoyer)
    void OnClickBtnEnvoyer(){
        Intent intent= getIntent();
        Bundle value = intent.getExtras();
        String nom_restaurant =value.getString("nom_restaurant");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_commentaire = database.getReference("Commentaire");
        table_commentaire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Commentaire comm =new Commentaire(textViewCommentaire.getText().toString(),ratingBar.getRating());
                table_commentaire.child(nom_restaurant).child(textViewTitre.getText().toString()).setValue(comm);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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