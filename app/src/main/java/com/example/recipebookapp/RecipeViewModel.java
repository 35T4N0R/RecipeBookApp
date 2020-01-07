package com.example.recipebookapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;

    private LiveData<List<Recipe>> recipes;
    private LiveData<Recipe[]> isEmpty;

    public RecipeViewModel(@NonNull Application application){
        super(application);
        recipeRepository = new RecipeRepository(application);
        recipes = recipeRepository.findAllRecipes();
        isEmpty = recipeRepository.isEmpty();
    }

    public LiveData<List<Recipe>> findAll(){
        return recipes;
    }

    public LiveData<Recipe[]> isEmpty(){
        return isEmpty;
    }

    public LiveData<Recipe> findRecipeWithId(int recipeId){
        return recipeRepository.findRecipeWithId(recipeId);
    }

    public void insert(Recipe recipe){
        recipeRepository.insert(recipe);
    }

    public void update(Recipe recipe){
        recipeRepository.update(recipe);
    }

    public void delete(Recipe recipe){
        recipeRepository.delete(recipe);
    }
}
