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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Chen-Hsi on 2016/10/3.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    ArrayList<Review> list=new ArrayList();
    public ReviewAdapter(Context context, int resource) {
        super(context, resource);

    }
    static  class DataHandler{
        TextView review;
        TextView detail;

    }

    @Override
    public void add(Review object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Review getItem(int position) {
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
            row=inflater.inflate(R.layout.review_list,parent,false);
            handler=new DataHandler();
            handler.detail=(TextView)row.findViewById(R.id.tDetails);
            handler.review=(TextView)row.findViewById(R.id.tReview);
            row.setTag(handler);
        }
        else
        {
            handler=(DataHandler)row.getTag();
        }
        Review dataProvider=(Review)this.getItem(position);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        handler.detail.setText(dataProvider.getAcc()+"     "+format1.format(dataProvider.getDate()));
        handler.review.setText(dataProvider.getText());

        return row;
    }



}
