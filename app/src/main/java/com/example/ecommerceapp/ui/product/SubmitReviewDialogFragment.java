package com.example.ecommerceapp.ui.product;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class SubmitReviewDialogFragment extends DialogFragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private String productId;
    private ProductViewModel productViewModel; // To call submitReview

    private RatingBar ratingBarSubmit;
    private EditText editTextComment;
    private TextInputLayout textInputLayoutComment;

    public static SubmitReviewDialogFragment newInstance(String productId) {
        SubmitReviewDialogFragment fragment = new SubmitReviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString(ARG_PRODUCT_ID);
        }
        // Initialize ViewModel - scoped to the hosting Activity (ProductDetailActivity)
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_submit_review, null);

        ratingBarSubmit = view.findViewById(R.id.ratingBarSubmit);
        editTextComment = view.findViewById(R.id.editTextReviewComment);
        textInputLayoutComment = view.findViewById(R.id.textInputLayoutReviewComment);

        builder.setView(view)
                .setPositiveButton("Submit", null) // Set to null to override and prevent auto-dismiss
                .setNegativeButton("Cancel", (dialog, id) -> SubmitReviewDialogFragment.this.getDialog().cancel());

        AlertDialog dialog = builder.create();

        // Override positive button click to handle validation and submission
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                int rating = (int) ratingBarSubmit.getRating();
                String comment = editTextComment.getText().toString().trim();

                if (rating == 0) {
                    Toast.makeText(getContext(), "Please provide a rating.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Comment is optional, no specific validation needed unless requirements change

                productViewModel.submitReview(productId, rating, comment);
                // Observe submission result in ProductDetailActivity
                dismiss(); // Dismiss dialog after attempting submission
            });
        });

        return dialog;
    }
}
