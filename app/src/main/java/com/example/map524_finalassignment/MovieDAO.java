package com.example.map524_finalassignment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

@Dao
public interface MovieDAO {

    @Insert
    void addMovie(Movie newMovie);

    @Delete
    void removeMovie(Movie movieToRemove);

    @Query("SELECT * FROM Movie")
    ArrayList<Movie> getAllMovies();

    @Query("SELECT * FROM Movie WHERE isFavourite = 1")
    ArrayList<Movie> getFavouriteMovies();

    @Query("SELECT * FROM Movie WHERE isWatchLater = 1")
    ArrayList<Movie> getWatchLaterMovies();

    @Update
    void updateMovie(Movie movieToUpdate);
}
