package com.example.rss_reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewAdaper extends ArrayAdapter<Feed> {
    private Context mycontext;
    private int myresource;
    public NewAdaper(@NonNull Context context, int resource, @NonNull ArrayList<Feed> objects) {
        super(context, resource, objects);
        mycontext = context;
        myresource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mycontext);
        convertView = inflater.inflate(myresource,parent,false);
        ImageView image = convertView.findViewById(R.id.newImage);
        TextView titlle = convertView.findViewById(R.id.tittle);
        TextView date = convertView.findViewById(R.id.date);
        image.setImageBitmap(getItem(position).getImage());
        titlle.setText(getItem(position).getTitle());
        date.setText(getItem(position).getPubDate());


        return convertView;
    }
}
