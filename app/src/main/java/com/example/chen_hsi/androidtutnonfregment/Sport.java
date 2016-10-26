package com.example.chen_hsi.androidtutnonfregment;

import java.io.Serializable;

/**
 * Created by GMD on 5/10/16.
 */

public class Sport  implements Serializable
{
    public enum SPORT_TYPE{
        BADMINTON("Badminton"),
        Basketball("Basketball"),
        PickleBall("Pickle Ball"),
        SOCCER("Soccer"),
        SQUASH("Squash"),
        TABLETENNIS("Table tennis"),
        TENNIS("Tennis"),
        SWIMMING("Swimming"),
        RUGBY("Rugby"),
        GYM("Gym");
        private String name;
        SPORT_TYPE(String name){this.name=name;}
        public String getName(){return this.name;}
        public static String[] names() {
            SPORT_TYPE[] sport_types = values();
            String[] names = new String[sport_types.length];

            for (int i = 0; i < sport_types.length; i++) {
                names[i] = sport_types[i].name();
            }

            return names;
        }
    }
    private int type;
    private double price;
    private int id;
    private SPORT_TYPE sport_type;

    public SPORT_TYPE getSport_type() {
        return sport_type;
    }

    public void setSport_type(SPORT_TYPE sport_type) {
        this.sport_type = sport_type;
    }

    public Sport(int type, double price, int id)
    {

        this.type=type;
        this.price=price;
        this.id=id;
        this.sport_type=SPORT_TYPE.values()[type];
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