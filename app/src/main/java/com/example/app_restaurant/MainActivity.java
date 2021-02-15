package com.example.app_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView slogan;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences("myRef",MODE_PRIVATE);
            if (sharedPreferences.getString("username", null) != null && sharedPreferences.getString("password", null) != null)
                            {
                                if(sharedPreferences.getString("type", "null").equals("Client")) {
                                    Intent home = new Intent(this, HomeClient.class);
                                    startActivity(home);
                                }
                                else if(sharedPreferences.getString("type", "null").equals("Manager")) {
                                    Intent home = new Intent(this, list_restaurant_total_Activity.class);
                                    home.putExtra("user",sharedPreferences.getString("username", null));
                                    startActivity(home);
                                }
                            }
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        slogan = (TextView)findViewById(R.id.slogan);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignIn.class);
                startActivity(i);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(SignUp);
            }
        });
    }

}