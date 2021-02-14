package com.example.app_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.hp.myapplication.Common.Common;
import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.User;
import com.example.app_restaurant.ModelClient.UserClient;
import com.example.app_restaurant.ModelClient.UserInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnSignIn;
    SharedPreferences sharedPreferences;

    @BindView(R.id.checkBox)
    CheckBox checkBox;
    UserInterface api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        edtEmail = (MaterialEditText)findViewById(R.id.edtEmail);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setMessage("Please wait!");
                progressDialog.show();
                api= ApiClient.getClient().create(UserInterface.class);
                Call<UserClient> call= api.getUserByemail(edtEmail.getText().toString());
                call.enqueue(new Callback<UserClient>() {
                    @Override
                    public void onResponse(Call<UserClient> call, Response<UserClient> response) {
                        UserClient user=response.body();
                        if(user.getPassword().equals(edtPassword.getText().toString())){
                            progressDialog.dismiss();
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("Login",edtEmail.getText().toString());
                            editor.apply();
                            if(checkBox.isChecked()) {
                                sharedPreferences.edit().putString("username", edtEmail.getText().toString()).commit();
                                sharedPreferences.edit().putString("password", edtPassword.getText().toString()).commit();
                                sharedPreferences.edit().putString("type", user.getType()).commit();
                            }
                            if(user.getType().equals("Client")) {
                                Intent home = new Intent(SignIn.this, HomeClient.class);
                                startActivity(home);
                            }
                            else {
                                Intent home = new Intent(SignIn.this, list_restaurant_total_Activity.class);
                                home.putExtra("user",user.getEmail());
                                startActivity(home);
                            }
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this, "Password incorrect..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserClient> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(SignIn.this,"email incorrect!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}