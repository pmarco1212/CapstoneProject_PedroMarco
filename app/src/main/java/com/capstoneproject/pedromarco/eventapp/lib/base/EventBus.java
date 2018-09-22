package com.capstoneproject.pedromarco.eventapp.lib.base;

/**
 * Interface of the eventbus with its methods
 */
public interface EventBus {
    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Object event);
}
