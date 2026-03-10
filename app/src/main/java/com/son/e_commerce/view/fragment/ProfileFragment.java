package com.son.e_commerce.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.son.e_commerce.AuthActivity;
import com.son.e_commerce.R;
import com.son.e_commerce.utils.TokenManager;

public class ProfileFragment extends Fragment {

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewFullName;
    private Button buttonLogout;
    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tokenManager = new TokenManager(requireContext());

        initViews(view);
        loadUserInfo();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewFullName = view.findViewById(R.id.textViewFullName);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        // Fallback if layout doesn't have these views yet
        if (textViewUsername == null) {
            TextView textView = view.findViewById(R.id.textView);
            if (textView != null) {
                textView.setText("Profile Screen\n\nUser: " + tokenManager.getUsername());
            }
        }
    }

    private void loadUserInfo() {
        if (textViewUsername != null) {
            textViewUsername.setText(tokenManager.getUsername() != null ? tokenManager.getUsername() : "N/A");
        }
        if (textViewEmail != null) {
            textViewEmail.setText(tokenManager.getEmail() != null ? tokenManager.getEmail() : "N/A");
        }
        if (textViewFullName != null) {
            textViewFullName.setText(tokenManager.getFullName() != null ? tokenManager.getFullName() : "N/A");
        }
    }

    private void setupListeners() {
        if (buttonLogout != null) {
            buttonLogout.setOnClickListener(v -> showLogoutConfirmation());
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> logout())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void logout() {
        // Clear token và user data
        tokenManager.clearToken();

        Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình login
        Intent intent = new Intent(requireContext(), AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
