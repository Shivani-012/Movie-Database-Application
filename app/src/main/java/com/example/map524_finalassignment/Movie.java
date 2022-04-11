package com.example.map524_finalassignment;

import java.util.Date;

public class Movie {

    int id;
    String title;
    Date releaseDate;
    String description;
    String posterPath;
    int [] genres;

    Movie(String title, Date date, String desc, String poster, int [] genres){
        this.title = title;
        this.releaseDate = date;
        this.description = desc;
        this.posterPath = poster;
        this.genres = genres;
    }

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
}
