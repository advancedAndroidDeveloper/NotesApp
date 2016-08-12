/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.example.notesapp.notedetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.notesapp.R;
import com.example.notesapp.backendinteraction.ServerConstants;
import com.example.notesapp.backendinteraction.ServerResultReceiver;
import com.example.notesapp.backendinteraction.ServerSyncService;
import com.example.notesapp.data.NoteRepositories;
import com.example.notesapp.data.NotesServiceApiImpl;
import com.example.notesapp.util.EspressoIdlingResource;


/**
 * Main UI for the note detail screen.
 */
public class NoteDetailFragment extends Fragment implements NoteDetailContract.View {

    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";
    public static final String ARGUMENT_SERVER_ID = "SERVER_ID";

    private NoteDetailContract.UserActionsListener mActionsListener;

    private TextView mDetailTitle;

    private TextView mDetailDescription;

    private ImageView mDetailImage;

    public static NoteDetailFragment newInstance(String noteId,String serverId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NOTE_ID, noteId);
        arguments.putString(ARGUMENT_SERVER_ID, serverId);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new NoteDetailPresenter(NoteRepositories.getInMemoryRepoInstance(new NotesServiceApiImpl()),
                this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mDetailTitle = (TextView) root.findViewById(R.id.note_detail_title);
        mDetailDescription = (TextView) root.findViewById(R.id.note_detail_description);
        mDetailImage = (ImageView) root.findViewById(R.id.note_detail_image);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete : {
                mActionsListener.checkForUserConfirmation();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        String noteId = getArguments().getString(ARGUMENT_NOTE_ID);
        mActionsListener.openNote(noteId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
       if (active) {
           mDetailTitle.setText("");
            mDetailDescription.setText(getString(R.string.loading));
        }
    }


    public void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete this note");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String noteId = getArguments().getString(ARGUMENT_NOTE_ID);
                String serverId = getArguments().getString(ARGUMENT_SERVER_ID);
                Intent intent= new Intent(getActivity(), ServerSyncService.class);
                intent.putExtra("action", ServerConstants.ACTION_DELETE);
                intent.putExtra("id",noteId);
                intent.putExtra("serverId",serverId);
                getActivity().startService(intent);
                mActionsListener.deleteNote(noteId);

            }
        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                    // Create the AlertDialog object and return it
                     builder.create().show();

    }

    @Override
    public void goBackToList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void hideDescription() {
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void hideTitle() {
        mDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mDetailDescription.setVisibility(View.VISIBLE);
      mDetailDescription.setText(description);
    }

    @Override
    public void showTitle(String title) {
       mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setText(title);
    }

    @Override
    public void showImage(String imageUrl) {
        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment(); // App is busy until further notice.

        mDetailImage.setVisibility(View.VISIBLE);

        // This app uses Glide for image loading
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mDetailImage) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        EspressoIdlingResource.decrement(); // App is idle.
                    }
                });
    }

    @Override
    public void hideImage() {
        mDetailImage.setImageDrawable(null);
        mDetailImage.setVisibility(View.GONE);
    }

    @Override
    public void showMissingNote() {
        mDetailTitle.setText("");
        mDetailDescription.setText(getString(R.string.no_data));
    }
}
