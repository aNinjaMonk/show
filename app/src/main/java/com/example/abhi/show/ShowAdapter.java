package com.example.abhi.show;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abhi on 24/02/15.
 */
public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Show> shows;

    public ShowAdapter(Context context, ArrayList<Show> shows){
        this.shows = shows;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView showBrief;
        private final TextView showTitle;

        public ViewHolder(View v){
            super(v);
            showBrief = (ImageView)v.findViewById(R.id.showBrief);
            showTitle = (TextView)v.findViewById(R.id.showTitle);
        }

        public ImageView getShowBrief() {
            return showBrief;
        }

        public TextView getShowTitle() {
            return showTitle;
        }
    }

    @Override
    public void onBindViewHolder(ShowAdapter.ViewHolder holder, int position) {
        Picasso.with(mContext).load(shows.get(position).getBriefUrl()).into(holder.showBrief);
        holder.showTitle.setText(shows.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    @Override
    public ShowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_list,parent,false);
        return new ViewHolder(v);
    }

}
