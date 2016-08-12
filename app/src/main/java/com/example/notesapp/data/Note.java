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

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Immutable model class for a Note.
 */
public  class Note extends RealmObject {
    @PrimaryKey
    private  String id;
    @Nullable
    private  String title;
    @Nullable
    private  String description;
    @Nullable
    private  String imageUrl;
    private  String serverId="";

    public Note() {
        id = UUID.randomUUID().toString();
    }

    public Note(@Nullable String title, @Nullable String description, @Nullable String imageUrl) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public String getServerId() {
        return serverId;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }


}
