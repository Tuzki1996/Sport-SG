package com.example.chen_hsi.androidtutnonfregment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by GMD on 26/10/16.
 */

public class MyApplication extends Application {
    public ArrayList<Facility> getOriginalList() {
        return OriginalList;
    }

    public void setOriginalList(ArrayList<Facility> originalList) {
        OriginalList = originalList;
    }

    ArrayList<Facility> OriginalList=new ArrayList();

}
