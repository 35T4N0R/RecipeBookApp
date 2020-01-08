package com.example.recipebookapp;


import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName="recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int recipeId;

    @SerializedName("label")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("url")
    private String sourceUrl;

    @SerializedName("ingredientLines")
    private ArrayList<String> ingredients;

    private Bitmap imageBitmap;

    public Recipe(String title, String imageUrl, String sourceUrl, ArrayList<String> ingredients){
        this.title = title;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
        this.imageBitmap = null;
    }

    public int getRecipeId(){
        return this.recipeId;
    }

    public String getTitle(){
        return this.title;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public String getSourceUrl(){
        return this.sourceUrl;
    }

    public ArrayList<String> getIngredients(){
        return this.ingredients;
    }

    public Bitmap getImageBitmap(){
        return this.imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap){
        this.imageBitmap = imageBitmap;
    }

    public void setRecipeId(int recipeId){
        this.recipeId = recipeId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setSourceUrl(String sourceUrl){
        this.sourceUrl = sourceUrl;
    }

    public void setIngredients(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }
}
