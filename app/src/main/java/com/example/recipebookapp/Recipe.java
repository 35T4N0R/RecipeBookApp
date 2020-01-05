package com.example.recipebookapp;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

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
    private List<String> ingredients;

    public Recipe(String title, String imageUrl, String sourceUrl, List<String> ingredients){
        this.title = title;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients;
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

    public List<String> getIngredients(){
        return this.ingredients;
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

    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }
}
