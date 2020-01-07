package com.example.recipebookapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);

    @Update
    public void update(Recipe recipe);

    @Delete
    public void delete(Recipe recipe);

    @Query("DELETE FROM recipe")
    public void deleteAll();

    @Query("SELECT * FROM recipe ORDER BY title")
    public LiveData<List<Recipe>> findAll();

    @Query("SELECT * FROM recipe WHERE recipeId LIKE :recipeId")
    public LiveData<Recipe> findRecipeWithId(int recipeId);

    @Query("SELECT * FROM recipe LIMIT 1")
    public LiveData<Recipe[]> isEmpty();
}

