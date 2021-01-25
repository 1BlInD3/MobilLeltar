package com.example.mobilleltar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobilleltar.DataItems.PolcItems;
import com.example.mobilleltar.R;

import java.util.ArrayList;

public class PolcItemAdapter extends RecyclerView.Adapter<PolcItemAdapter.PolcitemViewHolder> {

    private ArrayList<PolcItems> mPolcItems;

    public static class PolcitemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mennyiseg;
        private TextView unit;
        private TextView megnevezes1;
        private TextView megnevezes2;
        private TextView intRem;
        private TextView allapot;

        public PolcitemViewHolder(@NonNull View itemView) {
            super(itemView);

            mennyiseg = (TextView)itemView.findViewById(R.id.mennyisegText1);
            unit = (TextView)itemView.findViewById(R.id.polcText);
            megnevezes1 = (TextView)itemView.findViewById(R.id.raktarText1);
            megnevezes2 = (TextView)itemView.findViewById(R.id.megnevezes2);
            intRem = (TextView)itemView.findViewById(R.id.intRemText);
            allapot = (TextView)itemView.findViewById(R.id.allapot);

        }
    }

    @NonNull
    @Override
    public PolcitemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.polc_view,viewGroup,false);
        PolcitemViewHolder polcitemViewHolder = new PolcitemViewHolder(v);
        return polcitemViewHolder;
    }

    public PolcItemAdapter (ArrayList<PolcItems> polcItems)
    {
        mPolcItems = polcItems;
    }

    @Override
    public void onBindViewHolder(@NonNull PolcitemViewHolder polcitemViewHolder, int i) {

        PolcItems currentPolcItem = mPolcItems.get(i);
        polcitemViewHolder.mennyiseg.setText(String.valueOf(currentPolcItem.getmMennyiseg()));
        polcitemViewHolder.unit.setText(currentPolcItem.getmEgyseg());
        polcitemViewHolder.megnevezes1.setText(currentPolcItem.getmMegnevezes1());
        polcitemViewHolder.megnevezes2.setText(currentPolcItem.getmMegnevezes2());
        polcitemViewHolder.intRem.setText(currentPolcItem.getmIntRem());
        polcitemViewHolder.allapot.setText(currentPolcItem.getmAllapot());

    }

    @Override
    public int getItemCount() {
        return mPolcItems.size();
    }
}
