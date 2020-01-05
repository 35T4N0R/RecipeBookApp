package com.example.recipebookapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> recipes;

    public RecipeRepository(Application application){
        RecipeDatabase database = RecipeDatabase.getDatabase(application);
        recipeDao = database.recipeDao();
        recipes = recipeDao.findAll();
    }

    LiveData<List<Recipe>> findAllRecipes(){
        return recipes;
    }

    void insert(Recipe recipe){
        RecipeDatabase.databaseWriteExecutor.execute(()->{
            recipeDao.insert(recipe);
        });
    }

    void update(Recipe recipe){
        RecipeDatabase.databaseWriteExecutor.execute(()->{
            recipeDao.update(recipe);
        });
    }

    void delete(Recipe recipe){
        RecipeDatabase.databaseWriteExecutor.execute(()->{
            recipeDao.delete(recipe);
        });
    }
}
