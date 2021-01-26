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
import android.widget.Toast;

import com.example.mobilleltar.Adapters.PolcItemAdapter;
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
 * Use the {@link PolcResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PolcResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private String sql ="";


    // TODO: Rename and change types of parameters
   // private String mParam1;
  //  private String mParam2;
    private ArrayList<PolcItems> myPolcItems = new ArrayList<>();

    public PolcResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment CikkResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PolcResultFragment newInstance(String param1) {
        PolcResultFragment fragment = new PolcResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cikk_result, container, false);

        FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.polcHeaderFrame);
        View child = getLayoutInflater().inflate(R.layout.polc_header,null);
        frameLayout.addView(child);

        myPolcItems.clear();
        LoadPolc();

        recyclerView = (RecyclerView)view.findViewById(R.id.polcRecycler);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(view.getContext());
        adapter = new PolcItemAdapter(myPolcItems);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void LoadPolc()
    {
        ArrayList<PolcItems> test = (ArrayList<PolcItems>) getArguments().getSerializable("polc");
        for(int i = 0; i < test.size(); i++)
        {
            myPolcItems.add(new PolcItems(test.get(i).getmMennyiseg(),test.get(i).getmEgyseg(),test.get(i).getmMegnevezes1(),test.get(i).getmMegnevezes2(),test.get(i).getmIntRem(),test.get(i).getmAllapot()));
        }
    }
}