package com.example.recipebookapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    private RecipeViewModel recipeViewModel;
    private TextView titleTextView;
    private ImageView recipeImageView;
    private ListView ingredientsListView;
    private ArrayAdapter<String> listViewAdapter;
    private TextView linkTextView;
    private Button editButton;
    private Button deleteButton;
    private Recipe deletedRecipe;

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
        deleteButton = findViewById(R.id.recipe_details_delete_button);
        editButton = findViewById(R.id.recipe_details_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeDetailsActivity.this, FormActivity.class);
                intent.putExtra("recipeId",recipeId);
                startActivity(intent);
            }
        });


        AlertDialog confirmDelete = buildAlert();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete.show();
            }
        });

        recipeViewModel.findRecipeWithId(recipeId).observe(this,new Observer<Recipe>(){
            @Override
            public void onChanged(@Nullable final Recipe recipe){
                deletedRecipe = recipe;
                if(recipe != null){
                    titleTextView.setText(recipe.getTitle());
                    if(recipe.getImageUrl() != null){
                        Picasso.with(getBaseContext()).load(recipe.getImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(recipeImageView);
                    }else if(recipe.getImageBitmap() != null){
                        recipeImageView.setImageBitmap(recipe.getImageBitmap());
                    }
                    else{
                        recipeImageView.setImageResource(R.drawable.ic_image_black_24dp);
                    }
                    listViewAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.recipe_details_ingredients_item,R.id.one_ingredient,recipe.getIngredients());
                    ingredientsListView.setAdapter(listViewAdapter);
                    setListViewHeightBasedOnChildren(ingredientsListView);
                    linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    linkTextView.setText(Html.fromHtml("<a href=\""+recipe.getSourceUrl()+"\">Przepis</a>"));
                }

            }
        });
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

        params.height = totalHeight + 100 + (listView.getDividerHeight() * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public AlertDialog buildAlert(){
        AlertDialog.Builder confirmDeleteBuilder = new AlertDialog.Builder(this);
        confirmDeleteBuilder.setMessage("Czy na pewno chcesz usunąć ten przepis ?");
        confirmDeleteBuilder.setTitle("Potwierdzenie usunięcia");
        confirmDeleteBuilder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        confirmDeleteBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recipeViewModel.delete(deletedRecipe);
                Intent intent = new Intent(RecipeDetailsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        confirmDeleteBuilder.setCancelable(false);
        return confirmDeleteBuilder.create();
    }
}
