package com.example.map524_finalassignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

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

    // declare and initialize url text for trending movies, movie search and poster search
    private final String trendingURL = "https://api.themoviedb.org/3/trending/movie/day?api_key=989e7ae3797c43076bfe511c208fc9cb&page=";
    private final String movieSearchURL = "https://api.themoviedb.org/3/search/movie?api_key=989e7ae3797c43076bfe511c208fc9cb&query=";
    private final String posterURL = "https://image.tmdb.org/t/p/w500";

    // declare and initialize variables for executor service and handler
    public static ExecutorService networkExecutorService = Executors.newFixedThreadPool(4);
    public static Handler networkingHandler = new Handler(Looper.getMainLooper());

    // interface that implements listeners for when data and images are received
    interface NetworkingListener{
        void dataListener(String jsonString);
        void imageListener(Bitmap image, MovieRecyclerAdapter.MovieViewHolder holder);
    }

    public NetworkingListener listener; // declare listener

    // method that gets trending movies
    public void getTrendingMovies(int pageNum){
        String urlForTrending = trendingURL + pageNum;
        connect(urlForTrending);
    }

    // method that searches for movies
    public void searchForMovie(String movieText){
        String urlForMovie = movieSearchURL + movieText;
        connect(urlForMovie);
    }

    // method that gets movie poster
    // parameters are poster path and MovieViewHolder, if movie image needs to be set in RecyclerView
    public void getMoviePoster(String posterPath, MovieRecyclerAdapter.MovieViewHolder holder) {
        // add poster path to poster url to get full url
        String urlForPoster = posterURL + posterPath;

        // open thread to connect to network
        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // initialize url with poster url
                    URL url = new URL(urlForPoster);
                    // get bitmap from content on url
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

                    // use handler to trigger imageListener in activity returning bitmap
                    networkingHandler.post(new Runnable() {
                    @Override
                        public void run() {
                            listener.imageListener(bitmap, holder);
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
        // open thread to connect to network
        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                // declare httpURLConnection
                HttpURLConnection httpURLConnection = null;

                try {
                    // declare string to hold Json message
                    String jsonString = "";
                    // create url from parameter
                    URL urlObject = new URL(url);

                    // create and set up connection
                    httpURLConnection = (HttpURLConnection)urlObject.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type","application/json");

                    // declare and initialize input stream and input stream reader
                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int inputStreamData;
                    // loop through characters in json message and add to json string
                    while ( (inputStreamData = reader.read()) != -1){
                        char current = (char)inputStreamData;
                        jsonString += current;
                    }
                    final String finalJsonString = jsonString;

                    // use handler to trigger dataListener in activity returning the final json string
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
                    // close httpURLConnection
                    httpURLConnection.disconnect();
                }
            }
        });
    }
}
