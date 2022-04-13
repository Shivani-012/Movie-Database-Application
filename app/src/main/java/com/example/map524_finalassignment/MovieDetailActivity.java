package com.example.map524_finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity implements
        NetworkingService.NetworkingListener, View.OnClickListener {

    // initialize layout elements
    ImageView movieImg;
    TextView movieTitle;
    TextView movieDate;
    TextView movieDesc;
    Button addMovieBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // get networking service and set listener
        NetworkingService networkingService = ((MyApp)getApplication()).getNetworkingService();
        networkingService.listener = this;

        // get movie object from intent
        Movie currentMovie = getIntent().getExtras().getParcelable("clickedMovie");

        // initialize movie image and get movie poster
        movieImg.findViewById(R.id.movie_detail_image);
        networkingService.getMoviePoster(currentMovie.getPosterPath(), null);

        // initialize movie title and set title text
        movieTitle.findViewById(R.id.movie_detail_title);
        movieTitle.setText(currentMovie.getTitle());

        // initialize movie date and set date text
        movieDate.findViewById(R.id.movie_detail_release_date);
        movieDate.setText(currentMovie.getReleaseDate());

        // initialize movie description and set description text
        movieDesc.findViewById(R.id.movie_detail_description);
        movieDesc.setText(currentMovie.getDescription());

        // initialize add button and set on click listener
        addMovieBtn.findViewById(R.id.add_movie_button);
        addMovieBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // open dialog fragment?
        // asks which list to add it to

    }

    @Override
    public void dataListener(String jsonString) {

    }

    @Override
    public void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder) {
        // set the movie poster image
        movieImg.setImageBitmap(image);
    }

}