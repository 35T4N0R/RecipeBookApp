package com.example.recipebookapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeContainer {

    @SerializedName("hits")
    private List<Recipe> recipeList;

    public List<Recipe> getRecipeList(){
        return this.recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList){
        this.recipeList = recipeList;
    }
}
