package com.example.recipebookapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

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

        ingredientsAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.recipe_details_ingredients_item,R.id.one_ingredient,ingredients);

        ingredientsList.setAdapter(ingredientsAdapter);
        setListViewHeightBasedOnChildren(ingredientsList);

        checkImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        saveRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String imageUrl = imageUrlEditText.getText().toString();
                if(imageUrl.isEmpty()) imageUrl = null;

                Recipe recipe = new Recipe(titleEditText.getText().toString(),imageUrl,sourceUrlEditText.getText().toString(),ingredients);
                recipe.setImageBitmap(imageBitmap);
                recipeViewModel.insert(recipe);

                Intent intent = new Intent(FormActivity.this,MainActivity.class);
                startActivity(intent);
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
