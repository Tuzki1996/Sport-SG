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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

public class FacilityAdapter extends ArrayAdapter<Facility> {
    ArrayList<Facility> list=new ArrayList();
    ArrayList<Facility> OriginalList=new ArrayList();
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
