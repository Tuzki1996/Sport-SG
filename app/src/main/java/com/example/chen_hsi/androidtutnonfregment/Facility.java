package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import com.google.android.gms.*;
import com.google.android.gms.maps.model.LatLng;

public class Facility   implements Serializable {
    private String facility_photo_resource;
    private String facility_name;
    private String facility_address;
    private double facility_lng;
    private double facility_lat;
    private String facility_phone;
    private  double facility_rating;
    private  String facility_description;
    private int facility_id;
    private transient LatLng facility_latlng;
    public double getFacility_rating() {
        return facility_rating;
    }


    public void setFacility_rating(double facility_rating) {
        this.facility_rating = facility_rating;
    }

    public String getFacility_description() {
        return facility_description;
    }

    public void setFacility_description(String facility_description) {
        this.facility_description = facility_description;
    }

    public LatLng getFacility_latlng() {
        return facility_latlng;
    }

    public void setFacility_latlng(LatLng facility_latlng) {
        this.facility_latlng = facility_latlng;
    }


    public int getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(int facility_id) {
        this.facility_id = facility_id;
    }

    public ArrayList<Review> getReviewList() {
        return ReviewList;
    }

    public void setReviewList(ArrayList<Review> reviewList) {
        ReviewList = reviewList;
    }

    public int[][] getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int[][] occupancy) {
        this.occupancy = occupancy;
    }

    private ArrayList<Sport> SportList=new ArrayList<Sport>();
    private ArrayList<Review> ReviewList=new ArrayList<Review>();
    private int occupancy[][]=new int[20][14];

    public Facility(int facility_id,String facility_name, String facility_address, double facility_lng, double facility_lat, String facility_phone, String facility_photo_resource,String facility_description,Double facility_rating) {
        this.facility_name = facility_name;
        this.facility_address = facility_address;
        this.facility_lng = facility_lng;
        this.facility_lat = facility_lat;
        this.facility_phone = facility_phone;
        this.facility_photo_resource = facility_photo_resource;
        this.facility_id=facility_id;
        this.facility_description=facility_description;
        this.facility_rating=facility_rating;
        this.facility_latlng=new LatLng(facility_lat,facility_lng);
    }


    public ArrayList<Sport> getSportList() {
        return SportList;
    }
    public void setSportList(ArrayList<Sport> sportList) {
        SportList = sportList;
    }

    public double getFacility_lng() {
        return facility_lng;
    }

    public void setFacility_lng(double facility_lng) {
        this.facility_lng = facility_lng;
    }

    public double getFacility_lat() {
        return facility_lat;
    }

    public void setFacility_lat(double facility_lat) {
        this.facility_lat = facility_lat;
    }

    public String getFacility_phone() {
        return facility_phone;
    }

    public void setFacility_phone(String facility_phone) {
        this.facility_phone = facility_phone;
    }

    public String getFacility_photo_resource() {
        return facility_photo_resource;
    }

    public void setFacility_photo_resource(String facility_photo_resource) {
        this.facility_photo_resource = facility_photo_resource;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getFacility_address() {
        return facility_address;
    }

    public void setFacility_address(String facility_address) {
        this.facility_address = facility_address;
    }
    public void addSport(Sport sport)
    {
        this.SportList.add(sport);
    }
    public void addReview(Review review)
    {
        this.ReviewList.add(review);
    }
    public void clearReviewList()
    {
        this.ReviewList.clear();
    }
}
