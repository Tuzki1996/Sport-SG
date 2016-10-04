package com.example.chen_hsi.androidtutnonfregment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SPORTSG.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_FACILITY_QUERY =
            "CREATE TABLE " + Facility.newFacility.TABLE_NAME + " (FACILITYID INTEGER PRIMARY KEY," +
                    Facility.newFacility.FACILITY_NAME + " TEXT," +
                    Facility.newFacility.FACILITY_XADDR + " REAL, " +
                    Facility.newFacility.FACILITY_YADDR + " REAL, " +
                    Facility.newFacility.FACILITY_ADDR + " TEXT, " +
                    Facility.newFacility.FACILITY_PHONE + " TEXT, " +
                    Facility.newFacility.FACILITY_PHOTO+ " TEXT);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FACILITY_QUERY);
        Log.e("DATABASE OPERATION", "Database created / opened...");
    }
    public void addFacility(String name,double xaddr,double yaddr,String addr,String phone,String photo,SQLiteDatabase db){

        ContentValues contentValues=new ContentValues();
        contentValues.put(Facility.newFacility.FACILITY_NAME,name);
        contentValues.put(Facility.newFacility.FACILITY_ADDR,addr);
        contentValues.put(Facility.newFacility.FACILITY_XADDR,xaddr);
        contentValues.put(Facility.newFacility.FACILITY_YADDR,yaddr);
        contentValues.put(Facility.newFacility.FACILITY_PHONE,phone);
        contentValues.put(Facility.newFacility.FACILITY_PHOTO,photo);
        db.insert(Facility.newFacility.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATION", "Row inserted...");

    }
    public Cursor getFacility(SQLiteDatabase db)
    {
        Cursor cursor;
        String[] projections={Facility.newFacility.FACILITY_NAME,Facility.newFacility.FACILITY_XADDR,Facility.newFacility.FACILITY_YADDR,Facility.newFacility.FACILITY_ADDR,Facility.newFacility.FACILITY_PHONE,Facility.newFacility.FACILITY_PHOTO};
        cursor= db.query(Facility.newFacility.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}

