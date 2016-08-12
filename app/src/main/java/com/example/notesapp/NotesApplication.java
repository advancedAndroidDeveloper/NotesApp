package com.example.notesapp;

import android.app.Application;
import android.content.Context;

import com.example.notesapp.data.NotesServiceApiEndpoint;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by shalini on 11-08-2016.
 */
public class NotesApplication extends Application {

    private static  Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=this;
    }
    public static Context getAppContext() {
        return NotesApplication.applicationContext;
    }

}
