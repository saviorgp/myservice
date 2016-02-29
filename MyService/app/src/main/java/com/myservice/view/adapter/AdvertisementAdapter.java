package com.myservice.view.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.component.Advertisement;

import java.util.ArrayList;

public class AdvertisementAdapter extends ArrayAdapter<Advertisement> {

    private ArrayList<Advertisement> advertisementList;
    private LayoutInflater inflater;

    public AdvertisementAdapter(Context context, int textViewResourceId, ArrayList<Advertisement> advertisementList) {
        super(context, textViewResourceId, advertisementList);

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.advertisementList = new ArrayList<Advertisement>();
        this.advertisementList.addAll(advertisementList);
    }

    private class ViewHolder {
        TextView title;
        TextView description;
    }

    public void add(Advertisement advertisement){
        Log.v("Serviço", advertisement.getTitle());

        this.advertisementList.add(advertisement);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;

        if (view == null) {

            int layout = R.layout.item_service;

            view = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.txt_item_title);
            holder.description = (TextView) view.findViewById(R.id.txt_item_description);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Advertisement advertisement = this.advertisementList.get(position);
        holder.title.setText(advertisement.getTitle());
        holder.description.setText(advertisement.getDescription());

        return view;

    }

}
