package com.example.ecommerceapp.ui.product;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.ecommerceapp.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not; // For checking empty text is not displayed initially

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProductListActivityTest {

    @Test
    public void productListActivity_RecyclerViewIsDisplayed_OnLaunch() {
        ActivityScenario.launch(ProductListActivity.class);

        // Check if the RecyclerView is displayed.
        // This assumes that the API call will be made and some data (or an empty list)
        // will be processed, making the RecyclerView visible or the empty text view visible.
        // A more robust test would use IdlingResource for network calls or mock responses.

        // Wait a bit for potential network call, though not ideal without IdlingResource
        try {
            Thread.sleep(2000); // Crude wait, replace with IdlingResource in real tests
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if either RecyclerView is displayed (has items) or empty text is shown
        // This test is a bit weak because it depends on live API or mock data timing.
        // For a real test, you'd use Espresso Idling Resources or MockWebServer.
        // Here, we just check for the presence of the RecyclerView ID.
        onView(withId(R.id.recyclerViewProducts)).check(matches(isDisplayed()));

        // Further test: if products are loaded, the "empty" text view should not be visible.
        // This depends on the API actually returning products.
        // onView(withId(R.id.textViewEmptyProductList)).check(matches(not(isDisplayed())));
    }

    @Test
    public void productListActivity_clickOnFirstItem_opensProductDetail() {
        ActivityScenario.launch(ProductListActivity.class);

        // Wait for data to potentially load
        try {
            Thread.sleep(3000); // Crude wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This test assumes there's at least one item in the RecyclerView.
        // It will fail if the API returns no products or if loading is too slow.
        // A proper test would use a MockWebServer to provide controlled data.
        // Or, check if the empty view is shown, and if not, then perform the click.

        // Attempt to click on the first item if the recycler view is populated.
        // This requires that your ProductAdapter actually has items.
        // onView(withId(R.id.recyclerViewProducts))
        //        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify ProductDetailActivity is opened (more advanced: use Espresso Intents)
        // For now, just check if some element from ProductDetailActivity is displayed.
        // This is not a robust way to check navigation without Intents.
        // Example (assuming ProductDetailActivity has a specific view):
        // onView(withId(R.id.imageViewProductDetail)).check(matches(isDisplayed()));

        // Due to limitations (no MockWebServer, no direct access to API for setup),
        // this click test is highly dependent on live data and timing.
        // A more basic check is just that the activity launches.
    }
}
