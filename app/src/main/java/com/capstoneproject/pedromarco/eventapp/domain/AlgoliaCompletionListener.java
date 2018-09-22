package com.capstoneproject.pedromarco.eventapp.domain;

import com.capstoneproject.pedromarco.eventapp.entities.Event;

import java.util.LinkedHashMap;

/**
 * Listener interface to listen for completion events
 */
public interface AlgoliaCompletionListener {
    void requestCompleted(LinkedHashMap<String, Event> hashMapEvents);
}
