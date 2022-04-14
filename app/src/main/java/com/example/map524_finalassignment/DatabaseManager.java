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

    public void getAllMovies(){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().getAllMovies();
            }
        });
    }

    public void addMovie(Movie newMovie, boolean listType){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {

                // get list of all movies stored in database
                List<Movie> movies = db.movieDAO().getAllMovies();

                // create flag that indicates whether newMovie is already in list
                boolean updateFlag = movies.contains(newMovie);

                newMovie.setFavourite(listType == true ? true : false);
                newMovie.setWatchLater(listType == true ? false : true);

                if (updateFlag)
                    db.movieDAO().updateMovie(newMovie);
                else
                    db.movieDAO().addMovie(newMovie);
            }
        });
    }

    public void updateMovie(Movie movieToUpdate){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().updateMovie(movieToUpdate);
            }
        });
    }

    public void removeMovie(Movie movieToRemove){

        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().removeMovie(movieToRemove);
            }
        });

    }

    public void getFavouriteMovies() {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().getFavouriteMovies();
            }
        });
    }

    public void getWatchLaterMovies() {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.movieDAO().getWatchLaterMovies();
            }
        });
    }

}
