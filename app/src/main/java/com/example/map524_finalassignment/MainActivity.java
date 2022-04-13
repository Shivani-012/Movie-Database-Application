package com.example.map524_finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieRecyclerAdapter.OnSelectListener,
        NetworkingService.NetworkingListener {

    ArrayList<Movie> movieList = new ArrayList<Movie>();
    MovieRecyclerAdapter movieAdapter;
    RecyclerView movieTable;

    NetworkingService networkingManager;
    JsonService jsonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        jsonManager = ((MyApp)getApplication()).getJsonService();

        networkingManager.listener = this;
        networkingManager.getTrendingMovies();

        movieTable = findViewById(R.id.recyclerViewMain);

        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));
    }

    // creator for Menu that inflates it with items
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    // function that detects when a menu item is selected
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            // if search is selected
            case R.id.search_for_movie:
                // open search activity
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            // if "my lists" is selected
            case R.id.view_user_lists:
                // open UserList activity
                Intent userListIntent = new Intent(this, UserListActivity.class);
                startActivity(userListIntent);
                break;
        }

        return true;
    }

    @Override
    public void OnMovieClick(int position) {
        //  Open MovieDetail Activity
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        startActivity(movieDetailIntent);
    }

    @Override
    public void dataListener(String jsonString) {
        movieList = jsonManager.getMoviesFromJSON(jsonString);
        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder) {
        holder.posterImg.setImageBitmap(image);
    }
}