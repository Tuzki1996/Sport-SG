package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by GMD on 13/10/16.
 */
import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private String acc;
    private String text;
    private Date date;
    private double rating;
    private int id;
    public Review(int id,String acc, String text, Date date, double rating) {
        this.acc = acc;
        this.text = text;
        this.date = date;
        this.rating = rating;
        this.id = id;
    }


    public void setAcc(String acc) {
        this.acc = acc;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating()
    {
        return rating;
    }
    public String getText()
    {
        return text;
    }
    public Date getDate(){
        return date;
    }
    public String getAcc()
    {
        return acc;
    }
}
