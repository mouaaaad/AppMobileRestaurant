package com.example.app_restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.Restaurant;
import com.example.app_restaurant.Model.menu;
import com.example.app_restaurant.ModelClient.Meal;
import com.example.app_restaurant.ModelClient.RestaurantClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    SharedPreferences preferences ;
    EditText edtplat,edtPrix,edtDetail;
    Button btnAjouterBtn;
    String image;
    // views for button
    private Button btnChoosemimg, btnUploadmimg;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private  StorageReference mStorageRef;
    RestaurantInterface apiMenu;
    RestaurantInterface apiRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(
                Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        // initialise views
        btnChoosemimg = findViewById(R.id.btnChoosemimg);
        btnUploadmimg = findViewById(R.id.btnUploadmimg);
        imageView = findViewById(R.id.imageView);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnChoosemimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        btnUploadmimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadImage();
            }
        });
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/plats/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/plats/"
                                    + UUID.randomUUID().toString());
            this.image=UUID.randomUUID().toString();

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MenuActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            image=uri.toString(); }
                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MenuActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }

        edtplat = (EditText)findViewById(R.id.nom_plat);
        edtPrix = (EditText)findViewById(R.id.prix);
        edtDetail = (EditText)findViewById(R.id.detail);
        btnAjouterBtn = (Button)findViewById(R.id.ajouter_plat);
        Intent intent=getIntent();
        String restaurant=intent.getStringExtra("restaurant");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_menu = database.getReference("Menu");
        btnAjouterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(MenuActivity.this);
                progressDialog.setMessage("Please wait..!");
                progressDialog.show();

                apiRestaurant= ApiClient.getClient().create(RestaurantInterface.class);
                Call<RestaurantClient> restaurantClientCall=apiRestaurant.getRestaurantByName(restaurant);
                restaurantClientCall.enqueue(new Callback<RestaurantClient>() {
                    @Override
                    public void onResponse(Call<RestaurantClient> call, Response<RestaurantClient> response) {
                        RestaurantClient restaurantClient =response.body();
                        apiMenu= ApiClient.getClient().create(RestaurantInterface.class);
                        Meal meal =new Meal(edtplat.getText().toString(),Double.parseDouble(edtPrix.getText().toString()),edtDetail.getText().toString(),image,restaurantClient,null);
                        Call<Meal>mealCall =apiMenu.postMeal(meal);
                        mealCall.enqueue(new Callback<Meal>() {
                            @Override
                            public void onResponse(Call<Meal> call, Response<Meal> response) {
                                Toast.makeText(MenuActivity.this, "Ajout Par succés", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(MenuActivity.this, ListeMenuActivity.class);
                                home.putExtra("restaurant",restaurant);
                                startActivity(home);
                            }

                            @Override
                            public void onFailure(Call<Meal> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<RestaurantClient> call, Throwable t) {

                    }
                });


                /*table_menu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtplat.getText().toString()).exists()){
                            progressDialog.dismiss();
                            Toast.makeText(MenuActivity.this,"Restaurant already exists!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                           /* progressDialog.dismiss();
                            menu menu = new menu(edtplat.getText().toString(),edtPrix.getText().toString(),image,edtDetail.getText().toString(),restaurant);
                            table_menu.child(edtplat.getText().toString()).setValue(menu);
                            Toast.makeText(MenuActivity.this," Menu Ajouter! " ,Toast.LENGTH_SHORT).show();
                            apiMenu= ApiClient.getClient().create(RestaurantInterface.class);
                            Meal meal =new Meal();
                            Call<Meal> restaurantClientCall=apiMenu.postMeal(meal);
                            Intent home = new Intent(MenuActivity.this, ListeMenuActivity.class);
                            home.putExtra("restaurant",restaurant);

                            startActivity(home);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
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
