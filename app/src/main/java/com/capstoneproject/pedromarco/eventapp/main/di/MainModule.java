package com.capstoneproject.pedromarco.eventapp.main.di;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.capstoneproject.pedromarco.eventapp.domain.FirebaseAPI;
import com.capstoneproject.pedromarco.eventapp.main.MainInteractor;
import com.capstoneproject.pedromarco.eventapp.main.MainInteractorImpl;
import com.capstoneproject.pedromarco.eventapp.main.MainPresenter;
import com.capstoneproject.pedromarco.eventapp.main.MainPresenterImpl;
import com.capstoneproject.pedromarco.eventapp.main.MainRepository;
import com.capstoneproject.pedromarco.eventapp.main.MainRepositoryImpl;
import com.capstoneproject.pedromarco.eventapp.main.ui.MainView;
import com.capstoneproject.pedromarco.eventapp.main.ui.adapters.ViewPagerAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module providing all the Main package dependencies (except for the tabs subpackage)
 */
@Module
public class MainModule {

    private MainView view;
    private String[] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public MainModule(MainView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        this.view = view;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(MainView view, MainInteractor mainInteractor) {
        return new MainPresenterImpl(view, mainInteractor);
    }


    @Provides
    @Singleton
    MainInteractor providesMainInteractor(MainRepository repository) {
        return new MainInteractorImpl(repository);
    }

    @Provides
    @Singleton
    MainRepository providesMainRepository(FirebaseAPI firebase) {
        return new MainRepositoryImpl(firebase);
    }

    @Provides
    @Singleton
    ViewPagerAdapter providesViewPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        return new ViewPagerAdapter(fm, fragments, titles);
    }

    @Provides
    @Singleton
    FragmentManager providesAdapterFragmentManager() {
        return this.fragmentManager;
    }

    @Provides
    @Singleton
    Fragment[] providesFragmentArrayForAdapter() {
        return this.fragments;
    }

    @Provides
    @Singleton
    String[] providesStringArrayForAdapter() {
        return this.titles;
    }
}
