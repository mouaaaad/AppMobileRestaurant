package com.example.app_restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.app_restaurant.Model.Restaurant;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(Context context, int resource, ArrayList<Restaurant> restaurants) {
        super(context, resource,restaurants);
        this.restaurants = restaurants;
    }

    public  void update(ArrayList<Restaurant> restaurantSearch){
        restaurants=new ArrayList<Restaurant>();
        restaurants.addAll(restaurantSearch);
        //notifyDataSetChanged();
        int taille=restaurantSearch.size();
        String taillee=taille+"";
        Log.d("tesst:",taillee);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.structure_restaurant,parent,false);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.img_restaurant);
        Glide.with(convertView).load(restaurants.get(position).getImage()).into(imageView) ;
        //Log.d("test",restaurants.get(position).getImage());

        TextView textView=(TextView)convertView.findViewById(R.id.name_restaurant);
        textView.setText(restaurants.get(position).getNom());

        TextView textView1=(TextView)convertView.findViewById(R.id.categorie);
        textView1.setText(restaurants.get(position).getType());

        TextView textView2=(TextView)convertView.findViewById(R.id.adress_restaurant);
        textView2.setText(restaurants.get(position).getAdresse());

        RatingBar ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar_restaurant);
        ratingBar.setRating(restaurants.get(position).getRate());

        return convertView;
    }
}
