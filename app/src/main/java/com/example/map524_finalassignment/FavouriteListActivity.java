package com.example.map524_finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;

public class FavouriteListActivity extends AppCompatActivity implements
        MovieRecyclerAdapter.OnSelectListener,
        NetworkingService.NetworkingListener{

    // declare array list, adapter and recycler view
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    MovieRecyclerAdapter movieAdapter;
    RecyclerView movieTable;

    // declare networking and Json services
    NetworkingService networkingManager;
    JsonService jsonManager;

    // declare database manager
    DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        // get database manager
        dbManager = ((MyApp)getApplication()).dbManager;
        dbManager.getMovieDB(this);

        dbManager.getFavouriteMovies(); // get movies in "Favourite Movies" list

        // get networking service and set listener
        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;

        // get json service
        jsonManager = ((MyApp)getApplication()).getJsonService();

        // initialize recycler view
        movieTable = findViewById(R.id.recyclerViewFavourite);

        // create & set adapter for recycler view, then set layout manager
        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));

        setTitle("Favourite Movies");
    }

    @Override
    // method that handles when a movie is selected in the recycler view
    public void OnMovieClick(int position) {
        //  Open MovieDetail Activity
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        // Add clicked movie object to intent extras
        movieDetailIntent.putExtra("clickedMovie", movieList.get(position));

        startActivity(movieDetailIntent);
    }

    @Override
    public void dataListener(String jsonString) { }

    @Override
    // method that detects when a bitmap image is returned from another thread
    public void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder) {
        holder.posterImg.setImageBitmap(image);
    }

}