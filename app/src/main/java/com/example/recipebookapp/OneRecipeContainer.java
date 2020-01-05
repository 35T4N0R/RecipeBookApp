package com.example.recipebookapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OneRecipeContainer {

        @SerializedName("recipe")
        private Recipe oneRecipe;

        public Recipe getOneRecipe(){
            return this.oneRecipe;
        }

        public void setOneRecipe(Recipe oneRecipe){
            this.oneRecipe = oneRecipe;
        }
}
