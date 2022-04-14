package com.example.map524_finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;

public class WatchLaterActivity extends AppCompatActivity implements
        MovieRecyclerAdapter.OnSelectListener,
        NetworkingService.NetworkingListener{

    ArrayList<Movie> movieList = new ArrayList<Movie>();
    MovieRecyclerAdapter movieAdapter;
    RecyclerView movieTable;

    DatabaseManager dbManager;

    NetworkingService networkingManager;
    JsonService jsonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later_list);

        dbManager = ((MyApp)getApplication()).dbManager;
        dbManager.getMovieDB(this);

        dbManager.getWatchLaterMovies();

        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        jsonManager = ((MyApp)getApplication()).getJsonService();

        networkingManager.listener = this;
        networkingManager.getTrendingMovies();

        movieTable = findViewById(R.id.recyclerViewWatchLater);

        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));

    }

    @Override
    public void OnMovieClick(int position) {
        //  Open MovieDetail Activity
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);

        // Add clicked movie object to intent extras
        movieDetailIntent.putExtra("clickedMovie", movieList.get(position));

        startActivity(movieDetailIntent);
    }

    @Override
    public void dataListener(String jsonString) {

    }

    @Override
    public void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder) {
        holder.posterImg.setImageBitmap(image);
    }
}