package com.example.mobilleltar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobilleltar.DataItems.CikkItems;
import com.example.mobilleltar.R;

import java.util.ArrayList;

public class CikkItemAdapter extends RecyclerView.Adapter<CikkItemAdapter.CikkItemHolder> {

    private ArrayList<CikkItems> mItems = new ArrayList<>();

    public static class CikkItemHolder extends RecyclerView.ViewHolder
    {

        private TextView mennyisegTxt;
        private TextView polcTxt;
        private TextView raktarTxt;
        private TextView allapotTxt;

        public CikkItemHolder(@NonNull View itemView) {
            super(itemView);

            mennyisegTxt = (TextView)itemView.findViewById(R.id.mennyisegText1);
            polcTxt = (TextView)itemView.findViewById(R.id.polcText);
            raktarTxt = (TextView)itemView.findViewById(R.id.raktarText1);
            allapotTxt = (TextView)itemView.findViewById(R.id.allapotText);
        }
    }

    @NonNull
    @Override
    public CikkItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cikk_view,viewGroup,false);
        CikkItemHolder cikkItemHolder = new CikkItemHolder(view);
        return cikkItemHolder;
    }

    public CikkItemAdapter(ArrayList<CikkItems> items)
    {
        mItems = items;
    }

    @Override
    public void onBindViewHolder(@NonNull CikkItemHolder cikkItemHolder, int i) {

        CikkItems cuurentCikkItem = mItems.get(i);
        cikkItemHolder.mennyisegTxt.setText(String.valueOf(cuurentCikkItem.getmMennyiseg()));
        cikkItemHolder.polcTxt.setText(cuurentCikkItem.getmPolc());
        cikkItemHolder.raktarTxt.setText(cuurentCikkItem.getmRaktar());
        cikkItemHolder.allapotTxt.setText(cuurentCikkItem.getmAllapot());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
