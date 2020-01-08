package com.example.recipebookapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private RecipeViewModel recipeViewModel;

    private EditText titleEditText;
    private EditText ingredientEditText;
    private EditText imageUrlEditText;
    private Button addIngredientButton;
    private ListView ingredientsList;
    private ArrayAdapter<String> ingredientsAdapter;
    private ArrayList<String> ingredients;
    private RadioButton urlRadioButton;
    private RadioButton cameraRadioButton;
    private Button cameraButton;
    private ImageView imageView;
    private Button checkImageUrl;
    private Button saveRecipeButton;
    private EditText sourceUrlEditText;
    private Bitmap imageBitmap;
    private int recipeId;
    private String title;
    private String imageUrl;
    private String source;
    private Recipe editedRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        getSupportActionBar().setTitle("Form");



        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);


        titleEditText = findViewById(R.id.title_edit_text);
        ingredientEditText = findViewById(R.id.ingredient_edit_text);
        addIngredientButton = findViewById(R.id.add_ingredient_button);
        ingredientsList = findViewById(R.id.ingredients_list);
        urlRadioButton = findViewById(R.id.url_radio_button);
        cameraRadioButton = findViewById(R.id.camera_radio_button);
        imageUrlEditText = findViewById(R.id.image_url);
        cameraButton = findViewById(R.id.open_camera_button);
        imageView = findViewById(R.id.form_image_view);
        checkImageUrl = findViewById(R.id.check_image_url);
        saveRecipeButton = findViewById(R.id.save_recipe_button);
        sourceUrlEditText = findViewById(R.id.source_url);

        ingredients = new ArrayList<String>();





        if( getIntent().hasExtra("recipeId")){
            recipeId = getIntent().getExtras().getInt("recipeId");

            recipeViewModel.findRecipeWithId(recipeId).observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(Recipe recipe) {
                    editedRecipe = recipe;
                    titleEditText.setText(recipe.getTitle());
                    imageUrlEditText.setText(recipe.getImageUrl());
                    sourceUrlEditText.setText(recipe.getSourceUrl());
                    ingredients = recipe.getIngredients();
                    ingredientsAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.recipe_details_ingredients_item,R.id.one_ingredient,ingredients);
                    ingredientsList.setAdapter(ingredientsAdapter);
                    setListViewHeightBasedOnChildren(ingredientsList);
                    if(recipe.getImageUrl() != null){
                        Picasso.with(getBaseContext()).load(recipe.getImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(imageView);
                    }else if(recipe.getImageBitmap() != null){
                        imageView.setImageBitmap(recipe.getImageBitmap());
                    }
                    else{
                        imageView.setImageResource(R.drawable.ic_image_black_24dp);
                    }
                }
            });
        }else{
            ingredientsAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.recipe_details_ingredients_item,R.id.one_ingredient,ingredients);

            ingredientsList.setAdapter(ingredientsAdapter);
            setListViewHeightBasedOnChildren(ingredientsList);
        }


        ingredientsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ingredients.remove(position);
                ingredientsAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(ingredientsList);
                return true;
            }
        });

        checkImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editedRecipe != null){
                    imageBitmap = null;
                    editedRecipe.setImageBitmap(null);
                }
                if(imageUrlEditText.getText().toString() != null){
                    Picasso.with(getBaseContext()).load(imageUrlEditText.getText().toString()).placeholder(R.drawable.ic_image_black_24dp).into(imageView);
                } else{
                    imageView.setImageResource(R.drawable.ic_image_black_24dp);
                }            }
        });

        addIngredientButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ingredients.add(ingredientEditText.getText().toString());
                ingredientsAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(ingredientsList);
                ingredientEditText.setText("");
            }
        });



        urlRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imageUrlEditText.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    checkImageUrl.setVisibility(View.VISIBLE);
                }
            }
        });

        cameraRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imageUrlEditText.setVisibility(View.INVISIBLE);
                    cameraButton.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    checkImageUrl.setVisibility(View.INVISIBLE);
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    if(editedRecipe != null){
                        editedRecipe.setImageUrl(null);
                        imageUrlEditText.setText(null);
                    }

                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        saveRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageUrl = imageUrlEditText.getText().toString();
                title = titleEditText.getText().toString();
                source = sourceUrlEditText.getText().toString();

                if(imageUrl.isEmpty()) imageUrl = null;
                if(title.isEmpty()) title = null;
                if(source.isEmpty()) source = null;

                if(!getIntent().hasExtra("recipeId")){

                    if(title != null){
                        Recipe recipe = new Recipe(title,imageUrl,source,ingredients);
                        recipe.setImageBitmap(imageBitmap);
                        recipeViewModel.insert(recipe);

                        Intent intent = new Intent(FormActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else{
                        Snackbar.make(findViewById(R.id.form_layout),"Musisz przynajmniej wpisac tytul przepisu", Snackbar.LENGTH_LONG).show();

                    }
                }else{
                    editedRecipe.setTitle(title);
                    editedRecipe.setSourceUrl(source);
                    editedRecipe.setImageBitmap(imageBitmap);
                    editedRecipe.setImageUrl(imageUrl);
                    editedRecipe.setIngredients(ingredients);
                    recipeViewModel.update(editedRecipe);
                    Intent intent = new Intent(FormActivity.this,RecipeDetailsActivity.class);
                    intent.putExtra("recipeId", recipeId);
                    startActivity(intent);

                }



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + 100 + (listView.getDividerHeight() *
                (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
