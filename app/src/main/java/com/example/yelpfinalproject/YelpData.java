package com.example.yelpfinalproject;

import java.util.Map;

public class YelpData {


    private static YelpData yd = new YelpData();

    public static YelpData getInstance() {
        return YelpData.yd;
    }

    private String[] resultNames = {"1","1","1","1","1"};

    public String[] getResultNames(){
        return this.resultNames;
    }

    public void setResultNames(String[] tempNew){
        this.resultNames = tempNew;
    }

    public void setResultNamesSpecific(String tempNew, int tempNew2){
        this.resultNames[tempNew2] = tempNew;
    }

    private double[] resultRatings = {1,1,1,1,1};

    public double[] getResultRatings(){
        return this.resultRatings;
    }

    public void setResultRatings(double[] tempNew){
        this.resultRatings = tempNew;
    }

    public void setResultRatingsSpecific(double tempNew, int tempNew2){
        this.resultRatings[tempNew2] = tempNew;
    }

    private double[] resultLat = {1,1,1,1,1};

    public double[] getResultLat(){
        return this.resultLat;
    }

    public void setResultLat(double[] tempNew){
        this.resultLat = tempNew;
    }

    public void setResultLatSpecific(double tempNew, int tempNew2){
        this.resultLat[tempNew2] = tempNew;
    }

    private double[] resultLon = {1,1,1,1,1};

    public double[] getResultLon(){
        return this.resultLon;
    }

    public void setResultLon(double[] tempNew){
        this.resultLon = tempNew;
    }

    public void setResultLonSpecific(double tempNew, int tempNew2){
        this.resultLon[tempNew2] = tempNew;
    }

    private String[] resultPrice = {"1","1","1","1","1"};

    public String[] getResultPrice(){
        return this.resultPrice;
    }

    public void setResultPrice(String[] tempNew){
        this.resultPrice = tempNew;
    }

    public void setResultPriceSpecific(String tempNew, int tempNew2){
        this.resultPrice[tempNew2] = tempNew;
    }

    private String[] resultPhone = {"1","1","1","1","1"};

    public String[] getResultPhone(){
        return this.resultPhone;
    }

    public void setResultPhone(String[] tempNew){
        this.resultPrice = tempNew;
    }

    public void setResultPhoneSpecific(String tempNew, int tempNew2){
        this.resultPrice[tempNew2] = tempNew;
    }

    //IDs of all the locations currently loaded. It is necessary for the Favorite saving functionality
    private String[] resultID = {"1","1","1","1","1"};

    public String[] getResultID(){
        return this.resultID;
    }

    public void setResultID(String[] tempNew){
        this.resultID = tempNew;
    }

    public void setResultIDSpecific(String tempNew, int tempNew2){
        this.resultID[tempNew2] = tempNew;
    }

    //current Latitude and Longitude for the Map usage & Current Location Name for the map
    public double currentLat = 1;

    public double getCurrentLat(){
        return this.currentLat;
    }

    public void setCurrentLat(double tempNew){
        this.currentLat = tempNew;
    }

    public double currentLon = 1;

    public double getCurrentLon(){
        return this.currentLon;
    }

    public void setCurrentLon(double tempNew){
        this.currentLon = tempNew;
    }

    public String currentLocationName = "1";

    public String getCurrentLocationName(){
        return this.currentLocationName;
    }

    public void setCurrentLocationName(String tempNew){
        this.currentLocationName = tempNew;
    }

    //gets the current UserID. This information is wiped on Log Out
    public String currentUserID = "1";

    public String getCurrentUserID(){
        return this.currentUserID;
    }

    public void setCurrentUserID(String tempNew){
        this.currentUserID = tempNew;
    }

    //gets the results from the database
    public Map<String, Object> map;

    public Map<String, Object> getDatabaseResults(){
        return this.map;
    }

    public void setDatabaseResults(Map<String, Object> tempNew){
        this.map = tempNew;
    }

    //if the Favorites is being ran or not
    public Boolean FavoritesRun = false;

    public Boolean getFavoritesRun(){
        return this.FavoritesRun;
    }

    public void setFavoritesRun(Boolean tempNew){
        this.FavoritesRun = tempNew;
    }
}
