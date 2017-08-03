package com.johnpetitto.orachat;

import android.support.design.widget.TextInputLayout;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.johnpetitto.orachat.ui.AccountAccessActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginErrorValidationTest {
    @Rule
    public ActivityTestRule<AccountAccessActivity> activityRule =
            new ActivityTestRule<>(AccountAccessActivity.class);

    @Test
    public void hasEmailError() {
        onView(withId(R.id.login_email)).perform(typeText("bad email"));
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.login_email_layout)).check(
                matches(hasExpectedError(
                        activityRule.getActivity().getString(R.string.email_error)
                ))
        );
    }

    @Test
    public void hasPasswordError() {
        onView(withId(R.id.login_password)).perform(typeText("short"));
        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.login_password_layout)).check(
                matches(hasExpectedError(
                        activityRule.getActivity().getString(R.string.password_error)
                ))
        );
    }

    private static Matcher<View> hasExpectedError(String expectedError) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) item).getError();

                return error != null && expectedError.equals(error.toString());

            }

            @Override
            public void describeTo(Description description) {}
        };
    }
}
