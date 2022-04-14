package com.example.map524_finalassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

        // declare array list of movies, context and special OnSelectListener object
        private ArrayList<Movie> listOfMovies;
        private final Context context;
        private final OnSelectListener myOnSelectListener;

        // declare network service (to be able to fetch movie posters)
        private final NetworkingService networkingService;

        // Overloaded constructor that sets listOfMovies, context, onSelectListener and networkingService
        public MovieRecyclerAdapter(ArrayList<Movie> listOfMovies, Context context, OnSelectListener onSelectListener, NetworkingService ns) {
            this.listOfMovies = listOfMovies;
            this.context = context;
            this.myOnSelectListener = onSelectListener;
            this.networkingService = ns;
        }

        // setter for list of movies
        public void setListOfMovies(ArrayList<Movie> movies){
            this.listOfMovies = movies;
        }

        // create method for a movie view holder
        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // use inflater to set view to existing row
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_adapter_movie_row, parent, false);
            // return created view holder
            return new MovieViewHolder(view, myOnSelectListener);
        }

    // method that binds values of a movie to view holder
        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            // set text of movie title & date
            holder.titleText.setText(listOfMovies.get(position).getTitle());
            holder.dateText.setText(String.valueOf(listOfMovies.get(position).getReleaseDate()));

            // use networkingService to get the bitmap for the movie poster
            networkingService.getMoviePoster(listOfMovies.get(position).getPosterPath(), holder);
        }

        // method that returns the size of array list of movies
        @Override
        public int getItemCount() {
            return listOfMovies.size();
        }

        // Movie View Holder Class
        public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // declare text views for each view field
            TextView titleText;
            TextView dateText;
            ImageView posterImg;

            OnSelectListener myOnSelectListener; // declare onSelectListener object

            // overloaded constructor
            public MovieViewHolder(@NonNull View itemView, OnSelectListener onSelectListener) {
                super(itemView);

                // initialize each text view
                titleText = itemView.findViewById(R.id.row_movie_title);
                dateText = itemView.findViewById(R.id.row_movie_date);
                posterImg = itemView.findViewById(R.id.row_movie_poster);

                this.myOnSelectListener = onSelectListener; // set onSelectListener object

                itemView.setOnClickListener(this); // listen to clicked for this holder
            }

            // when view holder is clicked, call OnMovieClick() passing the position of the movie in the array list
            @Override
            public void onClick(View view) {
                myOnSelectListener.OnMovieClick(getAdapterPosition());
            }
        }

        // special interface to help detect view holder clicks
        public interface OnSelectListener{
            void OnMovieClick(int position);
        }

}
