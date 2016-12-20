package com.sbo_app;

import org.json.JSONArray;
import org.json.JSONObject;

public class Trip {
    private String routeId;
    private String routeDirection;
    private String busPlate;
    private String  routeName;
    private JSONArray passengers;

    public Trip(String routeId, String routeDirection, String busPlate,
                String routeName){
        this.setRouteId(routeId);
        this.setRouteDirection(routeDirection);
        this.setBusPlate(busPlate);
        this.setRouteName(routeName);
        passengers = new JSONArray();
    }


    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteDirection() {
        return routeDirection;
    }

    public void setRouteDirection(String routeDirection) {
        this.routeDirection = routeDirection;
    }

    public String getBusPlate() {
        return busPlate;
    }

    public void setBusPlate(String busPlate) {
        this.busPlate = busPlate;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public JSONArray getPassengers() {
        return passengers;
    }

    public void addPassenger(JSONObject passenger) {
        this.passengers.put(passenger);
    }
}
