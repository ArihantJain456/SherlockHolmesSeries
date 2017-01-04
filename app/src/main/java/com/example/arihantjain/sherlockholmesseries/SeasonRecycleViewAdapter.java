package com.example.arihantjain.sherlockholmesseries;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Arihant Jain on 12/22/2016.
 */

public class SeasonRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;

    ArrayList<SeasonModel> seasonModels;

    public SeasonRecycleViewAdapter (Context context,ArrayList<SeasonModel> seasonModels){
        this.seasonModels = seasonModels;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.single_season_layout,parent,false);
        SeasonRecycleViewAdapter.Item item = new SeasonRecycleViewAdapter.Item(row);


        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((SeasonRecycleViewAdapter.Item)holder).textView.setText("Season : " + seasonModels.get(position).getSeasonNo());
        ((SeasonRecycleViewAdapter.Item) holder).textView2.setText(seasonModels.get(position).getSeasonDate());
        Picasso.with(context).load(seasonModels.get(position).getSeasonImage()).placeholder(R.drawable.progress_image).fit().into(((Item) holder).itemImage);
    }
    @Override
    public int getItemCount() {
        try {
            return seasonModels.size();
        }catch (NullPointerException e){
            e.printStackTrace();
            return 0;
        }
    }
    public class Item extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView textView;
        TextView textView2;
        public Item(View itemView1) {
            super(itemView1);
            textView = (TextView) itemView1.findViewById(R.id.item_title);
            textView2 = (TextView) itemView1.findViewById(R.id.item_detail);
            itemImage = (ImageView)itemView1.findViewById(R.id.item_image);
        }
    }
}
