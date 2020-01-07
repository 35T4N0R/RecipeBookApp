package com.example.recipebookapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeViewModel recipeViewModel;
    private TextView titleTextView;
    private ImageView recipeImageView;
    private ListView ingredientsListView;
    private ArrayAdapter<String> listViewAdapter;
    private TextView linkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setTitle("Details");

        int recipeId = getIntent().getExtras().getInt("recipeId");
        titleTextView = findViewById(R.id.recipe_details_title);
        recipeImageView = findViewById(R.id.recipe_details_image);
        ingredientsListView = findViewById(R.id.recipe_details_ingredients);
        linkTextView = findViewById(R.id.recipe_details_link);
        recipeViewModel.findRecipeWithId(recipeId).observe(this,new Observer<Recipe>(){
            @Override
            public void onChanged(@Nullable final Recipe recipe){
                titleTextView.setText(recipe.getTitle());
                if(recipe.getImageUrl() != null){
                    Picasso.with(getBaseContext()).load(recipe.getImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(recipeImageView);
                } else{
                    recipeImageView.setImageResource(R.drawable.ic_image_black_24dp);
                }
                listViewAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.recipe_details_ingredients_item,R.id.one_ingredient,recipe.getIngredients());
                ingredientsListView.setAdapter(listViewAdapter);
                //Linkify.addLinks(linkTextView,Linkify.ALL);
                linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
                linkTextView.setText(Html.fromHtml("<a href=\""+recipe.getSourceUrl()+"\">Przepis</a>"));
                /*linkTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(recipe.getSourceUrl()));
                        startActivity(intent);
                    }
                });*/
            }
        });
    }
}
