package com.example.map524_finalassignment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    // call back interfaces to communicate with activities
    interface DBCallBackInterface {
        void movieAdded(boolean result, boolean listType);
        void movieRemoved(boolean result);
        void movieUpdated(boolean listType);
        void listOfAllMovies(ArrayList<Movie> movies);
        void listOfFavouriteMovies(ArrayList<Movie> movies);
        void listOfWatchLaterMovies(ArrayList<Movie> movies);
    }

    // declare variables for database, callback interface, executor and handler
    static MovieDatabase db;
    DBCallBackInterface listener;
    ExecutorService dbExecutor = Executors.newFixedThreadPool(4);
    Handler dbHandler = new Handler(Looper.getMainLooper());

    private static void buildMovieDBInstance(Context context){
        db = Room.databaseBuilder(context, MovieDatabase.class, "movie_database").build();
    }

    // getter for movie database
    public MovieDatabase getMovieDB(Context context){
        if (db == null)
            buildMovieDBInstance(context);
        return db;
    }

    // method that gets all movie in database
    public void getAllMovies(){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // get list of all movies from database
                ArrayList<Movie> movieList = db.movieDAO().getAllMovies();
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // trigger listener in activity passing list of movies
                        listener.listOfAllMovies(movieList);
                    }
                });
            }
        });
    }

    // method that adds a movie to the database
    public void addMovie(Movie newMovie, boolean listType){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {

                // get list of all movies stored in database
                List<Movie> movies = db.movieDAO().getAllMovies();

                // create flag that indicates whether newMovie is already in list
                boolean inList = movies.contains(newMovie);

                // if the movie is already in the list, check if the movie list type has changed
                // checking if the list type matches whether the movie is in the Favourite Movies
                // list will tell is it has been updated.
                // will go into statement if the movie is the SAME! Do not want to add duplicate movies!
                if (inList && listType == newMovie.isFavourite()){
                    dbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // tried to add same movie, return false
                            listener.movieAdded(false, listType);
                        }
                    });
                }
                // otherwise movie is either new or has been updated
                else {
                    // set the list flags depending on the list type
                    newMovie.setFavourite(listType == true ? true : false);
                    newMovie.setWatchLater(listType == true ? false : true);

                    // if the movie is in the list, update the movie in the database
                    if (inList)
                        updateMovie(newMovie, listType);
                    // else, dd the movie to the database
                    else {
                        db.movieDAO().addMovie(newMovie);

                        dbHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // trigger listener in activity
                                listener.movieAdded(true, listType);
                            }
                        });
                    }
                }
            }
        });
    }

    // method that updates a movie in database
    public void updateMovie(Movie movieToUpdate, boolean listType){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // update movie
                db.movieDAO().updateMovie(movieToUpdate);
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // trigger listener in activity passing the list type (for message)
                        listener.movieUpdated(listType);
                    }
                });
            }
        });
    }

    // method that removes a movie from database
    public void removeMovie(Movie movieToRemove){
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // remove movie
                db.movieDAO().removeMovie(movieToRemove);

                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // trigger listener in activity
                        listener.movieRemoved(true);
                    }
                });
            }
        });
    }

    // method that returns a list of movies within the favourite movies list
    public void getFavouriteMovies() {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // get list of favourite movies
                ArrayList<Movie> movieList = db.movieDAO().getFavouriteMovies();

                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // trigger listener in activity passing list of favourite movies
                        listener.listOfFavouriteMovies(movieList);
                    }
                });
            }
        });
    }

    // method that returns a list of movies within the watch later list
    public void getWatchLaterMovies() {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // get list of watch later movies
                ArrayList<Movie> movieList = db.movieDAO().getWatchLaterMovies();

                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // trigger listener in activity passing list of watch later movies
                        listener.listOfWatchLaterMovies(movieList);
                    }
                });
            }
        });
    }
}
