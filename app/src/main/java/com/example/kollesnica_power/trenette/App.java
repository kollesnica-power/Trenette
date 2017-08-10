package com.example.kollesnica_power.trenette;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by kollesnica1337 on 10.08.2017.
 * Application class.
 */

public class App extends Application {

    private static App instance;

    public static synchronized App getInstance()
    {
        if (instance == null) {

            instance = new App();

        }

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;

        // Configure Realm for the application
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("trenette.realm")
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }


}
