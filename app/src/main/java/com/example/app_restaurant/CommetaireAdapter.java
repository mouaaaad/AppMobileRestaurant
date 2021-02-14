package com.example.app_restaurant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.app_restaurant.Model.Commentaire;


import java.util.ArrayList;

public class CommetaireAdapter  extends ArrayAdapter<Commentaire> {
    private ArrayList<Commentaire> commentaires;

    public CommetaireAdapter(Context context, int resource, ArrayList<Commentaire> commentaires) {
        super(context, resource,commentaires);
        this.commentaires = commentaires;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.structure_commentaire,parent,false);

        //ImageView imageView=(ImageView)convertView.findViewById(R.id.img_restaurant);
        //imageView.setBackgroundResource(restaurants.get(position).getImage());

       TextView textView=(TextView)convertView.findViewById(R.id.commentaire_view);
        textView.setText(commentaires.get(position).getCommentaire());

        TextView textView1=(TextView)convertView.findViewById(R.id.titre_comentaire_view);
        textView1.setText(commentaires.get(position).getTitre());

        RatingBar ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar_commentaire);
        ratingBar.setRating(commentaires.get(position).getRate());

        return convertView;
    }
}
