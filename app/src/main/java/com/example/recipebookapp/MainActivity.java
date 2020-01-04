package com.example.recipebookapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_RECIPE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_RECIPE_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_EDIT_RECIPE_TITLE = "bookTitle";
    private Recipe editedRecipe;

    //chicken 0-5
    //pasta 0-5
    //pork 0-5
    //rice 60-65
    //beef 44-49
    //fish 27-32

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Recipe book = new Recipe(data.getStringExtra(EditRecipeActivity.EXTRA_EDIT_Recipe_TITLE)));
            recipeViewModel.insert(book);
            Snackbar.make(findViewById(R.id.coordinator_layout),getString(R.string.recipe_added),Snackbar.LENGTH_LONG).show();
        }else if(requestCode == EDIT_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedRecipe.setTitle(data.getStringExtra(EditRecipeActivity.EXTRA_EDIT_RECIPE_TITLE));
            recipeViewModel.update(editedRecipe);
            /*Book editedBook = new Book(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE),data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            editedBook.setId(data.getExtras().getInt("bookId"));
            bookViewModel.update(editedBook);*/
            Snackbar.make(findViewById(R.id.coordinator_layout),getString(R.string.recipe_edited),Snackbar.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(),R.string.empty_not_saved,Toast.LENGTH_LONG).show();
        }
    }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView recipeTitleTextView;


        public RecipeHolder (LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.recipe_list_item,parent,false));

            recipeTitleTextView = itemView.findViewById(R.id.recipe_title_text_view);

        }

        public void bind(Recipe recipe){
            recipeTitleTextView.setText(recipe.getTitle());
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    bookViewModel.delete(book);
                    return false;
                }

            });
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    editedRecipe = recipe;
                    Intent intent = new Intent(MainActivity.this,EditRecipeActivity.class);
                    intent.putExtra(EXTRA_EDIT_RECIPE_TITLE,recipe.getTitle());
                    //intent.putExtra("bookId",book.getId());
                    startActivityForResult(intent,EDIT_RECIPE_ACTIVITY_REQUEST_CODE);
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
            if(books != null){
                Recipe recipe = books.get(position);
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

        void setBooks(List<Recipe> recipes){
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


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
}
