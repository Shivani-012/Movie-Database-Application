package com.example.map524_finalassignment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    static MovieDatabase db;
    ExecutorService dbExecutor = Executors.newFixedThreadPool(4);
    Handler dbHandler = new Handler(Looper.getMainLooper());

    private static void buildMovieDBInstance(Context context){
        db = Room.databaseBuilder(context, MovieDatabase.class, "movie_database").build();
    }

    public MovieDatabase getMovieDB(Context context){
        if (db == null)
            buildMovieDBInstance(context);
        return db;
    }

    public void saveMovie(Movie newMovie, boolean listType){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {

                // check if its in movies ?
                List<Movie> movies = db.movieDAO().getAllMovies();

                boolean updateFlag = false;
                updateFlag = movies.contains(newMovie);

                newMovie.setFavourite(listType == true ? true : false);
                newMovie.setWatchLater(listType == true ? false : true);

                if (updateFlag)
                    db.movieDAO().updateMovie(newMovie);
                else
                    db.movieDAO().addMovie(newMovie);
            }
        });
    }

    public boolean removeMovie(Movie movieToRemove){

        List<Movie> movies = db.movieDAO().getAllMovies();

        if (movies.contains(movieToRemove)) {
            db.movieDAO().removeMovie(movieToRemove);
            return true;
        }
        else
            return false;
    }

    public List<Movie> getFavouriteMovies() {
        List<Movie> movieList = db.movieDAO().getFavouriteMovies();
        return movieList;
    }

    public List<Movie> getWatchLaterMovies() {
        List<Movie> movieList = db.movieDAO().getWatchLaterMovies();
        return movieList;
    }

}
