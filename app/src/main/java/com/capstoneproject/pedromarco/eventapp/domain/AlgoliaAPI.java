package com.capstoneproject.pedromarco.eventapp.domain;

import com.algolia.search.saas.AbstractQuery;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.capstoneproject.pedromarco.eventapp.entities.CurrentLocation;
import com.capstoneproject.pedromarco.eventapp.entities.Event;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Class declaring and defining the methods to interact with Algolia
 */
public class AlgoliaAPI {
    private static final String EVENTS_INDEX = "eventApp";
    private Index eventsIndex;

    public AlgoliaAPI(Client algoliaClient) {
        eventsIndex = algoliaClient.getIndex(EVENTS_INDEX);
        setAttributesForFacetting();
    }

    /**
     * Set the attributes to facet when doing the search in Algolia (only the category attribute)
     */
    private void setAttributesForFacetting() {
        try {
            eventsIndex.setSettingsAsync(
                    new JSONObject().put("attributesForFaceting",
                            (new JSONArray()
                                    .put("searchable(category)"))
                    )
                    , null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to add or update a given event in Algolia
     *
     * @param event event to update
     * @return true if sucesfull or false otherwise
     */
    public boolean addorUpdateEvent(Event event) {
        JSONObject eventJSON = getJSONObjectFromEvent(event);
        if (eventJSON != null) {
            try {
                eventsIndex.partialUpdateObjectAsync(eventJSON, eventJSON.get("id").toString(), true, null);
                return true;
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Method to convert an Event to a JSONobjet
     *
     * @param event event to convert into JSON
     * @return the object if success or null otherwise
     */
    private JSONObject getJSONObjectFromEvent(Event event) {
        JSONObject eventJSON = null;
        try {
            eventJSON = new JSONObject().put("id", event.getId())
                    .put("date", event.getDate())
                    .put("numericdate", getNumericDate(event.getDate())) //this field is only for  Algolia search purposes, is not in the Event model
                    .put("_geoloc", new JSONObject().put("lat", event.getLatitude()).put("lng", event.getLongitude())) //this field is only for  Algolia search purposes, is not in the Event model
                    .put("time", event.getTime())
                    .put("name", event.getName())
                    .put("description", event.getDescription())
                    .put("rating", event.getRating())
                    .put("category", event.getCategory())
                    .put("location", event.getLocation())
                    .put("latitude", event.getLatitude())
                    .put("longitude", event.getLongitude())
                    .put("photoURL", event.getPhotoURL())
                    .put("creator", event.getCreator())
                    .put("maxPeople", event.getMaxPeople())
                    .put("currentPeople", event.getCurrentPeople());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventJSON;
    }

    /**
     * Use a formula to convert the date to numeric (as required in Algolia)
     * with the formula year * 10000 + month * 100 + day
     *
     * @param date date to convert
     * @return the date in numeric
     */
    private int getNumericDate(String date) {
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));

        return year * 10000 + month * 100 + day;
    }

    /**
     * Get the current date in numeric, in order to compare with today's date in queries
     *
     * @return
     */
    private int getCurrentDateNumeric() {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        return getNumericDate(date);
    }

    /**
     * Get all events nearby (up to 1000 events, sorted by distance)
     *
     * @param listener completion listener
     */
    public void getNearByEvents(AlgoliaCompletionListener listener) {
        searchEvents("", 100, null, null, null, listener);
    }

    /**
     * Search for all events with the given parameters
     *
     * @param keywords keywords to search
     * @param distance max distance to the event
     * @param dateFrom min date
     * @param dateTo   max date
     * @param category category oft he event
     * @param listener completion listener
     */
    public void searchEvents(String keywords, int distance, String dateFrom, String dateTo, String category, final AlgoliaCompletionListener listener) {
        String filters = "";
        Query query = new Query();

        if (dateFrom != null && !dateFrom.isEmpty()) { //If a datefrom is set add it to filter
            filters = filters.concat("numericdate>=" + getNumericDate(dateFrom));
        } else { //Add the current date as minimun date to retrieve only future events
            filters = filters.concat("numericdate>=" + getCurrentDateNumeric());
        }
        if (dateTo != null && !dateTo.isEmpty()) { //if there is a final date, add it to the filter
            filters = filters.concat(" AND numericdate<=" + getNumericDate(dateTo));
        } //if there is a category set, add it to the filter
        if (category != null && !category.isEmpty()) {
            filters = filters.concat(" AND category:\"" + category + "\"");
        }

        double currentLatitude = CurrentLocation.getLatitude();
        double currentLongitude = CurrentLocation.getLongitude();
        if (currentLatitude != 0.0 || currentLongitude != 0.0) { //if there is a currentlocation already asigned, use this location for the search
            query.setAroundLatLng(new AbstractQuery.LatLng(currentLatitude, currentLongitude));
        } else { //if not, use the IP address
            query.setAroundLatLngViaIP(true);
        }
        query.setQuery(keywords);
        query.setFacets("*");
        if (distance == 0) { //if its 0, its the minimun, so 5kilometers
            query.setAroundRadius(5000);
        } else if (distance == 100) {
            //is the maximum, so no minimum set
        } else {
            query.setAroundRadius(distance * 1000);
        }
        query.setFilters(filters);
        eventsIndex.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                listener.requestCompleted(getEventsFromJSONObject(jsonObject));
            }
        });
    }

    /**
     * Get the events included in the JSON object returned from the query and return them in a HashMap
     *
     * @param object the JSONObject reply of the query
     * @return a LinkedHashMap with all events obtained
     */
    private LinkedHashMap<String, Event> getEventsFromJSONObject(JSONObject object) {
        LinkedHashMap<String, Event> hashMapEvents = null;
        try {
            if (object != null) {
                JSONArray hits = object.getJSONArray("hits");
                hashMapEvents = new LinkedHashMap<>();
                for (int i = 0; i < hits.length(); i++) {
                    JSONObject eventObject = hits.getJSONObject(i);
                    Event event = new Gson().fromJson(eventObject.toString(), Event.class);
                    hashMapEvents.put(event.getId(), event);
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return hashMapEvents;
    }
}
