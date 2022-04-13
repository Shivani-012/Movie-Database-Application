package com.example.map524_finalassignment;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie implements Parcelable {

    @PrimaryKey
    int id;
    String title;
    @ColumnInfo ( name = "release_year" )
    String releaseYear;
    String description;
    @ColumnInfo ( name = "poster_path" )
    String posterPath;
    int [] genres;

    boolean isFavourite;
    boolean isWatchLater;

    Movie(){
        id = 0;
        title = "";
        releaseYear = "";
        description = "";
        posterPath = "";
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseYear = in.readString();
        description = in.readString();
        posterPath = in.readString();
        genres = in.createIntArray();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseYear;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseYear = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isWatchLater() {
        return isWatchLater;
    }

    public void setWatchLater(boolean watchLater) {
        isWatchLater = watchLater;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(releaseYear);
        parcel.writeString(description);
        parcel.writeString(posterPath);
        parcel.writeIntArray(genres);
    }
}
