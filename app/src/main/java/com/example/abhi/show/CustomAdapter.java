package com.example.abhi.show;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abhi on 19/01/15.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ArrayList<EntryData> entriesData;
    private Context mContext;

    public CustomAdapter(Context context,ArrayList<EntryData> entriesData){
        this.entriesData = entriesData;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public ViewHolder(View v){
            super(v);
            imageView = (ImageView)v.findViewById(R.id.imageView);
        }
        public ImageView getImageView(){ return imageView; }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(mContext).load(entriesData.get(position).getImageUrl()).into(holder.getImageView());
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,EntryActivity.class);
                intent.putExtra("Selected",position);
                intent.putExtra("EntryId",entriesData.get(position).getEntryId());
                intent.putExtra("EntryUrl",entriesData.get(position).getImageUrl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return entriesData.size();
    }

    public void add(EntryData item, int position) {
        entriesData.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(EntryData item) {
        int position = entriesData.indexOf(item);
        entriesData.remove(position);
        notifyItemRemoved(position);
    }

}
