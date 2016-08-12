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

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.example.notesapp.NotesApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
public final class NotesServiceApiEndpoint {


    private static NotesServiceApiEndpoint instance;
    private final Realm realm;

    private NotesServiceApiEndpoint() {
        realm =
                Realm.getInstance(
                        new RealmConfiguration.Builder(NotesApplication.getAppContext())
                                .name("myOtherRealm.realm")
                                .build()
                );
    }

    public static NotesServiceApiEndpoint getInstance() {

        if (instance == null) {
            instance = new NotesServiceApiEndpoint();
        }
        return instance;
    }



    public  void addNote(Note note) {
        realm.beginTransaction();
        realm.copyToRealm(note);
        realm.commitTransaction();
    }

    //query a single item with the given id
    public Note getNote(String id) {

        return realm.where(Note.class).equalTo("id", id).findFirst();
    }

    //query a single item with the given id
    public void deleteNote(String id) {
        realm.beginTransaction();
        Note result = realm.where(Note.class).equalTo("id", id).findFirst();
        result.removeFromRealm();
        realm.commitTransaction();
    }


    /**
     * @return the Notes to show when starting the app.
     */
    public  RealmResults<Note> loadPersistedNotes() {

        // Sort by id, in descending order
        RealmResults<Note> results =
                realm.where(Note.class)
                        .findAllSorted("id", false);
        return results;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Note.class);
        realm.commitTransaction();
    }


}
