package com.example.notesapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NotesActivityTest {
    @Rule
    public ActivityTestRule<NotesActivity> mActivityRule = new ActivityTestRule<>(NotesActivity.class);

    @Test
    public void fab_btn_onClick() {
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId())
    }
}