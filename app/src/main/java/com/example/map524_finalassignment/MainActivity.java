package com.example.map524_finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieRecyclerAdapter.OnSelectListener,
        NetworkingService.NetworkingListener,
        View.OnClickListener {

    // declare views for page navigation
    Button backBtn, nextBtn;
    TextView pageNumView;

    // declare array list, adapter and recycler view
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    MovieRecyclerAdapter movieAdapter;
    RecyclerView movieTable;

    // declare networking and Json services
    NetworkingService networkingManager;
    JsonService jsonManager;

    // declare integer to hold page number
    int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get query from save instance state
        if (savedInstanceState != null)
            pageNum = savedInstanceState.getInt("page_number");
        else
            pageNum = 1;

        // get networking service and set listener
        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;
        networkingManager.getTrendingMovies(pageNum); // get trending movies

        // get json service
        jsonManager = ((MyApp)getApplication()).getJsonService();

        backBtn = findViewById(R.id.trending_back_btn);
        backBtn.setOnClickListener(this);

        nextBtn = findViewById(R.id.trending_next_btn);
        nextBtn.setOnClickListener(this);

        pageNumView = findViewById(R.id.trending_page_number);
        pageNumView.setText(String.valueOf(pageNum));
        if (pageNum == 1){
            backBtn.setEnabled(false);
            backBtn.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray)));
        }
        else {
            backBtn.setEnabled(true);
            backBtn.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_500)));
        }

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

    @Override
    // save counter values in save instance state
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page_number", pageNum);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId(); // get view id

        switch(id) {
            // if back button is clicked
            case R.id.trending_back_btn:
                if (pageNum > 1) {
                    --pageNum; // decrement page number
                    networkingManager.getTrendingMovies(pageNum); // get trending movies for previous page
                    pageNumView.setText(String.valueOf(pageNum));

                    // if page is page 1
                    if (pageNum == 1) {
                        // disable back button and change colour
                        backBtn.setEnabled(false);
                        backBtn.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.gray)));
                    }
                }
                break;
            case R.id.trending_next_btn:

                // if current page number is 1
                if (pageNum == 1) {
                    // enable back button and change colour
                    backBtn.setEnabled(true);
                    backBtn.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_500)));
                }

                ++pageNum; // increment page number
                networkingManager.getTrendingMovies(pageNum); // get trending movies for next page
                pageNumView.setText(String.valueOf(pageNum));

                break;
        }
    }

}