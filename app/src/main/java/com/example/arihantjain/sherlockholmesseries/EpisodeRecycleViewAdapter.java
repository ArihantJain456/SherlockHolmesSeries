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

public class EpisodeRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<EpisodeModel> episodeModels;

    public EpisodeRecycleViewAdapter (Context context, ArrayList<EpisodeModel> episodeModels){
        this.context = context;
        this.episodeModels = episodeModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.single_item,parent,false);
        EpisodeRecycleViewAdapter.Item item = new EpisodeRecycleViewAdapter.Item(row);


        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((EpisodeRecycleViewAdapter.Item)holder).textView.setText(episodeModels.get(position).getEpisode());
        ((EpisodeRecycleViewAdapter.Item) holder).textView2.setText(episodeModels.get(position).getTitle());
        if(MainActivity.getCurrentSeason()!=4)
        Picasso.with(context).load(episodeModels.get(position).getImage()).placeholder(R.drawable.progress_image).fit().error(R.drawable.error_image).into(((Item) holder).itemImage);
        else
        ((Item) holder).itemImage.setImageResource(R.drawable.coming_soon);
    }

    @Override
    public int getItemCount() {
        try {
            return episodeModels.size();
        }catch (NullPointerException e){
            Toast.makeText(context,"Weak internet connection",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Error in loading data",Toast.LENGTH_LONG).show();
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
