package com.example.app_restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.app_restaurant.ApiClient.ApiClient;
import com.example.app_restaurant.Model.Restaurant;
import com.example.app_restaurant.Model.menu;
import com.example.app_restaurant.ModelClient.Meal;
import com.example.app_restaurant.ModelClient.RestaurantClient;
import com.example.app_restaurant.ModelClient.RestaurantInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListeMenuActivity extends AppCompatActivity {
    //@BindView(R.id.list_menu_total)
    //ListView listMenuTotal ;
    SharedPreferences preferences ;
    ArrayList<menu> menus;
    MenuAdapter adapter ;
    private StorageReference mStorageRef;
    String res;
    SwipeMenuListView listMenuTotal;
    SwipeMenuCreator creator;
    RestaurantInterface apiMenu;
    RestaurantInterface apiDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_menu);
        listMenuTotal=(SwipeMenuListView)findViewById(R.id.list_menu_total);
        ButterKnife.bind(this);
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                openItem.setWidth((90));
                // set item title

                // set item title fontsize
                openItem.setTitleSize(18);
                openItem.setIcon(R.drawable.ic_delete_foreground);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_update_foreground);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listMenuTotal.setMenuCreator(creator);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_menu = database.getReference("Menu");
        mStorageRef= FirebaseStorage.getInstance().getReference();
        Bundle extras=getIntent().getExtras();
         res=new String(extras.getString("restaurant"));
         Log.d("nom_rest",res);
        menus =new ArrayList<menu>();
        apiMenu= ApiClient.getClient().create(RestaurantInterface.class);
        Call<List<Meal>> call= apiMenu.getMeal(res);
        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                List<Meal> meals =response.body();
                for(Meal meal : meals){
                    menus.add(new menu(meal.getMeal(),meal.getPrice()+"Dh","photo",meal.getDetail(),meal.getRestaurant().getName()));
                }
                adapter=new MenuAdapter(getApplicationContext(), R.layout.activity_liste_menu,menus);
                listMenuTotal.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {

            }
        });


     /*   table_menu.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null && dataSnapshot.exists()) {


                    //progressDialog.dismiss();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        /*String restaurant1 = snapshot.child("restaurant").getValue(String.class);
                        //Log.d("restaurant1",restaurant1);
                        if(restaurant1==null)
                        {
                            restaurant1=" ";
                        }
                        if( snapshot.child("restaurant").getValue(String.class).equals(res))
                        {
                            String nom =snapshot.getKey();
                            String prix = snapshot.child("prix").getValue(String.class)+"Dh";
                          // Log.d("restaurant2",snapshot.child("restaurant").getValue(String.class));
                            String detail = snapshot.child("detail").getValue(String.class);
                            String restaurant = snapshot.child("restaurant").getValue(String.class);

                            String photo = snapshot.child("photo").getValue(String.class);
                            menus.add(new menu(nom,prix,photo,detail,restaurant));
                        }

                    }
                    String size =menus.size()+"";
                    Log.d("size 2",size);
                    adapter=new MenuAdapter(getApplicationContext(), R.layout.activity_liste_menu,menus);
                    listMenuTotal.setAdapter(adapter);

                }
                else{
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("test","non");
            }  });*/
        listMenuTotal.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override

            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        menu Menu ;
                        Menu=menus.get(position);
                        //FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //DatabaseReference table_res = database.getReference("Menu");
                       // table_res.child(Menu.getNom()).removeValue();
                        apiDelete= ApiClient.getClient().create(RestaurantInterface.class);
                        Call<Void> voidCall= apiDelete.deleteMeal(Menu.getNom());
                        voidCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        menus.clear();
                        //restaurants.remove(restaurant);
                        adapter=new MenuAdapter(getApplicationContext(),R.layout.activity_menu_structure,menus);
                        listMenuTotal.setAdapter(adapter);
                        break;

                }

                return false;
            }
        });
        }
    @OnClick(R.id.nouveau_plat)
    public void onClick(){

        Intent home = new Intent(this, MenuActivity.class);
        home.putExtra("restaurant",res);
        startActivity(home);
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