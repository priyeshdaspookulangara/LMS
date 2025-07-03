package com.example.ecommerceapp.ui.auth;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.ecommerceapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    // Consider using ActivityScenarioRule for more concise setup
    // @Rule
    // public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        // Initialize Intents before each test
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents after each test
        Intents.release();
    }

    @Test
    public void loginScreen_UIElementsDisplayed() {
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.textViewRegisterLink)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRegisterLink_opensRegisterActivity() {
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.textViewRegisterLink)).perform(click());

        // Verify that an Intent to RegisterActivity was sent
        intended(hasComponent(RegisterActivity.class.getName()));
    }

    @Test
    public void loginAttempt_emptyEmail_showsError() {
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.buttonLogin)).perform(click());
        // Check for error on email field (assuming TextInputLayout shows error)
        // This requires TextInputLayout to be used correctly and error to be set on it.
        // The current LoginActivity sets error directly on EditText.
        onView(withId(R.id.editTextEmail)).check(matches(hasErrorText("Enter a valid email")));
    }

    @Test
    public void loginAttempt_invalidEmailFormat_showsError() {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editTextEmail)).perform(typeText("invalidemail"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.editTextEmail)).check(matches(hasErrorText("Enter a valid email")));
    }

    @Test
    public void loginAttempt_emptyPassword_showsError() {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editTextEmail)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.editTextPassword)).check(matches(hasErrorText("Password must be at least 6 characters")));
    }

    // Helper method for checking EditText error text
    // This is a common pattern for checking errors set by setError()
    public static org.hamcrest.Matcher<View> hasErrorText(String expectedError) {
        return new org.hamcrest.TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof android.widget.EditText)) {
                    return false;
                }
                android.widget.EditText editText = (android.widget.EditText) view;
                return editText.getError() != null && editText.getError().toString().equals(expectedError);
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has error text: " + expectedError);
            }
        };
    }

    // Note: Testing successful login would require mocking the AuthRepository
    // or setting up a test user, and then verifying navigation to MainActivity.
    // This can be complex with Espresso alone without a DI framework for repository mocking.
    // For this example, we focus on UI validation and basic navigation.
}
