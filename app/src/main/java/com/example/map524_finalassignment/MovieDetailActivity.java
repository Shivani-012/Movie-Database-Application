package com.example.map524_finalassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MovieDetailActivity extends AppCompatActivity implements
        NetworkingService.NetworkingListener,
        View.OnClickListener,
        FragmentDialog.DialogClickListener {

    // initialize layout elements
    ImageView movieImg;
    TextView movieTitle;
    TextView movieDate;
    TextView movieDesc;
    Button addMovieBtn;

    // create managers for database and fragment
    DatabaseManager dbManager;
    FragmentManager fm = getSupportFragmentManager();

    // create movie object to store current movie
    Movie currentMovie;

    // create indicators for list type when adding a movie
    boolean favouriteIndicator;
    boolean watchLaterIndicator;

    // declare builder for alert dialog box
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // initialize builder for alert dialog box
        builder = new AlertDialog.Builder(this);

        // get networking service and set listener
        NetworkingService networkingService = ((MyApp)getApplication()).getNetworkingService();
        networkingService.listener = this;

        // get database manager from myApp and get movie database
        dbManager = ((MyApp)getApplication()).dbManager;
        dbManager.getMovieDB(this);

        // get indicator flags for list types
        favouriteIndicator = ((MyApp)getApplication()).favouriteFlag;
        watchLaterIndicator = ((MyApp)getApplication()).watchLaterFlag;

        // get movie object from intent
        currentMovie = getIntent().getExtras().getParcelable("clickedMovie");

        // initialize movie image and get movie poster
        movieImg = findViewById(R.id.movie_detail_image);
        networkingService.getMoviePoster(currentMovie.getPosterPath(), null);

        // initialize movie title and set title text
        movieTitle = findViewById(R.id.movie_detail_title);
        movieTitle.setText(currentMovie.getTitle());

        // initialize movie date and set date text
        movieDate = findViewById(R.id.movie_detail_release_date);
        movieDate.setText(currentMovie.getReleaseDate());

        // initialize movie description and set description text
        movieDesc = findViewById(R.id.movie_detail_description);
        movieDesc.setText(currentMovie.getDescription());

        // initialize add button and set on click listener
        addMovieBtn = findViewById(R.id.add_movie_button);
        addMovieBtn.setOnClickListener(this);
    }

    @Override
    public void dataListener(String jsonString) { }

    @Override
    // image listener function that sets the movie poster image
    public void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder) {
        // set the movie poster image
        movieImg.setImageBitmap(image);
    }

    @Override
    // method for when "Add to List" button is clicked
    public void onClick(View view) {

        // create new instance of fragment and show it
        FragmentDialog dialog = FragmentDialog.newInstance(currentMovie.getTitle());
        dialog.show(fm, FragmentDialog.Tag);

        dialog.listener = this; // set listener
    }

    @Override
    // method that handles when adding a movie to favourites list is selected
    public void dialogListenerOnFavourite() {
        String message; // declare message for toast

        // check if movie is already in favourites list
        if (currentMovie.isFavourite()){
            // set abort message
            message = currentMovie.getTitle() + " in already in Favourite Movies list.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        // else movie is not in favourites list
        else {
            // check if movie is in watch later list
            if (currentMovie.isWatchLater()){

                // use alert dialog to confirm with user that they want to transfer movie to their favourite movie list
                builder.setMessage(currentMovie.getTitle() + " is currently in your Watch Later list. Would you like to transfer it to your Favourite Movies list?")
                        .setPositiveButton("OK", (dialog, i) -> {

                            currentMovie.setWatchLater(false); // set watch later flag
                            currentMovie.setFavourite(true); // set favourite flag

                            dbManager.updateMovie(currentMovie); // update movie

                            // print success message
                            Toast.makeText(this, currentMovie.getTitle() + " has been moved to your Favourite Movies list.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", (dialog, i) -> {

                            // print cancel message
                            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
                }
            else {

                currentMovie.setFavourite(true); // set favourite flag

                // add movie to database
                dbManager.addMovie(currentMovie, favouriteIndicator);

                // set success message & print
                message = currentMovie.getTitle() + " added to Favourite Movies list.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    // method that handles when adding a movie to watch later list is selected
    public void dialogListenerOnWatchLater() {

        String message; // declare message for toast

        // check if movie is already in watch later list
        if (currentMovie.isWatchLater()){
            // set abort message
            message = currentMovie.getTitle() + " in already in Watch Later list.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        // else movie is not in watch later list
        else {
            // check if movie is in favourites list
            if (currentMovie.isFavourite()){

                // use alert dialog to confirm with user that they want to transfer movie to their watch later movie list
                builder.setMessage(currentMovie.getTitle() + " is currently in your Favourite Movies list. Would you like to transfer it to your Watch Later list?")
                        .setPositiveButton("OK", (dialog, i) -> {

                            currentMovie.setFavourite(false); // set favourite flag
                            currentMovie.setWatchLater(true); // set watch later flag

                            dbManager.updateMovie(currentMovie); // update movie

                            // print success message
                            Toast.makeText(this, currentMovie.getTitle() + " has been moved to your Watch Later list.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("CANCEL", (dialog, i) -> {

                            // print cancel message
                            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            }
            else {

                currentMovie.setWatchLater(true); // set watch later flag

                // add movie to database
                dbManager.addMovie(currentMovie, watchLaterIndicator);

                // set success message
                message = currentMovie.getTitle() + " added to Watch Later list.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    // method that when cancel is selected on dialog fragment
    public void dialogListenerOnCancel() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
    }
}