package com.example.shalini.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by shalini on 15-07-2016.
 */
@Entity
public  class Note  {

    @Id
    Long id;
    private String title;
    private String description;
    private String IMEI;

    public Note(String title, String description, String IMEI) {
        this.title = title;
        this.description = description;
        this.IMEI = IMEI;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }
}
