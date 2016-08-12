package com.example.notesapp.functional;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.test.ApplicationTestCase;
import android.test.ServiceTestCase;
import android.text.TextUtils;

import com.example.notesapp.backendinteraction.ServerConstants;
import com.example.notesapp.backendinteraction.ServerResultReceiver;
import com.example.notesapp.backendinteraction.ServerSyncService;
import com.example.notesapp.data.Note;
import com.example.notesapp.data.NoteRepositories;
import com.example.notesapp.data.NotesServiceApiImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by shalini on 12-08-2016.
 */
public class BackEndIntegrationTest  extends ServiceTestCase<ServerSyncService> {

        String mJsonString = null;
        Exception mError = null;
        CountDownLatch signal = null;

        public BackEndIntegrationTest() {
            super(ServerSyncService.class);
        }

        @Override
        protected void setUp() throws Exception {
            signal = new CountDownLatch(1);
        }

        @Override
        protected void tearDown() throws Exception {
            signal.countDown();
        }

        public void testSaveNoteOnServer() throws InterruptedException {

            //our synchronization aid
            CountDownLatch signal = new CountDownLatch(1);
            //passing object for evaluating the result of our service
            Map<String, String> result = new HashMap<String, String>();
            //Creating a new thread which is responsible for running our Service
            new Thread(new WorkerRunnable( signal, result)).start();
            //casuing the current to wait
            signal.await();
            assertEquals(ServerConstants.RESULT_SUCCESS, Integer.parseInt(result.get("result_value")));

        }



    class WorkerRunnable implements Runnable {
        private  CountDownLatch doneSignal;
        private Map<String, String> result;
        public WorkerRunnable(CountDownLatch cdl, Map<String, String> result){
            this.doneSignal = cdl;
            this.result = result;
        }
        @Override
        public void run() {
            Intent intent= new Intent(getContext(), ServerSyncService.class);
            intent.putExtra("action", ServerConstants.ACTION_SAVE);
            Note note= new Note("test","test server data",null);
            intent.putExtra("id",note.getId());
            NoteRepositories.getInMemoryRepoInstance(new NotesServiceApiImpl()).saveNote(note);

              ServerResultReceiver serviceCallback = new ServerResultReceiver(null) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    result.put("result_value", String.valueOf(resultCode));
                    doneSignal.countDown();
                }
            };
            intent.putExtra("receiverTag",
                    serviceCallback);
            startService(intent);
        }
    }

}
