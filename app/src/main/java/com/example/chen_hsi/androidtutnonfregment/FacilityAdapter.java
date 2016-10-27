package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

public class FacilityAdapter extends ArrayAdapter<Facility> {
    ArrayList<Facility> list=new ArrayList();
    ArrayList<Facility> OriginalList=new ArrayList();
    GPSTracker gps;
    public void setSortDistance(Boolean sortDistance) {
        this.sortDistance = sortDistance;
    }

    Boolean sortDistance=false;
    private FacilityFilter filter;

    public ArrayList<Integer> getSportlist() {
        return Sportlist;
    }

    public void setSportlist(ArrayList<Integer> sportlist) {
        Sportlist = sportlist;
    }

    ArrayList<Integer> Sportlist=new ArrayList<Integer>();
    public FacilityAdapter(Context context, int resource) {
        super(context, resource);

    }
    static  class DataHandler{
        ImageView Photo;
        TextView name;
        TextView address;
        RatingBar ratingBar;
    }

    @Override
    public void add(Facility object) {
        super.add(object);
        list.add(object);
        OriginalList.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Facility getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row=convertView;
        DataHandler handler;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.row_layout,parent,false);
            handler=new DataHandler();
            handler.Photo=(ImageView)row.findViewById(R.id.facility_photo);
            handler.name=(TextView)row.findViewById(R.id.facility_name);
            handler.address=(TextView)row.findViewById(R.id.facility_address);
            handler.ratingBar=(RatingBar)row.findViewById(R.id.ratingBarUser) ;
            row.setTag(handler);
        }
        else
        {
            handler=(DataHandler)row.getTag();
        }
        Facility dataProvider=(Facility)this.getItem(position);
        Picasso.with(getContext()).load(dataProvider.getFacility_photo_resource()).into(handler.Photo);
        handler.name.setText(dataProvider.getFacility_name());
        handler.address.setText(dataProvider.getFacility_address());
        handler.ratingBar.setRating((float)dataProvider.getFacility_rating());
        return row;
    }

    public Filter getFilter(){
        if(filter==null)
            filter=new FacilityFilter();
        return filter;
    }
    private class FacilityFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence cs) {
            cs=cs.toString().toLowerCase();
            FilterResults filterResults=new FilterResults();
            if(cs!=null&&cs.toString().length()>0)
            {
                ArrayList<Facility> filtered=new ArrayList<Facility>();
                for(int i=0;i<OriginalList.size();i++)
                {
                    Facility facility=OriginalList.get(i);
                    if(facility.getFacility_name().toLowerCase().contains(cs))
                        filtered.add(facility);
                }
                if(Sportlist.size()>0) {
                    ArrayList<Facility> sportFiltered=new ArrayList<Facility>();
                    for (int i = 0; i < filtered.size(); i++) {
                        Facility facility = filtered.get(i);
                        for (int j = 0; j < facility.getSportList().size(); j++) {
                            if(Sportlist.contains(facility.getSportList().get(j).getType()))
                            {
                                sportFiltered.add(facility);
                                break;
                            }
                        }
                    }
                    filtered=sportFiltered;
                }
                if(sortDistance)
                {

                    gps = new GPSTracker(getContext());
                    if(gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        LatLng latLng = new LatLng(latitude,longitude);

                        Toast.makeText(getContext(), latitude+""+longitude, Toast.LENGTH_LONG);
                        Collections.sort(filtered, new SortDistance(latLng));
                    }
                    else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        Toast.makeText(getContext(), "GPS Not Found", Toast.LENGTH_LONG);
                    }
                }
                else
                {
                    Collections.sort(filtered, new SortNames());
                }
                filterResults.count=filtered.size();
                filterResults.values=filtered;
            }
            else
            {
                synchronized (this)
                {
                    ArrayList<Facility> filtered=OriginalList;
                    if(Sportlist.size()>0) {
                        ArrayList<Facility> sportFiltered=new ArrayList<Facility>();
                        for (int i = 0; i < OriginalList.size(); i++) {
                            Facility facility = OriginalList.get(i);
                            for (int j = 0; j < facility.getSportList().size(); j++) {
                                if(Sportlist.contains(facility.getSportList().get(j).getType()))
                                {
                                    sportFiltered.add(facility);
                                    break;
                                }
                            }
                        }
                        filtered=sportFiltered;
                    }
                    if(sortDistance)
                    {

                        gps = new GPSTracker(getContext());

                        if(gps.canGetLocation()) {
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            LatLng latLng = new LatLng(latitude,longitude);

                            Toast.makeText(getContext(), latitude+""+longitude, Toast.LENGTH_LONG);
                            Collections.sort(filtered, new SortDistance(latLng));
                        }
                        else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            Toast.makeText(getContext(), "GPS Not Found", Toast.LENGTH_LONG);
                        }
                    }
                    else
                    {
                        Collections.sort(filtered, new SortNames());
                    }
                    filterResults.count=filtered.size();
                    filterResults.values=filtered;
                }
            }
            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list=(ArrayList<Facility>)filterResults.values;
            notifyDataSetChanged();
            clear();

            notifyDataSetInvalidated();
        }
    }
}
class SortDistance implements Comparator<Facility> {
    LatLng currentLoc;

    public SortDistance(LatLng current){
        currentLoc = current;
    }
    @Override
    public int compare(final Facility place1, final Facility place2) {
        double lat1 = place1.getFacility_latlng().latitude;
        double lon1 = place1.getFacility_latlng().longitude;
        double lat2 = place2.getFacility_latlng().latitude;
        double lon2 = place2.getFacility_latlng().longitude;

        double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
        double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
class SortNames implements Comparator<Facility> {
    @Override
    public int compare(Facility a, Facility b) {
        return a.getFacility_name().compareToIgnoreCase(b.getFacility_name());
    }
}