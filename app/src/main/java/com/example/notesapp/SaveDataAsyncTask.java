package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.shalini.myapplication.backend.notesApi.NotesApi;
import com.example.shalini.myapplication.backend.notesApi.model.Note;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;




public class SaveDataAsyncTask extends AsyncTask<Void, Note, Note> {
    private static NotesApi myApiService = null;
    private Context context;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //    @Override
    protected Note doInBackground(Void... params) {



        Log.v("My String : ","doInBack Called");
        if(myApiService == null) {  // Only do this once
            NotesApi.Builder builder = new NotesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("https://notes-137310.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            myApiService.deleteNote("5639445604728832").execute();
            return null;



        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Note result) {
        Log.v("My String : ","onPost Called");

        if(result!=null) {


                Log.v("My String : ",""+result.getId());

        }
        else
        {
            Log.v("My String : ","error...");
        }
    }


}