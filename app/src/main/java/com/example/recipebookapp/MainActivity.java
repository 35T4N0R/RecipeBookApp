package com.example.recipebookapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {



    private RecipeViewModel recipeViewModel;

    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private SensorManager mSensorMgr;

    private Sensor accelerometer;

    private Sensor light;
    private int brightness;
    private float maxValue;

    private Random rnd;
    private int recipesCount;
    private ArrayList<Integer> recipesId = new ArrayList<Integer>();


    public static final int NEW_RECIPE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_RECIPE_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_EDIT_RECIPE_TITLE = "bookTitle";
    public static final String EXTRA_RECIPE_ID = "recipeId";
    private Recipe editedRecipe;
    Handler handler = new Handler();

    private static String app_id = "5c4d239d";
    private static String app_key = "fbfcc1588959c5c95444569037fcf6c7";


    private final SensorEventListener accelSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double)(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2)));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(mAccel > 12){
                rnd = new Random();
                int id = rnd.nextInt(recipesId.size());
                Intent intent = new Intent(MainActivity.this,RecipeDetailsActivity.class);
                intent.putExtra("recipeId",recipesId.get(id));
                startActivity(intent);
                //Snackbar.make(findViewById(R.id.coordinator_layout),"Shake, shake, shake", Snackbar.LENGTH_LONG).show();

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private final SensorEventListener lightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float value = event.values[0];
            CoordinatorLayout root = findViewById(R.id.coordinator_layout);
            TextView txt = findViewById(R.id.sensor);
            txt.setText(value+"");
            /*handler.postDelayed(new Runnable() {
                public void run() {
                    root.setBackgroundColor(getResources().getColor(R.color.light_green));
                }
            }, 1000);

            root.setBackgroundColor(Color.WHITE);*/
            value /= 5;
            int newValue = (int) (255f * (value + 10)/ 30);
            if(newValue >= 255) newValue = 255;
            //root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        mSensorMgr.registerListener(accelSensorListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorMgr.registerListener(lightSensorListener,light,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorMgr.unregisterListener(accelSensorListener);
        mSensorMgr.unregisterListener(lightSensorListener);
        super.onPause();
    }

    /*@Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            long curTime = System.currentTimeMillis();
            if((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS){

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2)) - SensorManager.GRAVITY_EARTH;

                if(acceleration > SHAKE_THRESHOLD){
                    mLastShakeTime = curTime;
                    Snackbar.make(findViewById(R.id.coordinator_layout),"Shake, shake, shake", Snackbar.LENGTH_LONG).show();

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Recipe recipe = new Recipe(data.getStringExtra(FormActivity.EXTRA_EDIT_RECIPE_TITLE)));
            recipeViewModel.insert(recipe);
            Snackbar.make(findViewById(R.id.coordinator_layout),getString(R.string.recipe_added),Snackbar.LENGTH_LONG).show();
        }else if(requestCode == EDIT_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedRecipe.setTitle(data.getStringExtra(EditRecipeActivity.EXTRA_EDIT_RECIPE_TITLE));
            recipeViewModel.update(editedRecipe);
            Book editedBook = new Book(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            editedBook.setId(data.getExtras().getInt("bookId"));
            bookViewModel.update(editedBook);
            Snackbar.make(findViewById(R.id.coordinator_layout),getString(R.string.recipe_edited),Snackbar.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),R.string.empty_not_saved,Toast.LENGTH_LONG).show();
        }
    }*/

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView recipeTitleTextView;
        private ImageView recipeImageView;


        public RecipeHolder (LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.recipe_list_item,parent,false));

            recipeTitleTextView = itemView.findViewById(R.id.recipe_title_text_view);
            recipeImageView = itemView.findViewById(R.id.recipe_image);
        }

        public void bind(Recipe recipe){
            recipeTitleTextView.setText(recipe.getTitle());
            if(recipe.getImageUrl() != null){
                Picasso.with(itemView.getContext()).load(recipe.getImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(recipeImageView);
            }else if(recipe.getImageBitmap() != null){
                recipeImageView.setImageBitmap(recipe.getImageBitmap());
            }
            else{
                recipeImageView.setImageResource(R.drawable.ic_image_black_24dp);
            }


            /*itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    recipeViewModel.delete(recipe);
                    return false;
                }

            });*/
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //id do extra
                    Intent intent = new Intent(MainActivity.this,RecipeDetailsActivity.class);
                    intent.putExtra(EXTRA_RECIPE_ID,recipe.getRecipeId());
                    startActivity(intent);
                }
            });
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder>{
        private List<Recipe> recipes;

        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipeHolder(getLayoutInflater(),parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            if(recipes != null){
                Recipe recipe = recipes.get(position);
                holder.bind(recipe);
            }else {
                Log.d("MainActivity","No recipes");
            }
        }

        @Override
        public int getItemCount() {
            if(recipes != null){
                return recipes.size();
            }else{
                return 0;
            }
        }

        void setRecipes(List<Recipe> recipes){
            this.recipes = recipes;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = mSensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        maxValue = light.getMaximumRange();

        mSensorMgr.registerListener(accelSensorListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecipeAdapter adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeViewModel.findAll().observe(this,new Observer<List<Recipe>>(){
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes){
                if(recipes == null || recipes.isEmpty()){
                    fetchRecipesData("chicken",0,5);
                    fetchRecipesData("pasta",0,5);
                    fetchRecipesData("pork",0,5);
                    fetchRecipesData("rice",60,65);
                    fetchRecipesData("beef",44,49);
                    //fetchRecipesData("fish",27,32);
                }else{
                    for(Recipe r:recipes){
                        recipesId.add(r.getRecipeId());
                    }
                    adapter.setRecipes(recipes);
                }

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void fetchRecipesData(String q, int from, int to){
        RecipeService recipeService = RetrofitInstance.getRetrofitInstance().create(RecipeService.class);

        Call<RecipeContainer> recipeApiCall = recipeService.findRecipes(q,app_id,app_key,from,to);

        recipeApiCall.enqueue(new retrofit2.Callback<RecipeContainer>(){
            @Override
            public void onResponse(Call<RecipeContainer> call, Response<RecipeContainer> response){
                for(int i = 0; i < response.body().getRecipeList().size();i++){
                    Recipe recipeTmp = response.body().getRecipeList().get(i).getOneRecipe();
                    recipeViewModel.insert(recipeTmp);
                }
            }
            @Override
            public void onFailure(Call<RecipeContainer> call, Throwable t){
                Snackbar.make(findViewById(R.id.coordinator_layout),"Something went wrong... Please try again later", Snackbar.LENGTH_LONG).show();
            }
        });

    }


}
