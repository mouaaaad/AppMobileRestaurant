package com.example.app_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.User;
import com.example.app_restaurant.ModelClient.RestaurantClient;
//import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.example.app_restaurant.ModelClient.UserClient;
import com.example.app_restaurant.ModelClient.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements OnItemSelectedListener{

    MaterialEditText edtName,edtEmail,edtPassword;
    Button btnSignUp;
    Spinner spinner ;
    String type ;
    UserInterface api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        spinner=(Spinner)findViewById(R.id.type);
        edtName = (MaterialEditText)findViewById(R.id.Name);
        edtEmail = (MaterialEditText)findViewById(R.id.edtEmail);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Please wait..!");
                progressDialog.show();
                api= ApiClient.getClient().create(UserInterface.class);
                Call<UserClient> call= api.getUserByemail(edtEmail.getText().toString());
                call.enqueue(new Callback<UserClient>() {
                    @Override
                    public void onResponse(Call<UserClient> call, Response<UserClient> response) {
                        UserClient user=response.body();
                        if(user.getEmail().equals(edtEmail.getText().toString())) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this,"email existe deja!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserClient> call, Throwable t) {
                        Date dt = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String check = dateFormat.format(dt);
                        UserClient userClient =new UserClient(edtName.getText().toString(),edtEmail.getText().toString(),edtPassword.getText().toString(),null,type,check);
                        if(type.equals("Client")){
                            Call<UserClient> userPostCall=api.postClients(userClient);
                            userPostCall.enqueue(new Callback<UserClient>() {
                                @Override
                                public void onResponse(Call<UserClient> call, Response<UserClient> response) {
                                    progressDialog.dismiss();
                                    if(response.body()==null)
                                        Toast.makeText(SignUp.this,"username existe deja!",Toast.LENGTH_SHORT).show();
                                    else{
                                    Toast.makeText(SignUp.this,"insertion par succés",Toast.LENGTH_SHORT).show();
                                        Intent signIn = new Intent(SignUp.this, SignIn.class);
                                        startActivity(signIn);
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserClient> call, Throwable t) {

                                }
                            });
                        }
                        else {
                            Call<UserClient> userPostCall=api.postManagers(userClient);
                            userPostCall.enqueue(new Callback<UserClient>() {
                                @Override
                                public void onResponse(Call<UserClient> call, Response<UserClient> response) {
                                    progressDialog.dismiss();
                                    if(response.body()==null)
                                        Toast.makeText(SignUp.this,"username existe deja!",Toast.LENGTH_SHORT).show();
                                    else{
                                        Toast.makeText(SignUp.this,"insertion par succés",Toast.LENGTH_SHORT).show();
                                        Intent signIn = new Intent(SignUp.this, SignIn.class);
                                        startActivity(signIn);
                                    }

                                }
                                @Override
                                public void onFailure(Call<UserClient> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        type=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}