package com.myservice.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myservice.R;
import com.myservice.model.component.Advertisement;
import com.myservice.utils.DownloadImagemUtil;

import java.util.ArrayList;

public class AdvertisementAdapter extends ArrayAdapter<Advertisement> {

    private LayoutInflater inflater;
    private DownloadImagemUtil downloader;
    private Activity context;

    public AdvertisementAdapter(Activity context, ArrayList<Advertisement> advertisementList) {
        super(context, R.layout.item_service, advertisementList);

        downloader = new DownloadImagemUtil(context);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView title;
        TextView description;
        ImageView imgFoto;
        ProgressBar progress;
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

            holder.imgFoto = (ImageView) view.findViewById(R.id.img);
            holder.imgFoto.setDrawingCacheEnabled(true);
            holder.progress = (ProgressBar) view.findViewById(R.id.progress);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Advertisement advertisement = getItem(position);
        holder.title.setText(advertisement.getTitle());
        holder.description.setText(advertisement.getCategory().getName());
        holder.imgFoto.setDrawingCacheEnabled(true);
        holder.imgFoto.setImageBitmap(null);

        if(advertisement.getImages().size() > 0){
            downloader.download(context, advertisement.getImages().get(0).getUrl(), holder.imgFoto, holder.progress);
        }
        else{
            downloader.stop(holder.imgFoto, holder.progress, R.drawable.sample_img_item);
        }

        return view;

    }

}
