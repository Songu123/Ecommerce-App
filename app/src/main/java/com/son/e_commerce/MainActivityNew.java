package com.son.e_commerce;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.son.e_commerce.view.fragment.CartFragment;
import com.son.e_commerce.view.fragment.ExploreFragment;
import com.son.e_commerce.view.fragment.HomeFragment;
import com.son.e_commerce.view.fragment.OrdersFragment;
import com.son.e_commerce.view.fragment.ProfileFragment;

public class MainActivityNew extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        setupBottomNavigation();

        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_explore) {
                fragment = new ExploreFragment();
            } else if (itemId == R.id.nav_cart) {
                fragment = new CartFragment();
            } else if (itemId == R.id.nav_orders) {
                fragment = new OrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }

    public void navigateToHome() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    public void navigateToCart() {
        bottomNavigation.setSelectedItemId(R.id.nav_cart);
    }

    public void navigateToExplore() {
        bottomNavigation.setSelectedItemId(R.id.nav_explore);
    }

    public void navigateToOrders() {
        bottomNavigation.setSelectedItemId(R.id.nav_orders);
    }

    public void navigateToProfile() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }
}
