package com.example.map524_finalassignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkingService {

    private String trendingURL = "https://api.themoviedb.org/3/trending/movie/day?api_key=989e7ae3797c43076bfe511c208fc9cb";
    private String movieSearchURL = "https://api.themoviedb.org/3/search/movie?api_key=989e7ae3797c43076bfe511c208fc9cb&query=";
    private String posterURL = "https://image.tmdb.org/t/p/w500";

    public static ExecutorService networkExecutorService = Executors.newFixedThreadPool(4);
    public static Handler networkingHandler = new Handler(Looper.getMainLooper());

    interface NetworkingListener{
        void dataListener(String jsonString);
        void imageListener(Bitmap image);
    }

    public NetworkingListener listener;

    public void getTrendingMovies(){
        connect(trendingURL);
    }

    public void searchForMovie(String movieChars){
        String urlForMovie = movieSearchURL + movieChars;
        connect(urlForMovie);
    }

    public void getMoviePoster(String posterPath) {
        String urlForPoster = posterURL + posterPath;
        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(urlForPoster);
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

                    networkingHandler.post(new Runnable() {
                    @Override
                        public void run() {
                            listener.imageListener(bitmap);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void connect (String url){
        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;


                try {

                    String jsonString = "";
                    URL urlObject = new URL(url);

                    httpURLConnection= (HttpURLConnection)urlObject.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type","application/json");

                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int inputStreamData = 0;
                    while ( (inputStreamData = reader.read()) != -1){
                        char current = (char)inputStreamData;
                        jsonString+= current;
                    }

                    final String finalJsonString = jsonString;
                    networkingHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.dataListener(finalJsonString);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    httpURLConnection.disconnect();
                }

            }
        });
    }

}
