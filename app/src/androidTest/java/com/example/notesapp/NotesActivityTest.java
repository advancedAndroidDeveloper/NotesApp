package com.example.notesapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.notesapp.notes.NotesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NotesActivityTest {

    @Rule
    public ActivityTestRule<NotesActivity> mActivityTestRule = new ActivityTestRule<>(NotesActivity.class);

    @Test
    public void notesActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_add_notes),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        onView(withId(R.id.add_note_title)).perform(typeText("test"), closeSoftKeyboard());

        String newNoteDescription = "UI testing for Android";
        onView(withId(R.id.add_note_description)).perform(typeText(newNoteDescription),
               closeSoftKeyboard());

        onView(withId(R.id.fab_add_notes)).perform(click());
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.notes_list),
                        withParent(allOf(withId(R.id.refresh_layout),
                                withParent(withId(R.id.contentFrame)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

    }
}
