package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

import java.io.Serializable;


public class Facility implements Serializable {
    private String facility_photo_resource;
    private String facility_name;
    private String facility_address;
    private double facility_xaddr;
    private double facility_yaddr;
    private String facility_phone;
   // private ArrayList<Sport> SportList;
   // private ArrayList<Review> ReviewList;
    private int occupancy[][]=new int[20][14];

    public Facility(String facility_name, String facility_address, double facility_xaddr, double facility_yaddr, String facility_phone, String facility_photo_resource) {
        this.facility_name = facility_name;
        this.facility_address = facility_address;
        this.facility_xaddr = facility_xaddr;
        this.facility_yaddr = facility_yaddr;
        this.facility_phone = facility_phone;
        this.facility_photo_resource = facility_photo_resource;
    }



    public static abstract class newFacility
    {
        public static final String FACILITY_NAME="facility_name";
        public static final String FACILITY_XADDR="facility_xaddr";
        public static final String FACILITY_YADDR="facility_yaddr";
        public static final String FACILITY_ADDR="facility_addr";
        public static final String FACILITY_PHONE="facility_phone";
        public static final String FACILITY_PHOTO="facility_photo";
        public static final String TABLE_NAME="Facility";
    }
    public Facility(String facility_photo_resource,String facility_name,String facility_address)
    {
        this.setFacility_photo_resource(facility_photo_resource);
        this.setFacility_address(facility_address);
        this.setFacility_name(facility_name);
    }

    public double getFacility_xaddr() {
        return facility_xaddr;
    }

    public void setFacility_xaddr(double facility_xaddr) {
        this.facility_xaddr = facility_xaddr;
    }

    public double getFacility_yaddr() {
        return facility_yaddr;
    }

    public void setFacility_yaddr(double facility_yaddr) {
        this.facility_yaddr = facility_yaddr;
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
}
