package com.son.e_commerce.presenter.contract;

import com.son.e_commerce.model.entity.User;

public interface ProfileContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showUserProfile(User user);
        void showError(String message);
        void navigateToLogin();
        void navigateToEditProfile();
        void navigateToOrders();
        void navigateToSettings();
        void showLogoutSuccess();
    }

    interface Presenter {
        void loadUserProfile();
        void onEditProfileClick();
        void onOrdersClick();
        void onSettingsClick();
        void onLogoutClick();
        void onDestroy();
    }
}
