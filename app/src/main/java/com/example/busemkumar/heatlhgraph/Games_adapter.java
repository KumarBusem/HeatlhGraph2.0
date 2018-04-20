package com.example.busemkumar.heatlhgraph;

/**
 * Created by apiiit-rkv on 18/4/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class Games_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Games_Data> data= Collections.emptyList();
    Context con;
    Games_Data current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Games_adapter(Context context, List<Games_Data> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.game_container, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final Games_Data current=data.get(position);
        myHolder.textFishName.setText(current.GameName);
        myHolder.textType.setText(current.players_count + " Players");
        Glide.with(context).load(current.Game_url).into(myHolder.ivFish);
        myHolder.ivFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent=new Intent(con,Game_Information_activity.class);
                intent.putExtra("key",current.Game_id);
                con.startActivity(intent);
            }
        });
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textFishName;
        ImageView ivFish;
        TextView textType;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            con=itemView.getContext();
            textFishName= (TextView) itemView.findViewById(R.id.textFishName);
            ivFish= (ImageView) itemView.findViewById(R.id.ivFish);
            textType = (TextView) itemView.findViewById(R.id.textCount);
        }

    }

}
