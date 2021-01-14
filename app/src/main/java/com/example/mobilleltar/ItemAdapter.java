package com.example.mobilleltar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {


   private static int position;

    private ArrayList<Item> mList;

    public interface OnItemClick
    {
        void onItemClick(int position);
    }

    private OnItemClick mListener;

    public void setOnItemClickListener(OnItemClick listener)
    {
        mListener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {


        private TextView textView;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;


        public ItemViewHolder(View itemView, final OnItemClick listener) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.text);
            textView2 = (TextView)itemView.findViewById(R.id.text2);
            textView3 = (TextView)itemView.findViewById(R.id.text3);
            textView4 = (TextView)itemView.findViewById(R.id.text4);
            textView5 = (TextView)itemView.findViewById(R.id.text6);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                         position = getAdapterPosition();
                         if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);

                        }
                    }
                }
            });

        }

    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        final ItemViewHolder iwh = new ItemViewHolder(view,mListener);


        return iwh;
    }

    public ItemAdapter(ArrayList<Item> item)
    {
        mList = item;

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position)
    {

        Item currentItem = mList.get(position);
        holder.textView.setText(currentItem.getmMertErtek());
        holder.textView2.setText(currentItem.getmDatum());
        holder.textView3.setText(currentItem.getmRajzszam());
        holder.textView4.setText(currentItem.getmValami());
        holder.textView5.setText(String.valueOf(currentItem.getmCount()));
        
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
