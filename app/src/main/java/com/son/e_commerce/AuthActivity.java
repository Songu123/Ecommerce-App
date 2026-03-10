package com.son.e_commerce;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.son.e_commerce.view.fragment.LoginFragment;
import com.son.e_commerce.view.fragment.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG_LOGIN = "LoginFragment";
    private static final String TAG_REGISTER = "RegisterFragment";

    private ImageButton buttonBack;
    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initViews();
        setupListeners();

        // Check intent to decide which fragment to show
        String action = getIntent().getStringExtra("action");
        if ("register".equals(action)) {
            showRegisterFragment();
        } else {
            showLoginFragment();
        }
    }

    private void initViews() {
        buttonBack = findViewById(R.id.buttonBack);
        textViewTitle = findViewById(R.id.textViewTitle);
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    public void showLoginFragment() {
        textViewTitle.setText("Đăng nhập");

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_LOGIN);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
        }

        replaceFragment(fragment, TAG_LOGIN);
    }

    public void showRegisterFragment() {
        textViewTitle.setText("Đăng ký");

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_REGISTER);
        if (fragment == null) {
            fragment = RegisterFragment.newInstance();
        }

        replaceFragment(fragment, TAG_REGISTER);
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        );
        transaction.replace(R.id.fragmentContainer, fragment, tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (currentFragment instanceof RegisterFragment) {
            // If on register screen, go back to login
            showLoginFragment();
        } else {
            // If on login screen, exit the activity
            super.onBackPressed();
        }
    }
}
