package com.example.map524_finalassignment;

import android.app.Application;

public class MyApp extends Application {

    MovieManager movie_list;

    private NetworkingService networkingService = new NetworkingService();
    private JsonService jsonService = new JsonService();

    public JsonService getJsonService(){
        return jsonService;
    }

    public NetworkingService getNetworkingService(){
        return networkingService;
    }

}
