package com.example.recipebookapp;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
//@TypeConverters({IngredientsConverter.class})
public abstract class RecipeDatabase extends RoomDatabase{
    public abstract RecipeDao recipeDao();

    //private static String app_id = "5c4d239d";
    //private static String app_key = "fbfcc1588959c5c95444569037fcf6c7";
    //private static List<Recipe> finalRecipes;
    private static Recipe recipeTmp;

    private static volatile RecipeDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool((NUMBER_OF_THREADS));

    static RecipeDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized(RecipeDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),RecipeDatabase.class, "recipe_db8").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);



            databaseWriteExecutor.execute(()->{
                //RecipeDao dao = INSTANCE.recipeDao();
                RecipeDao dao = INSTANCE.recipeDao();



                //chicken 0-5
                //pasta 0-5
                //pork 0-5
                //rice 60-65
                //beef 44-49
                //fish 27-32





                //Recipe r = new Recipe("test",null,null);
                //dao.insert(r);
                //fetchRecipesData("chicken",0,1);
                //dao.insert(recipeTmp);
                //fetchRecipesData("pasta",0,5);
                //fetchRecipesData("pork",0,5);
                //fetchRecipesData("rice",60,65);
                //fetchRecipesData("beef",44,49);
                //fetchRecipesData("fish",27,32);
                /*for(int i = 0;i<finalRecipes.size();i++){
                    //Log.d("MainActivity","elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo elo");
                    dao.insert(finalRecipes.get(i));
                }*/


                //Recipe recipe = new Recipe("test",null,null,null);
                //dao.insert(recipe);
            });
        }
    };

    /*private static void fetchRecipesData(String q, int from, int to){
        RecipeService recipeService = RetrofitInstance.getRetrofitInstance().create(RecipeService.class);

        Call<RecipeContainer> recipeApiCall = recipeService.findRecipes(q,app_id,app_key,from,to);



        //Ingredients i = new Ingredients(Arrays.asList(new String[]{"foo", "bar"}));



        recipeApiCall.enqueue(new retrofit2.Callback<RecipeContainer>(){
            @Override
            public void onResponse(Call<RecipeContainer> call, Response<RecipeContainer> response){
                for(int i = 0; i < response.body().getRecipeList().size();i++){
                    //Log.d("MainActivity","elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo elo");
                    //finalRecipes.add(response.body().getRecipeList().get(i));
                    System.out.println(response.body().getRecipeList().get(i).getOneRecipe().getTitle() + "elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo ");
                    recipeTmp = new Recipe(response.body().getRecipeList().get(i).getOneRecipe().getTitle(),response.body().getRecipeList().get(i).getOneRecipe().getImageUrl(),response.body().getRecipeList().get(i).getOneRecipe().getSourceUrl());


                    System.out.println("elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo eoelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo eloelo elo elo elo elo leo elo");
                }
            }
            @Override
            public void onFailure(Call<RecipeContainer> call, Throwable t){
                System.out.println(t.getMessage()+ "nie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie inenie nie nie ine");

                //Snackbar.make(findViewById(R.id.coordinator_layout),"Something went wrong... Please try again later", Snackbar.LENGTH_LONG).show();
            }
        });

    }*/

}
