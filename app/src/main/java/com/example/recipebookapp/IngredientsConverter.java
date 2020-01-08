package com.example.recipebookapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientsConverter {


    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayLisr(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static Bitmap fromStringToBitmap(String myImageData) {

        if(myImageData != null){
            byte[] imageAsBytes = android.util.Base64.decode(myImageData.getBytes(), android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }else{
            return null;
        }

    }
    @TypeConverter
    public static String fromBitmapToString(Bitmap imageBitmap) {

        if(imageBitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return android.util.Base64.encodeToString(b,android.util.Base64.DEFAULT);
        }else{
            return null;
        }

    }
}
