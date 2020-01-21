package com.example.recipebookapp;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class RecipeDatabase extends RoomDatabase{
    public abstract RecipeDao recipeDao();

    private static volatile RecipeDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 2;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool((NUMBER_OF_THREADS));

    static RecipeDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized(RecipeDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),RecipeDatabase.class, "recipe_db3").addCallback(sRoomDatabaseCallback).build();
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
                //chicken 0-5
                //pasta 0-5
                //pork 0-5
                //rice 60-65
                //beef 44-49
                //fish 27-32
        });
        }
    };
}
