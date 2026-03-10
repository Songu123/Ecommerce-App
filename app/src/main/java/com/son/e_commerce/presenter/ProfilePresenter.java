package com.son.e_commerce.presenter;

import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.contract.ProfileContract;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;
    private UserRepository userRepository;

    public ProfilePresenter(ProfileContract.View view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
    }

    @Override
    public void loadUserProfile() {
        User currentUser = userRepository.getCurrentUser();
        if (currentUser == null) {
            view.navigateToLogin();
            return;
        }

        view.showLoading();
        userRepository.getUserById(currentUser.getId(), new UserRepository.OnUserLoadedListener() {
            @Override
            public void onSuccess(User user) {
                if (view != null) {
                    view.hideLoading();
                    view.showUserProfile(user);
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void onEditProfileClick() {
        if (view != null) {
            view.navigateToEditProfile();
        }
    }

    @Override
    public void onOrdersClick() {
        if (view != null) {
            view.navigateToOrders();
        }
    }

    @Override
    public void onSettingsClick() {
        if (view != null) {
            view.navigateToSettings();
        }
    }

    @Override
    public void onLogoutClick() {
        userRepository.logout();
        if (view != null) {
            view.showLogoutSuccess();
            view.navigateToLogin();
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
