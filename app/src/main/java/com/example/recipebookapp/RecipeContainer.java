package com.example.recipebookapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeContainer {

    @SerializedName("hits")
    private List<OneRecipeContainer> recipeList;

    public List<OneRecipeContainer> getRecipeList(){
        return this.recipeList;
    }

    public void setRecipeList(List<OneRecipeContainer> recipeList){
        this.recipeList = recipeList;
    }
}
