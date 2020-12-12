package com.example.yelpfinalproject;

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

    private double[] resultRatings = {1,1,1,1,1};

    public double[] getResultRatings(){
        return this.resultRatings;
    }

    public void setResultRatings(double[] tempNew){
        this.resultRatings = tempNew;
    }

    private double[] resultLat = {1,1,1,1,1};

    public double[] getResultLat(){
        return this.resultLat;
    }

    public void setResultLat(double[] tempNew){
        this.resultLat = tempNew;
    }

    private double[] resultLon = {1,1,1,1,1};

    public double[] getResultLon(){
        return this.resultLon;
    }

    public void setResultLon(double[] tempNew){
        this.resultLon = tempNew;
    }

    private String[] resultPrice = {"1","1","1","1","1"};

    public String[] getResultPrice(){
        return this.resultPrice;
    }

    public void setResultPrice(String[] tempNew){
        this.resultPrice = tempNew;
    }

    private String[] resultPhone = {"1","1","1","1","1"};

    public String[] getResultPhone(){
        return this.resultPhone;
    }

    public void setResultPhone(String[] tempNew){
        this.resultPrice = tempNew;
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
}
