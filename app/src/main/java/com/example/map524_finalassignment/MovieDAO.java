package com.example.map524_finalassignment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {

    @Insert
    void addNewMovie(Movie newMovie);

    @Delete
    void removeMovie(Movie movieToRemove);

    @Query("SELECT * FROM Movie")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM Movie WHERE isFavourite = 1")
    List<Movie> getFavouriteMovies();

    @Query("SELECT * FROM Movie WHERE isWatchLater = 1")
    List<Movie> getWatchLaterMovies();

    @Update
    void updateMovie(Movie movieToUpdate);
}
