package com.example.map524_finalassignment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements
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
        setContentView(R.layout.activity_search);

        // get networking service and set listener
        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;

        // get json service
        jsonManager = ((MyApp)getApplication()).getJsonService();

        // initialize recycler view
        movieTable = findViewById(R.id.recyclerViewSearch);

        // create & set adapter for recycler view, then set layout manager
        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));

        setTitle("Search Movies..."); // set title for search bar
    }

    // method that creates search menu
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        // inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // initialize search menu
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);

        // get search view query
        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();
        String searchFor = searchView.getQuery().toString();
        // if query is empty, set icon and query
        if (!searchFor.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(searchFor, false);
        }

        // set query hint
        searchView.setQueryHint("Search for Movies");

        // set query text listener to listen for user input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            // when text is submitted (user enters)
            public boolean onQueryTextSubmit(String query) {
                // search for movie using the text the user entered
                networkingManager.searchForMovie(query);
                return true;
            }

            @Override
            // when text in search bar is changed (user types)
            public boolean onQueryTextChange(String newText) {
                // if the text is more than 3 characters
                if (newText.length() >= 3) {
                    // search for movies
                    networkingManager.searchForMovie(newText);
                }
                // otherwise not given enough to search
                else {
                    // set movie list back to 0 and reset adapter
                    movieList = new ArrayList<>(0);
                    movieAdapter.setListOfMovies(movieList);
                    movieAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
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