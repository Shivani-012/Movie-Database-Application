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

    // declare array list, adapter and recycler view
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    MovieRecyclerAdapter movieAdapter;
    RecyclerView movieTable;

    // declare networking and Json services
    NetworkingService networkingManager;
    JsonService jsonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get networking service and set listener
        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;
        networkingManager.getTrendingMovies(); // get trending movies

        // get json service
        jsonManager = ((MyApp)getApplication()).getJsonService();

        // initialize recycler view
        movieTable = findViewById(R.id.recyclerViewMain);

        // create & set adapter for recycler view, then set layout manager
        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));

        setTitle("Trending Movies");
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
            case R.id.menu_search_movies:
                // open search activity
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            // if "favourite movies" is selected
            case R.id.menu_favourite_list:
                // open favourite list activity
                Intent favouriteListIntent = new Intent(this, FavouriteListActivity.class);
                startActivity(favouriteListIntent);
                break;
            // if "watch later" is selected
            case R.id.menu_watch_later_list:
                // open watch later list activity
                Intent watchLaterListIntent = new Intent(this, WatchLaterListActivity.class);
                startActivity(watchLaterListIntent);
                break;
        }
        return true;
    }

    @Override
    // method that handles when a movie is selected in the recycler view
    public void OnMovieClick(int position) {
        //  Open MovieDetail Activity
        Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
        // Add clicked movie object to intent extras
        movieDetailIntent.putExtra("clickedMovie", movieList.get(position));

        startActivity(movieDetailIntent); // start activity
    }

    @Override
    // method that detects when data is returned from another thread
    public void dataListener(String jsonString) {
        // get list of movies from json by parsing
        movieList = jsonManager.getMoviesFromJSON(jsonString);

        // reset movie adapter with new list of movies
        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    // method that detects when a bitmap image is returned from another thread
    public void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder) {
        holder.posterImg.setImageBitmap(image);
    }
}