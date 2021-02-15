package com.example.app_restaurant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.Restaurant;
import com.example.app_restaurant.ModelClient.City;
import com.example.app_restaurant.ModelClient.Manager;
import com.example.app_restaurant.ModelClient.RestaurantCategory;
import com.example.app_restaurant.ModelClient.RestaurantClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.example.app_restaurant.ModelClient.UserClient;
import com.example.app_restaurant.ModelClient.UserInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.widget.AdapterView.OnItemSelectedListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRestaurantActivity extends AppCompatActivity implements OnItemSelectedListener {
    //localisation
    SharedPreferences preferences ;
    private FusedLocationProviderClient client;
    private int REQUEST_CODE =111;
    private  double latitude ;
    private  double longitude ;
    //
    EditText edtNom,edtAdresse,edtType , edtOuverture , edtFermeture,edtPhone;
    Button btnAjouter;
    Spinner spinner ;
    Spinner spinnerVille ;
    String categorie ;
    String ville;

    String image;
    // views for button
    private Button btnSelect, btnUpload;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    private  StorageReference mStorageRef;
    RestaurantInterface api;
    RestaurantInterface apiRestC;
    RestaurantInterface apiCity;
    UserInterface ApiUser;
    Manager manager_rest;
    RestaurantCategory restaurantCategory;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        spinner=(Spinner)findViewById(R.id.edtType);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        spinnerVille=(Spinner)findViewById(R.id.edtVille);
        ArrayAdapter<CharSequence> adapterVille=ArrayAdapter.createFromResource(this,R.array.villes, android.R.layout.simple_spinner_item);
        adapterVille.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(adapterVille);
        spinnerVille.setOnItemSelectedListener(this);
        //localisation
        //
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
        //
        //
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(
                Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
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
        intent.setType("image/*");
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
            this.image=UUID.randomUUID().toString();
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + this.image);
                // this.image=UUID.randomUUID().toString();

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast
                            .makeText(AddRestaurantActivity.this,
                                    "Image Uploaded!!",
                                    Toast.LENGTH_SHORT)
                            .show();

                       ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               image=uri.toString();
                           }
                       });
                }
            });

       /*     ref.putFile(filePath)
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
                                            .makeText(AddRestaurantActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    image=taskSnapshot.
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddRestaurantActivity.this,
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
                            });*/
        }

        edtNom = (EditText)findViewById(R.id.edtNom);
        btnAjouter = (Button)findViewById(R.id.btnAjouter);
        edtOuverture=(EditText)findViewById(R.id.editOuverture) ;
        edtFermeture=(EditText)findViewById(R.id.editFermeture);
        edtPhone=(EditText)findViewById(R.id.editPhone);
        Intent intent=getIntent();
        //String user=intent.getStringExtra("client");
        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);
        String user=intent.getStringExtra("user");
        ApiUser=  ApiClient.getClient().create(UserInterface.class);
        Call<Manager> call= ApiUser.getManagerByemail(sharedPreferences.getString("Login","null"));
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                manager_rest=response.body();
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {

            }
        });
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference table_restaurant = database.getReference("Restaurant");
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiRestC=ApiClient.getClient().create(RestaurantInterface.class);
                Call<RestaurantCategory> categoryCall=apiRestC.getCategotie(categorie);
                categoryCall.enqueue(new Callback<RestaurantCategory>() {
                    @Override
                    public void onResponse(Call<RestaurantCategory> call, Response<RestaurantCategory> response) {
                       restaurantCategory=response.body();

                        apiCity=ApiClient.getClient().create(RestaurantInterface.class);
                        Log.d("ville",ville);
                        Call<City> cityCall=apiCity.getCity(ville);
                        cityCall.enqueue(new Callback<City>() {
                            @Override
                            public void onResponse(Call<City> call, Response<City> response) {
                                City city=response.body();
                                api= ApiClient.getClient().create(RestaurantInterface.class);
                                Date dt = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String check = dateFormat.format(dt);
                                Log.d("datte",check);
                                RestaurantClient restaurantClient =new RestaurantClient(edtNom.getText().toString(),image,edtPhone.getText().toString(),latitude,longitude,city,edtOuverture.getText().toString(),edtFermeture.getText().toString(),restaurantCategory,check,manager_rest);
                                Call<RestaurantClient> restaurantClientCall=api.postRestaurant(restaurantClient);
                                restaurantClientCall.enqueue(new Callback<RestaurantClient>() {
                                    @Override
                                    public void onResponse(Call<RestaurantClient> call, Response<RestaurantClient> response) {
                                        Log.d("yyyy",response.body()+"");
                                    }

                                    @Override
                                    public void onFailure(Call<RestaurantClient> call, Throwable t) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<City> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<RestaurantCategory> call, Throwable t) {
                    }
                });



             /*  table_restaurant.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtNom.getText().toString()).exists()){
                            progressDialog.dismiss();
                            Toast.makeText(AddRestaurantActivity.this,"Restaurant already exists!",Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(AddRestaurantActivity.this, list_restaurant_total_Activity.class);
                            home.putExtra("user",user);
                            startActivity(home);
                        }
                        else {
                            progressDialog.dismiss();
                            Date date =new Date();
                            Restaurant restaurant = new Restaurant(edtAdresse.getText().toString(),categorie,user,image,0,date,edtOuverture.getText().toString(),edtFermeture.getText().toString(),latitude,longitude);

                            table_restaurant.child(edtNom.getText().toString()).setValue(restaurant);
                            Toast.makeText(AddRestaurantActivity.this," Restaurant Ajouter! " ,Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(AddRestaurantActivity.this, list_restaurant_total_Activity.class);
                            home.putExtra("user",user);
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

    //localisation
    //
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("TAG", "getCurrentLocation: ");
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Log.d("tttttt","tttttt");
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();
                }
                else {Log.d("hhhhh","hhhhhhh");}
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }else {
            Toast.makeText(this,"refus",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch(parent.getId()) {
            case R.id.edtType: {
                categorie = parent.getItemAtPosition(position).toString();
            }
            case R.id.edtVille: {
                ville = parent.getItemAtPosition(position).toString();
            }

        }
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