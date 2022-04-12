package com.example.map524_finalassignment;

import static android.text.TextUtils.indexOf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonService {

    public ArrayList<Movie> getMoviesFromJSON(String json)  {
        ArrayList<Movie> movieList = new ArrayList<>(0);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray movieArray = jsonObject.getJSONArray("results");
            for (int i = 0 ; i < movieArray.length();i++){
                JSONObject movieObject = movieArray.getJSONObject(i);
                JSONArray genreArray = movieObject.getJSONArray("genre_ids");

                Movie m = new Movie();

                m.setId(movieObject.getInt("id"));
                m.setTitle(movieObject.getString("original_title"));
                m.setReleaseDate(movieObject.getString("release_date"));
                m.setDescription(movieObject.getString("overview"));
                m.setPosterPath(movieObject.getString("poster_path"));

                int [] genreList = new int[genreArray.length()];

                for (int j = 0 ; j < genreArray.length() ; j++){
                    genreList[j] = genreArray.getInt(j);
                }

                m.setGenres(genreList);
                movieList.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

}
