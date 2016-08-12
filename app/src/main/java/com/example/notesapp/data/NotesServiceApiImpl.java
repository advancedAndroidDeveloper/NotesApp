/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.notesapp.data;

import android.os.Handler;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Implementation of the Notes Service API that adds a latency simulating network.
 */
public class NotesServiceApiImpl implements NotesServiceApi {

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;


    @Override
    public void getAllNotes(final NotesServiceCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                callback.onLoaded(NotesServiceApiEndpoint.getInstance().loadPersistedNotes());
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getNote(final String noteId, final NotesServiceCallback callback) {
        Note note = NotesServiceApiEndpoint.getInstance().getNote(noteId);
        callback.onLoaded(note);
    }


    @Override
    public void deleteNote(String noteId) {
        NotesServiceApiEndpoint.getInstance().deleteNote(noteId);
    }


    @Override
    public void saveNote(Note note) {
        NotesServiceApiEndpoint.getInstance().addNote(note);
    }



}
