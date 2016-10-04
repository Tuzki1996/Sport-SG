package com.example.chen_hsi.androidtutnonfregment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

public class FacilityAdapter extends ArrayAdapter<Facility> {
    ArrayList<Facility> list=new ArrayList();
    ArrayList<Facility> OriginalList=new ArrayList();
    private FacilityFilter filter;
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
                filterResults.count=filtered.size();
                filterResults.values=filtered;
            }
            else
            {
                synchronized (this)
                {
                    filterResults.count=OriginalList.size();
                    filterResults.values=OriginalList;
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
