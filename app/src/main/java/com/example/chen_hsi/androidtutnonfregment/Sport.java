package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by GMD on 5/10/16.
 */

public class Sport {
    public enum sport_type{Badminton,Basketball,Netball,Squash,TableTennis,Volleyball,field};
    private int type;
    private double price;
    private int id;
    public Sport(int type,double price,int id)
    {
        this.type=type;
        this.price=price;
        this.id=id;
    }

    public int getType()
    {
        return type;
    }
    public void setType(int type){
        this.type=type;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public int getId()
    {
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
}