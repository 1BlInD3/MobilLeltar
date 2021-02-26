package com.example.mobilleltar.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilleltar.Adapters.CikkItemAdapter;
import com.example.mobilleltar.DataItems.CikkItems;
import com.example.mobilleltar.DataItems.PolcItems;
import com.example.mobilleltar.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CikkResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CikkResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<CikkItems> myCikkItems = new ArrayList<>();
    private String sql ="";
    public CikkResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
    // * @param param2 Parameter 2.
     * @return A new instance of fragment CikkResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CikkResultFragment newInstance(String param1,String param2, String param3) {
        CikkResultFragment fragment = new CikkResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM2, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cikk_result2, container, false);

        TextView megjegyzes1Txt = (TextView)view.findViewById(R.id.megjegyzes1CikkText);
        TextView megjegyzes2Txt = (TextView)view.findViewById(R.id.megjegyzes2CikkText);
        TextView unitTxt = (TextView)view.findViewById(R.id.unitCikkText);
        TextView intRemTxt = (TextView)view.findViewById(R.id.intRemCikkText);

        megjegyzes1Txt.setText(getArguments().getString("megjegyzes"));
        megjegyzes2Txt.setText(getArguments().getString("megjegyzes2"));
        unitTxt.setText(getArguments().getString("unit"));
        intRemTxt.setText(getArguments().getString("intrem"));

        FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.cikkHeaderFrame);
        View child = getLayoutInflater().inflate(R.layout.cikk_header,null);
        frameLayout.addView(child);

        myCikkItems.clear();
        LoadCikk();
        recyclerView = (RecyclerView)view.findViewById(R.id.cikkRecycler);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(view.getContext());
        adapter = new CikkItemAdapter(myCikkItems);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        frameLayout.setFocusable(false);

        return view;
    }

    private void LoadCikk()
    {
        ArrayList<CikkItems> test = (ArrayList<CikkItems>)getArguments().getSerializable("cikk");
        for(int i = 0; i<test.size(); i++)
        {
            myCikkItems.add(new CikkItems(test.get(i).getmMennyiseg(),test.get(i).getmPolc(),test.get(i).getmRaktar(),test.get(i).getmAllapot()));
        }
    }
}