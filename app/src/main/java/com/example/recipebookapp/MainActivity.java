package com.example.recipebookapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Variable to store brightness value
    private int brightness;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;

    private RecipeViewModel recipeViewModel;

    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private SensorManager mSensorMgr;

    private Sensor accelerometer;

    private Sensor light;
    private float maxValue;

    private Random rnd;
    private ArrayList<Integer> recipesId = new ArrayList<Integer>();

    public static final String EXTRA_RECIPE_ID = "recipeId";
    Handler handler = new Handler();

    private static String app_id = "5c4d239d";
    private static String app_key = "fbfcc1588959c5c95444569037fcf6c7";

    float value;



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
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private final SensorEventListener lightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            value = event.values[0];

            try
            {
                // To handle the auto
                Settings.System.putInt(cResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                //Get the current system brightness
                brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);

            }
            catch (Settings.SettingNotFoundException e)
            {
                e.printStackTrace();
            }
            if(value < 3 && brightness > 10){
                Snackbar.make(findViewById(R.id.coordinator_layout),R.string.snackbar_dark, Snackbar.LENGTH_LONG).show();

                brightness = 10;

                //Set the system brightness using the brightness variable value
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }else if(value > 40 && brightness < 150){
                Snackbar.make(findViewById(R.id.coordinator_layout),R.string.snackbar_light, Snackbar.LENGTH_LONG).show();

                brightness = 150;
                //Set the system brightness using the brightness variable value
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
            }
    else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        cResolver = getContentResolver();
        window = getWindow();

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
