package com.example.mobilleltar.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.mobilleltar.Adapters.ItemAdapter;
import com.example.mobilleltar.Adapters.ViewPagerAdapter;
import com.example.mobilleltar.DataItems.Item;
import com.example.mobilleltar.MainActivity;
import com.example.mobilleltar.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager layoutManager;
    private ItemAdapter adapter;
    private ArrayList<Item> myItems;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabChange tabChange;
    private Button editBtn;
    private String a, b, c, d;
    private int lastPos = -1;
    private int count = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public interface TabChange {
        void tabChangeListener(int index);

        void loadForChange(String cikkszam, String megnevezes1, String megnevezes2, String mennyiseg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("VALTOZAS","ONCREATEVIEW");
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        editBtn = (Button) view.findViewById(R.id.editButton);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout2);
        View child = getLayoutInflater().inflate(R.layout.header_proba, null);
        frameLayout.addView(child);

        myItems = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            myItems.add(new Item("500000dfghjfdghfdh", "2021.01.06", "ABC012345565562fghf47", "VALAMIdsklfnskljdnfa", i + 1));
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        adapter = new ItemAdapter(myItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        editBtn.setEnabled(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabChange.tabChangeListener(0);
                tabChange.loadForChange(a, b, c, d);
               // editBtn.setEnabled(false);
                 //Refresh2();
               // recyclerView.findViewHolderForAdapterPosition(lastPos+5).itemView.findViewById(R.id.frag_container).performClick();
               // adapter.notifyDataSetChanged();
                /* lastPos = -1;*/
                //count = 0;
            }
        });

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                adapter.notifyDataSetChanged();
                if (count == 0) {
                    editBtn.setEnabled(true);
                    a = myItems.get(position).getmRajzszam();
                    b = myItems.get(position).getmDatum();
                    c = myItems.get(position).getmValami();
                    d = String.valueOf(myItems.get(position).getmCount());
                    lastPos = position;
                   // count = 1;
                } else if (lastPos == position) {
                    editBtn.setEnabled(false);
                    count = 0;
                    lastPos = -1;
                }
            }

        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TabChange) {
            tabChange = (TabChange) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement tabchange");
        }
        Log.d("VALTAS", "ATTACH");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tabChange = null;
        Log.d("VALTAS", "DETACH");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("VALTAS", "STOP");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("VALTAS", "PAUSE");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("VALTAS", "RESUME");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("VALTAS", "START");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("VALTAS", "DESTROY");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("VALTAS", "DESTROY");
    }

    public void Refresh()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
    public void Refresh2()
    {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.frag_container);
        if (currentFragment instanceof MainFragment) {
        FragmentTransaction fragTransaction =   (getActivity()).getSupportFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();}
    }


}