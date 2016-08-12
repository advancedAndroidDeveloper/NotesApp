package com.example.notesapp.backendinteraction;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.notesapp.data.Note;
import com.example.notesapp.data.NoteRepositories;
import com.example.notesapp.data.NotesRepository;
import com.example.notesapp.data.NotesServiceApiImpl;
import com.example.shalini.myapplication.backend.notesApi.NotesApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;




public class ServerSyncService extends IntentService {



    private static NotesApi myApiService = null;
    private ServerResultReceiver resultReceiver;
    private int action=0;
    private Note requestedNote;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public ServerSyncService() {
        super("ServerSync");
    }


    @Override
    protected void onHandleIntent(final Intent intent) {
        this.action = intent.getIntExtra("action",0);
        String id=intent.getStringExtra("id");
        resultReceiver = intent.getParcelableExtra("receiverTag");

        NoteRepositories.getInMemoryRepoInstance(new NotesServiceApiImpl()).getNote(id,new NotesRepository.GetNoteCallback(){
            @Override
            public void onNoteLoaded(Note note) {
                requestedNote = note;
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
                    if(action==ServerConstants.ACTION_SAVE){
                        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        String imei=telephonyManager.getDeviceId();
                        com.example.shalini.myapplication.backend.notesApi.model.Note resultNote=myApiService.saveNote(requestedNote.getTitle(),requestedNote.getDescription(),imei).execute();
                        NoteRepositories.getInMemoryRepoInstance(new NotesServiceApiImpl()).updateNote(requestedNote,String.valueOf(resultNote.getId()));
                    }else{
                        String serverId=intent.getStringExtra("serverId");
                        if(!TextUtils.isEmpty(serverId)) {

                            myApiService.deleteNote(serverId).execute();
                        }
                    }
                    if(resultReceiver!=null){
                        resultReceiver.onReceiveResult(ServerConstants.RESULT_SUCCESS,null);
                    }
                } catch (IOException e) {
                    if(resultReceiver!=null){
                        Bundle bundle= new Bundle();
                        bundle.putString("Error_message",e.getLocalizedMessage());
                        resultReceiver.onReceiveResult(ServerConstants.RESULT_FAILURE,bundle);
                    }
                    e.printStackTrace();

                }


            }
        });


    }
}