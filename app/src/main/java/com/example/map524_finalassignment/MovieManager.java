package com.example.map524_finalassignment;

import java.util.ArrayList;

public class MovieManager {

        // declare array list of products
        private ArrayList<Movie> allMovies = new ArrayList<>();

        // constructor that accepts an array of products and pushes each into the list of products
        MovieManager(Movie[] movies){
            for (Movie movie : movies) {
                this.addMovie(movie);
            }
        }

        // method that returns array list of all products
        public ArrayList<Movie> getAllMovies(){
            return allMovies;
        }

        public void setMovies (ArrayList<Movie> newMovies){
            allMovies = newMovies;
        }

        // method that adds a new product to the array of products
        public void addMovie(Movie p){
            allMovies.add(p);
        }

        // method that returns the position of the product with a name matching the String parameter
        public int getProductPos(String product) {
            for(int i = 0; i< allMovies.size(); i++){
                if(product.equals(allMovies.get(i).getTitle())){
                    return i;
                }
            }
            return -1;
        }



}
