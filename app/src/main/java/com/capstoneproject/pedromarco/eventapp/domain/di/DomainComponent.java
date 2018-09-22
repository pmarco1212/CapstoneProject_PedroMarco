package com.capstoneproject.pedromarco.eventapp.domain.di;

import com.capstoneproject.pedromarco.eventapp.EventAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger Component interface of the Domain package
 */
@Singleton
@Component(modules = {DomainModule.class, EventAppModule.class})
public interface DomainComponent {
}
