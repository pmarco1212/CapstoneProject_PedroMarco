package com.capstoneproject.pedromarco.eventapp.main;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;


/**
 * Repository class, handling all the model-side data of the Main
 */
public class MainRepositoryImpl implements MainRepository {
    private FirebaseAPI firebaseAPI;

    public MainRepositoryImpl(FirebaseAPI firebaseAPI) {
        this.firebaseAPI = firebaseAPI;
    }

    /**
     * Log out from Firebase
     */
    @Override
    public void logout() {
        firebaseAPI.logout();
    }
}
