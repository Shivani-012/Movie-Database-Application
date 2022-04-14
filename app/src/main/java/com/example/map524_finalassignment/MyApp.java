package com.example.map524_finalassignment;

import android.app.Application;


public class MyApp extends Application {

    // declare and initialize list type flags
    final boolean favouriteFlag = true;
    final boolean watchLaterFlag = false;

    // declare and initialize networking and Json services
    private final NetworkingService networkingService = new NetworkingService();
    private final JsonService jsonService = new JsonService();

    // getter for JsonService
    public JsonService getJsonService(){
        return jsonService;
    }

    // getter for NetworkingService
    public NetworkingService getNetworkingService(){
        return networkingService;
    }

    // declare and initialize database manager
    DatabaseManager dbManager = new DatabaseManager();
}
