package com.example.recipebookapp;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName="recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("label")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("url")
    private String sourceUrl;

    @SerializedName("ingredientLines")
    private List<String> ingredients;

    public String getTitle(){
        return this.title;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public String getSourceUrl(){
        return this.sourceUrl;
    }

    public List<String> getIngredients(){
        return this.ingredients;
    }
}
