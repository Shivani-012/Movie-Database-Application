package com.example.map524_finalassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class WatchLaterListActivity extends AppCompatActivity implements
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

    // declare builder for alert dialog box
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later_list);

        // initialize builder for alert dialog box
        builder = new AlertDialog.Builder(this);

        // get database manager
        dbManager = ((MyApp)getApplication()).dbManager;
        dbManager.getMovieDB(this);

        dbManager.getWatchLaterMovies(); // get movies in "Watch Later" list

        // get networking service and set listener
        networkingManager = ((MyApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;

        // get json service
        jsonManager = ((MyApp)getApplication()).getJsonService();

        // initialize recycler view
        movieTable = findViewById(R.id.recyclerViewWatchLater);

        // create & set adapter for recycler view, then set layout manager
        movieAdapter = new MovieRecyclerAdapter(movieList, this, this, networkingManager);
        movieTable.setAdapter(movieAdapter);
        movieTable.setLayoutManager((new LinearLayoutManager(this)));

        // declare item touch helper to enable swipe to delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(movieTable);

        setTitle("Watch Later");
    }

    // method that handles when a movie in the recycler view is moved / swiped
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
            new ItemTouchHelper.SimpleCallback(
                    0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                // method that handles when a movie is swiped
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    // get position of swiped movie
                    int position = viewHolder.getAdapterPosition();

                    // get swiped movie
                    Movie swipedMovie = movieList.get(position);

                    // use alert dialog to confirm with user that they want to remove movie from list
                    builder.setMessage("Do want to remove " + swipedMovie.getTitle() + " from your Watch Later list?")
                            .setPositiveButton("OK", (dialog, i) -> {

                                dbManager.removeMovie(swipedMovie);

                            })
                            .setNegativeButton("CANCEL", (dialog, i) -> {

                            })
                            .setCancelable(false)
                            .show();
                }
            };

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