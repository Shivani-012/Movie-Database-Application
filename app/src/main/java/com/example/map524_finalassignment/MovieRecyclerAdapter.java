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

        // declare array list of products, context and special OnSelectListener object
        private ArrayList<Movie> listOfMovies;
        private Context context;
        private OnSelectListener myOnSelectListener;

        // Overloaded constructor that sets listOfPurchases, context and onSelectListener
        public MovieRecyclerAdapter(ArrayList<Movie> listOfMovies, Context context, OnSelectListener onSelectListener) {
            this.listOfMovies = listOfMovies;
            this.context = context;
            this.myOnSelectListener = onSelectListener;
        }

        public void setListOfMovies(ArrayList<Movie> movies){
            this.listOfMovies = movies;
        }

        // create method for a history view holder
        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // use inflater to set view to existing row
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_adapter_movie_row, parent, false);
            // return created view holder
            return new MovieViewHolder(view, myOnSelectListener);
        }

    // method that binds values of a purchase to view holder
        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            holder.titleText.setText(listOfMovies.get(position).getTitle());
            holder.dateText.setText(String.valueOf(listOfMovies.get(position).getReleaseDate()));

            //URL newurl = new URL(listOfMovies.get(position).getPosterPath());
            //Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            //holder.posterImg.setImageBitmap(mIcon_val);

            //holder.posterImg.setImageResource(listOfMovies.get(position).getPosterPath());
        }

        // method that returns the size of array list of purchases
        @Override
        public int getItemCount() {
            return listOfMovies.size();
        }

        // History View Holder Class
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

            // when view holder is clicked, call OnPurchaseSelect() passing the position of the purchase in the array list
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
