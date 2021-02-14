package com.example.app_restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.app_restaurant.Model.menu;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<menu> {
    private ArrayList<menu> menus;

    public MenuAdapter(Context context, int resource, ArrayList<menu> menus) {
        super(context, resource,menus);
        this.menus = menus;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.activity_menu_structure,parent,false);

        ImageView imageView=(ImageView)convertView.findViewById(R.id.image_plat);
        Glide.with(convertView).load(menus.get(position).getPhoto()).into(imageView) ;
        //ImageView imageView=(ImageView)convertView.findViewById(R.id.image_plat);
        //imageView.setBackgroundResource(restaurants.get(position).getImage());

        TextView textView=(TextView)convertView.findViewById(R.id.nom_plat);
        textView.setText(menus.get(position).getNom());

        TextView textView1=(TextView)convertView.findViewById(R.id.prix_plat);
        textView1.setText(menus.get(position).getPrix());

        TextView textView2=(TextView)convertView.findViewById(R.id.detail);
        textView2.setText(menus.get(position).getDetail());

        return convertView;
    }

}
