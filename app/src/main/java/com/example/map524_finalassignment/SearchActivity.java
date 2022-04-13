package com.example.map524_finalassignment;

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

    ArrayList<Movie> movieList = new ArrayList<Movie>();
    MovieRecyclerAdapter movieAdapter;
    RecyclerView movieTable;

    NetworkingService networkingManager;
    JsonService jsonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        jsonManager = ((MyApp)getApplication()).getJsonService();

        networkingManager.listener = this;

        movieTable = findViewById(R.id.recyclerViewSearch);

        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));

        setTitle("Search Movies...");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchViewMenuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();
        String searchFor = searchView.getQuery().toString();
        if (!searchFor.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(searchFor, false);
        }

        searchView.setQueryHint("Search for Movies");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {// when the user clicks enter

                // search for movie using the text the user entered
                networkingManager.searchForMovie(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    // search for cities
                    networkingManager.searchForMovie(newText);
                }
                else {
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
    public void OnMovieClick(int position) {
        // Open Movie Detail Activity for movie
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

    }
}