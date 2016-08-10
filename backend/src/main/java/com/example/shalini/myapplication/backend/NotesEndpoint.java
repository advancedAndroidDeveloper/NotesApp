/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.shalini.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "notesApi",
  version = "v2",
  namespace = @ApiNamespace(
    ownerDomain = "backend.myapplication.shalini.example.com",
    ownerName = "backend.myapplication.shalini.example.com",
    packagePath=""
  )
)
public class NotesEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "saveNote")
    public Note saveNote(@Named("title") String title,@Named("description") String description,@Named("IMEI") String IMEI) {
        ObjectifyService.register(Note.class);

        // Simple create
        Note note = new Note(title,description,IMEI);
        Result<Key<Note>> result=ofy().save().entity(note);   // synchronous
        result.now();
        if(note.id!= null) {
            return note;
        }
        else{
            return null;
        }
    }

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "deleteNote")
    public Void deleteNote(@Named("id") String id) {
        ObjectifyService.register(Note.class);

        ofy().delete().type(Note.class).id(Long.valueOf(id)).now();

        return null;
    }

}
