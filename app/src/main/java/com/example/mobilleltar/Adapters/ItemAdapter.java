package com.example.mobilleltar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobilleltar.DataItems.Item;
import com.example.mobilleltar.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> mList;
    private static int pos = -1;
    private static int lastPosition = -1;
    private static int counter = 0;
    private static SparseBooleanArray selectedItems = new SparseBooleanArray();
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


        public ItemViewHolder(final View itemView, final OnItemClick listener) {
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
                        pos = getAdapterPosition();
                           // if(counter == 0)
                         //   {
                            Log.d("IFELSE","IF");
                            if (pos != RecyclerView.NO_POSITION) {
                                listener.onItemClick(pos);
                                if (selectedItems.get(getAdapterPosition(), false)) {
                                    selectedItems.delete(getAdapterPosition());
                                    itemView.setSelected(false);
                                   // itemView.notifyAll();
                                    lastPosition = -1;

                                } else {
                                    selectedItems.put(getAdapterPosition(), true);
                                    itemView.setSelected(true);
                                    selectedItems.delete(getAdapterPosition());
                                 //   itemView.notifyAll();
                                    lastPosition = pos;
                                }
                            }
                               // counter = 1;
                          //  }else if(counter == 1 && pos == lastPosition)
                          /* {
                                listener.onItemClick(pos);
                                Log.d("IFELSE", "ELSE");
                                counter = 0;
                                //listener.onItemClick(pos);
                                if (selectedItems.get(lastPosition, false)) {
                                    selectedItems.delete(pos);
                                    itemView.setSelected(false);
                                    lastPosition = -1;

                                } else {
                                    selectedItems.put(lastPosition, true);
                                    itemView.setSelected(true);
                                    //lastPosition = pos;
                                }*/
                           // }
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
        if(lastPosition == position)
        {
            selectedItems.get(position,true);
            holder.itemView.setSelected(true);
           // selectedItems.delete(position);

        }else
        {
            selectedItems.get(position,false);
            holder.itemView.setSelected(false);
            //selectedItems.delete(position);
            //lastPosition = -1;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
