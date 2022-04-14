package com.example.map524_finalassignment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie implements Parcelable {

    // define primary key id
    @PrimaryKey
    int id;

    // define variables for other attributes / columns
    String title;
    @ColumnInfo ( name = "release_year" )
    String releaseYear;
    String description;
    @ColumnInfo ( name = "poster_path" )
    String posterPath;

    // define indicators for whether movie belongs in one of user's movie lists
    boolean isFavourite;
    boolean isWatchLater;

    // default movie constructor
    Movie(){
        id = 0;
        title = "";
        releaseYear = "";
        description = "";
        posterPath = "";

        isFavourite = false;
        isWatchLater = false;
    }

    // parcelable movie constructor
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseYear = in.readString();
        description = in.readString();
        posterPath = in.readString();
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    // getter that returns movie id
    public int getId() {
        return id;
    }

    // setter for movie id
    public void setId(int id) {
        this.id = id;
    }

    // getter that returns movie title
    public String getTitle() {
        return title;
    }

    // setter for movie title
    public void setTitle(String title) {
        this.title = title;
    }

    // getter that returns movie release date
    public String getReleaseDate() {
        return releaseYear;
    }

    // setter for movie release date
    public void setReleaseDate(String releaseDate) {
        this.releaseYear = releaseDate;
    }

    // getter that returns movie description
    public String getDescription() {
        return description;
    }

    // setter for movie description
    public void setDescription(String description) {
        this.description = description;
    }

    // getter that returns path for movie poster
    public String getPosterPath() {
        return posterPath;
    }

    // setter for poster path for movie
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    // getter that returns whether movie is within favourites list
    public boolean isFavourite() {
        return isFavourite;
    }

    // setter for whether movie is in favourites list
    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    // getter that returns whether movie is within watch later list
    public boolean isWatchLater() {
        return isWatchLater;
    }

    // setter for whether movie is in watch later list
    public void setWatchLater(boolean watchLater) {
        isWatchLater = watchLater;
    }

    @Override
    // method for parcelable to describe contents of parcel
    public int describeContents() {
        return 0;
    }

    @Override
    // method that writes movie object to a parcelable object
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(releaseYear);
        parcel.writeString(description);
        parcel.writeString(posterPath);
    }
}
