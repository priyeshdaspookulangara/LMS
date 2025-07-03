package com.example.ecommerceapp.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.EditText; // For dummy card fields

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.R;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout; // For dummy card fields

public class PaymentMethodFragment extends Fragment {

    private CheckoutViewModel checkoutViewModel;
    private RadioGroup radioGroupPaymentMethods;
    private MaterialRadioButton radioButtonCreditCard, radioButtonPayPal; // Add more as needed

    // Dummy fields for "Credit Card" simulation
    private TextInputLayout textInputCardNumber, textInputExpiry, textInputCVV;
    private EditText editTextCardNumber, editTextExpiry, editTextCVV;
    private View layoutCreditCardFields;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);

        radioGroupPaymentMethods = view.findViewById(R.id.radioGroupPaymentMethods);
        radioButtonCreditCard = view.findViewById(R.id.radioButtonCreditCard);
        radioButtonPayPal = view.findViewById(R.id.radioButtonPayPal);

        layoutCreditCardFields = view.findViewById(R.id.layoutCreditCardFields);
        textInputCardNumber = view.findViewById(R.id.textInputCardNumber);
        editTextCardNumber = view.findViewById(R.id.editTextCardNumber);
        textInputExpiry = view.findViewById(R.id.textInputCardExpiry);
        editTextExpiry = view.findViewById(R.id.editTextCardExpiry);
        textInputCVV = view.findViewById(R.id.textInputCardCVV);
        editTextCVV = view.findViewById(R.id.editTextCardCVV);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkoutViewModel = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);

        // Pre-select based on ViewModel if a method was already chosen
        checkoutViewModel.getSelectedPaymentMethod().observe(getViewLifecycleOwner(), method -> {
            if (method != null) {
                if (method.equals(radioButtonCreditCard.getText().toString())) {
                    radioButtonCreditCard.setChecked(true);
                } else if (method.equals(radioButtonPayPal.getText().toString())) {
                    radioButtonPayPal.setChecked(true);
                }
                // Add more else-if for other payment methods
            }
        });

        radioGroupPaymentMethods.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonCreditCard) {
                layoutCreditCardFields.setVisibility(View.VISIBLE);
            } else {
                layoutCreditCardFields.setVisibility(View.GONE);
            }
            // Save selected method to ViewModel immediately or in validateAndSave()
            // For now, let's save in validateAndSave
        });

        // Initial visibility based on current selection (if any)
        if (radioButtonCreditCard.isChecked()) {
            layoutCreditCardFields.setVisibility(View.VISIBLE);
        } else {
            layoutCreditCardFields.setVisibility(View.GONE);
        }
    }

    public boolean validateAndSave() {
        int selectedId = radioGroupPaymentMethods.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(getContext(), "Please select a payment method.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String paymentMethod;
        boolean isCardDetailsValid = true;

        if (selectedId == R.id.radioButtonCreditCard) {
            paymentMethod = radioButtonCreditCard.getText().toString();
            // Simulate validation for dummy card fields
            String cardNumber = editTextCardNumber.getText().toString().trim();
            String expiry = editTextExpiry.getText().toString().trim();
            String cvv = editTextCVV.getText().toString().trim();

            if (cardNumber.isEmpty() || cardNumber.length() < 13) { // Basic length check
                textInputCardNumber.setError("Enter a valid card number");
                isCardDetailsValid = false;
            } else {
                textInputCardNumber.setError(null);
            }
            if (expiry.isEmpty() || !expiry.matches("\\d{2}/\\d{2}")) { // MM/YY format
                textInputExpiry.setError("Enter valid expiry (MM/YY)");
                isCardDetailsValid = false;
            } else {
                textInputExpiry.setError(null);
            }
            if (cvv.isEmpty() || cvv.length() < 3) {
                textInputCVV.setError("Enter valid CVV");
                isCardDetailsValid = false;
            } else {
                textInputCVV.setError(null);
            }

        } else if (selectedId == R.id.radioButtonPayPal) {
            paymentMethod = radioButtonPayPal.getText().toString();
            // For PayPal, usually involves redirecting to PayPal's SDK/website.
            // No local form validation needed here beyond selection.
        } else {
            // Handle other payment methods
            paymentMethod = "Unknown"; // Should not happen if IDs are managed correctly
        }

        if (isCardDetailsValid) { // Only save if all details for the selected method are valid
            checkoutViewModel.updatePaymentMethod(paymentMethod);
            // In a real app, for credit cards, you'd get a token from a payment gateway here,
            // not store raw card details.
            return true;
        }
        return false;
    }
}
