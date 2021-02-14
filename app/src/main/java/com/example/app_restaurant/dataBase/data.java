package com.example.app_restaurant.dataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import  android.util.Log;

import com.example.app_restaurant.R;
public class data extends SQLiteOpenHelper {
    private static  final String DATABASE_NAME="Favorie.db";
    private static  final int  DATABASE_VERSION=1;
    private static  final String TABLE_NAME="List_Favorie";
    private static  final String ID="id";
    private static  final String Restaurant_name="Restaurant_name";




    public data(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE "+ TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT ,Restaurant_name Text ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME );
        onCreate(db);
    }



    public  void insert (String nom){
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(Restaurant_name,nom);
        db.insert(TABLE_NAME,null,values);
    }

   public  void delete (String nom){
        SQLiteDatabase db =getWritableDatabase();
       String[] whereArgs = new String[] {
               String.valueOf(nom)};
        db.delete(TABLE_NAME,"Restaurant_name = ?",whereArgs);
    }

}
