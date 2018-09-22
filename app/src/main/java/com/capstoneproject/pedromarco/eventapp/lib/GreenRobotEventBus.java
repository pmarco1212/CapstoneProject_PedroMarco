package com.capstoneproject.pedromarco.eventapp.lib;

import com.capstoneproject.pedromarco.eventapp.lib.base.EventBus;

/**
 * Class with the Green Robot EventBus logic
 */
public class GreenRobotEventBus implements EventBus {
    org.greenrobot.eventbus.EventBus eventBus;

    public GreenRobotEventBus() {
        eventBus = org.greenrobot.eventbus.EventBus.getDefault();
    }

    /**
     * Register a given subscriber to listen for events
     *
     * @param subscriber subscriber
     */
    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    /**
     * Unregister a given subscriber to stop listening for events
     *
     * @param subscriber subscriber
     */
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    /**
     * Post a given event
     *
     * @param event event to post
     */
    public void post(Object event) {
        eventBus.post(event);
    }
}
