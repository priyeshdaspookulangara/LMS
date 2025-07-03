package com.example.ecommerceapp.ui.checkout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.R;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG_SHIPPING = "shipping_fragment";
    private static final String TAG_PAYMENT = "payment_fragment";
    private static final String TAG_SUMMARY = "summary_fragment";

    private CheckoutViewModel checkoutViewModel;
    private int currentStep = 0; // 0: Shipping, 1: Payment, 2: Summary

    private Button buttonNext, buttonPrevious;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.toolbarCheckout);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        buttonNext = findViewById(R.id.buttonCheckoutNext);
        buttonPrevious = findViewById(R.id.buttonCheckoutPrevious);

        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);

        if (savedInstanceState == null) {
            loadFragment(new ShippingInfoFragment(), TAG_SHIPPING);
            updateStepUI();
        } else {
            // Restore current step from savedInstanceState if needed, or rely on FragmentManager
            currentStep = savedInstanceState.getInt("currentStep", 0);
            updateStepUI();
        }


        buttonNext.setOnClickListener(v -> handleNextStep());
        buttonPrevious.setOnClickListener(v -> handlePreviousStep());

        observeViewModel();
    }

    private void observeViewModel() {
        checkoutViewModel.getOrderPlacementResult().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                // Navigate to an Order Confirmation screen or back to home
                // For now, just finish CheckoutActivity
                setResult(RESULT_OK); // Indicate success to CartActivity if it started this
                finish();
            }
        });

        checkoutViewModel.getOrderPlacementError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, "Order placement failed: " + error, Toast.LENGTH_LONG).show();
                // Potentially allow user to retry or go back
            }
        });
    }

    private void handleNextStep() {
        switch (currentStep) {
            case 0: // Currently on Shipping
                ShippingInfoFragment shippingFragment = (ShippingInfoFragment) getSupportFragmentManager().findFragmentByTag(TAG_SHIPPING);
                if (shippingFragment != null && shippingFragment.validateAndSave()) {
                    currentStep = 1;
                    loadFragment(new PaymentMethodFragment(), TAG_PAYMENT);
                } else {
                    Toast.makeText(this, "Please fill all shipping details correctly.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1: // Currently on Payment
                PaymentMethodFragment paymentFragment = (PaymentMethodFragment) getSupportFragmentManager().findFragmentByTag(TAG_PAYMENT);
                if (paymentFragment != null && paymentFragment.validateAndSave()) {
                    checkoutViewModel.prepareOrderForSummary(); // Prepare data for summary
                    currentStep = 2;
                    loadFragment(new OrderSummaryFragment(), TAG_SUMMARY);
                } else {
                    Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2: // Currently on Summary
                // "Next" button here is "Place Order"
                checkoutViewModel.placeOrder();
                // Observer will handle success/failure
                break;
        }
        updateStepUI();
    }

    private void handlePreviousStep() {
        switch (currentStep) {
            case 1: // Currently on Payment, go back to Shipping
                currentStep = 0;
                loadFragment(new ShippingInfoFragment(), TAG_SHIPPING); // Or popBackStack if using addToBackStack
                break;
            case 2: // Currently on Summary, go back to Payment
                currentStep = 1;
                loadFragment(new PaymentMethodFragment(), TAG_PAYMENT);
                break;
            // No previous from Shipping (step 0)
        }
        updateStepUI();
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerCheckout, fragment, tag);
        // if (!tag.equals(TAG_SHIPPING)) { // Don't add initial fragment to backstack
        //     transaction.addToBackStack(tag);
        // }
        transaction.commit();
    }

    private void updateStepUI() {
        switch (currentStep) {
            case 0: // Shipping
                getSupportActionBar().setTitle("Shipping Information");
                buttonPrevious.setVisibility(View.GONE);
                buttonNext.setText("Next: Payment");
                buttonNext.setVisibility(View.VISIBLE);
                break;
            case 1: // Payment
                getSupportActionBar().setTitle("Payment Method");
                buttonPrevious.setVisibility(View.VISIBLE);
                buttonNext.setText("Next: Order Summary");
                buttonNext.setVisibility(View.VISIBLE);
                break;
            case 2: // Summary
                getSupportActionBar().setTitle("Order Summary");
                buttonPrevious.setVisibility(View.VISIBLE);
                buttonNext.setText("Place Order");
                buttonNext.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (currentStep > 0) {
                handlePreviousStep();
            } else {
                onBackPressed(); // Or finish()
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentStep", currentStep);
    }

    // Optional: Override onBackPressed to handle fragment back navigation if using addToBackStack
    // @Override
    // public void onBackPressed() {
    //     if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
    //         getSupportFragmentManager().popBackStack();
    //         // You'll need to figure out the currentStep based on which fragment is now visible
    //     } else {
    //         super.onBackPressed();
    //     }
    // }
}
