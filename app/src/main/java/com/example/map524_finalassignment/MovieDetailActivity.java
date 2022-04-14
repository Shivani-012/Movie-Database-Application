package com.example.map524_finalassignment;

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

    // create dbManager
    DatabaseManager dbManager;

    Movie currentMovie;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // get networking service and set listener
        NetworkingService networkingService = ((MyApp)getApplication()).getNetworkingService();
        networkingService.listener = this;

        // get database manager from myApp and get movie database
        dbManager = ((MyApp)getApplication()).dbManager;
        dbManager.getMovieDB(this);

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
    public void dataListener(String jsonString) {

    }

    @Override
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
    public void dialogListenerOnFavourite() {
        String message = currentMovie.getTitle() + " added to Favourite Movies list.";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialogListenerOnWatchLater() {
        String message = currentMovie.getTitle() + " added to Watch Later list.";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialogListenerOnCancel() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
    }
}