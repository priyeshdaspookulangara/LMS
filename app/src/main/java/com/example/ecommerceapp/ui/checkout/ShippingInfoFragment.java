package com.example.ecommerceapp.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class ShippingInfoFragment extends Fragment {

    private CheckoutViewModel checkoutViewModel;

    private TextInputLayout textInputFullName, textInputAddress1, textInputAddress2;
    private TextInputLayout textInputCity, textInputState, textInputPostalCode, textInputCountry, textInputPhone;
    private EditText editTextFullName, editTextAddress1, editTextAddress2;
    private EditText editTextCity, editTextState, editTextPostalCode, editTextCountry, editTextPhone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping_info, container, false);

        textInputFullName = view.findViewById(R.id.textInputShippingFullName);
        editTextFullName = view.findViewById(R.id.editTextShippingFullName);
        textInputAddress1 = view.findViewById(R.id.textInputShippingAddress1);
        editTextAddress1 = view.findViewById(R.id.editTextShippingAddress1);
        textInputAddress2 = view.findViewById(R.id.textInputShippingAddress2);
        editTextAddress2 = view.findViewById(R.id.editTextShippingAddress2);
        textInputCity = view.findViewById(R.id.textInputShippingCity);
        editTextCity = view.findViewById(R.id.editTextShippingCity);
        textInputState = view.findViewById(R.id.textInputShippingState);
        editTextState = view.findViewById(R.id.editTextShippingState);
        textInputPostalCode = view.findViewById(R.id.textInputShippingPostalCode);
        editTextPostalCode = view.findViewById(R.id.editTextShippingPostalCode);
        textInputCountry = view.findViewById(R.id.textInputShippingCountry);
        editTextCountry = view.findViewById(R.id.editTextShippingCountry);
        textInputPhone = view.findViewById(R.id.textInputShippingPhone);
        editTextPhone = view.findViewById(R.id.editTextShippingPhone);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkoutViewModel = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);

        // Observe LiveData from ViewModel to pre-fill fields if data exists
        checkoutViewModel.getShippingFullName().observe(getViewLifecycleOwner(), name -> editTextFullName.setText(name));
        checkoutViewModel.getShippingAddressLine1().observe(getViewLifecycleOwner(), addr1 -> editTextAddress1.setText(addr1));
        checkoutViewModel.getShippingAddressLine2().observe(getViewLifecycleOwner(), addr2 -> editTextAddress2.setText(addr2));
        checkoutViewModel.getShippingCity().observe(getViewLifecycleOwner(), city -> editTextCity.setText(city));
        checkoutViewModel.getShippingState().observe(getViewLifecycleOwner(), state -> editTextState.setText(state));
        checkoutViewModel.getShippingPostalCode().observe(getViewLifecycleOwner(), code -> editTextPostalCode.setText(code));
        checkoutViewModel.getShippingCountry().observe(getViewLifecycleOwner(), country -> editTextCountry.setText(country));
        checkoutViewModel.getShippingPhoneNumber().observe(getViewLifecycleOwner(), phone -> editTextPhone.setText(phone));
    }

    public boolean validateAndSave() {
        boolean isValid = true;

        String fullName = editTextFullName.getText().toString().trim();
        String address1 = editTextAddress1.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String state = editTextState.getText().toString().trim();
        String postalCode = editTextPostalCode.getText().toString().trim();
        String country = editTextCountry.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String address2 = editTextAddress2.getText().toString().trim(); // Optional

        if (fullName.isEmpty()) {
            textInputFullName.setError("Full name is required");
            isValid = false;
        } else {
            textInputFullName.setError(null);
        }

        if (address1.isEmpty()) {
            textInputAddress1.setError("Address line 1 is required");
            isValid = false;
        } else {
            textInputAddress1.setError(null);
        }

        if (city.isEmpty()) {
            textInputCity.setError("City is required");
            isValid = false;
        } else {
            textInputCity.setError(null);
        }

        if (state.isEmpty()) {
            textInputState.setError("State is required");
            isValid = false;
        } else {
            textInputState.setError(null);
        }

        if (postalCode.isEmpty()) {
            // Add more specific validation for postal code format based on country if needed
            textInputPostalCode.setError("Postal code is required");
            isValid = false;
        } else {
            textInputPostalCode.setError(null);
        }

        if (country.isEmpty()) {
            textInputCountry.setError("Country is required");
            isValid = false;
        } else {
            textInputCountry.setError(null);
        }

        if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
            // Basic phone validation
            textInputPhone.setError("Valid phone number is required");
            isValid = false;
        } else {
            textInputPhone.setError(null);
        }


        if (isValid) {
            checkoutViewModel.updateShippingFullName(fullName);
            checkoutViewModel.updateShippingAddressLine1(address1);
            checkoutViewModel.updateShippingAddressLine2(address2); // Can be empty
            checkoutViewModel.updateShippingCity(city);
            checkoutViewModel.updateShippingState(state);
            checkoutViewModel.updateShippingPostalCode(postalCode);
            checkoutViewModel.updateShippingCountry(country);
            checkoutViewModel.updateShippingPhoneNumber(phone);
        }
        return isValid;
    }
}
