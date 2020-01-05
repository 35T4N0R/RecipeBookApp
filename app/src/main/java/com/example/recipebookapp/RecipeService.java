package com.example.recipebookapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    //app_id = "5c4d239d"
    //app_key = "fbfcc1588959c5c95444569037fcf6c7"

    @GET("search")
    Call<RecipeContainer> findRecipes(@Query("q") String q,
                                      @Query("app_id") String app_id,
                                      @Query("app_key") String app_key,
                                      @Query("from") int from,
                                      @Query("to") int to);
}
