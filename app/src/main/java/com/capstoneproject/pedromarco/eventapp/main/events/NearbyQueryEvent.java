package com.capstoneproject.pedromarco.eventapp.main.events;

import com.capstoneproject.pedromarco.eventapp.entities.Event;

import java.util.List;

/**
 * EventBus event model of the Nearby tab subpackage
 */
public class NearbyQueryEvent {
    public final static int onSucefullQuery = 0;
    public final static int onFailQuery = 1;
    public final static int onNoResultsQuery = 2;

    private int eventType;
    private String errorMesage;
    private List<Event> eventList;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMesage() {
        return errorMesage;
    }

    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
