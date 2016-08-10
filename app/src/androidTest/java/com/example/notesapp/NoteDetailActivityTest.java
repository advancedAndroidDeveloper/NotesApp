package com.example.notesapp;

import static org.junit.Assert.*;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteDetailActivityTest {
    @Rule
    public ActivityTestRule<NoteDetailActivity> mActivityRule = new ActivityTestRule<>(NoteDetailActivity.class);

    @Test
    public void fab_btn_onClick() {
        Espresso.onView(ViewMatchers.withId(R.id.et_note_details)).perform(ViewActions.typeText("Test description"));
        Espresso.onView(ViewMatchers.withId(R.id.et_title)).perform(ViewActions.typeText("Test title"));
        Espresso.onView(ViewMatchers.withId(R.id.et_title)).perform(ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.fab_save)).perform(ViewActions.click());
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }
}